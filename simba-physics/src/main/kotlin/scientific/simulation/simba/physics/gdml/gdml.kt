package ru.mipt.npm.mcengine.geant4.gdml

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import ru.mipt.npm.mcengine.utils.*
import java.io.File
import javax.xml.XMLConstants
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

//TODO(Заменит карты на классы)
//TODO(доработать функционал)
//TODO(Разобраться с ресиверами и декомпозировать GDMLDocument)
//TODO(Доделать проверку повторяющихся имен)
//TODO(Сделать нормальное упорядочивание в structure)

//TODO
//fun GDMLDocument.applyAll(vararg actions: GDMLDocument.() -> Unit) = actions.forEach{ it() }
//GDMLDocument().applyAll(createContainer, createBGODetector, createTarget() )

@Target(AnnotationTarget.PROPERTY)
annotation class GDMLExclude


@Target(AnnotationTarget.CLASS)
annotation class GDMLName(val name: String)

open class GDMLElement {
    private var level: Int = 0
    @GDMLExclude
    public var order: Int = 0
    private val attributes = mutableMapOf<String, String>()
    @GDMLExclude
    val children = mutableListOf<GDMLElement>()

    protected fun <T : GDMLElement> doInit(child: T, init: T.() -> Unit) {
        child.init()
        children.add(child)
    }

    override fun toString(): String {
        children.sortBy { it.order }
        children.forEach { it.level = level + 1 }
        val offset = level * "\t"
        val name = this::class.findAnnotation<GDMLName>()?.name
                ?: this::class.simpleName.toString().toLowerCase()
        val attributes = getAttributesString()
        if (children.isEmpty()) {
            return "${offset}<$name${attributes}/>\n"
        } else {
            return "${offset}<$name${attributes}>\n" +
                    "${children.joinToString("")}${offset}" +
                    "</$name>\n"
        }
    }

    private fun getAttributesString(): String = javaClass.kotlin.memberProperties
            .filter { it.findAnnotation<GDMLExclude>() == null }
            .filter { it.get(this) != null }
            .joinToString("") { " ${it.name}=\"${it.get(this)}\"" }

    fun getNameAttrs(): List<String> {
        val names = mutableListOf<String>()
        javaClass.kotlin.memberProperties.filter { it.name == "name" }.forEach {
            names.add(it.get(this).toString())
        }
        if (children.isNotEmpty()) {
            children.map { it.getNameAttrs() }.forEach { names.addAll(it) }
        }
        return names
    }

}


private const val HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"

class GDMLDocument() {

    fun buildGeant(action: GDMLDocument.() -> Unit) = this.apply(action)
    private val CENTER_POSITION = POSITION("center", 0.0, 0.0, 0.0)
    private val schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
            javaClass.getResource("/gdml/schema/gdml.xsd")
    )
    private val validator = schema.newValidator()

    private val topTag = GDML()
    // KClass<*> заменить на подтип  KClass<GDMLElement>

    //    private val mainTag = object {
//        val define = DEFINE()
//        val materials = MATERIALS()
//        val solids = SOLIDS()
//        val structure = STRUCTURE()
//        val setup = SETUP()
//    }
    private val mapOfChildren = HashMap<KClass<*>, MutableMap<String, GDMLElement>>()
    private val mapOfMainTag = HashMap<KClass<*>, GDMLElement>()

    init {
        mapOfMainTag.apply {
            put(DEFINE::class, DEFINE())
            put(MATERIALS::class, MATERIALS())
            put(SOLIDS::class, SOLIDS())
            put(STRUCTURE::class, STRUCTURE())
            put(SETUP::class, SETUP())

        }
                .forEach {
                    mapOfChildren[it.key] = HashMap<String, GDMLElement>()
                    topTag.children.add(it.value)
                }
    }

    //Настройки мирового объема
    val WORLD_DEFAULT_NAME = "World"
    private var worldTag = WORLD("Vol" + WORLD_DEFAULT_NAME)

    init {
        mapOfMainTag[SETUP::class]!!.children.add(worldTag)
    }

    //DSL
    fun world(name: String) {
        worldTag = WORLD("Vol" + name)
    }

    fun position(name: String, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = POSITION(name, x, y, z)
    fun position(name: String): POSITION {
        val pos = mapOfChildren[DEFINE::class]!![name] ?: throw Exception("<define> haven't tag with name ${name}")
        return pos as? POSITION ?: throw Exception("Tag ${name} isn't position")
    }

    fun solid(name: String): Solid {
        return mapOfChildren[SOLIDS::class]!![name] as Solid
                ?: throw Exception("<solids> haven't tag with name ${name}")
    }

    private var structureOrder = 1

    fun volume(name: String, reciever: VOLUME.() -> Unit = {}): VOLUME {
        val vol = mapOfChildren[STRUCTURE::class]!!["Vol" + name] as VOLUME?
                ?: throw Exception("Volume with name ${name} don't exist")
        val tempOrder = structureOrder
        structureOrder = 1
        vol.apply(reciever)
        structureOrder = tempOrder
        return vol
    }

    fun volume(name: String,
               material: Material,
               solid: Solid,
               physvols: VOLUME.() -> Unit = { }): VOLUME {
        logger.debug { "Add volume ${name}" }
        structureOrder++

        val material_ : Material
        if (material is UserMaterial){
            material_ = Material(material.name)
            if (!mapOfChildren[STRUCTURE::class]!!.containsKey("Mat" + name)){
                mapOfChildren[MATERIALS::class]!!.put(material.name, material)
            }
            }
            else{
            material_ = material
        }

        if (mapOfChildren[STRUCTURE::class]!!.containsKey("Vol" + name)) {
            throw Exception("Another volume have name ${name}")
        } else {
            mapOfChildren[STRUCTURE::class]!!["Vol" + name] = VOLUME(name, material_, solid).apply(physvols).apply { order = structureOrder }
        }
        if (!mapOfChildren[SOLIDS::class]!!.containsKey(solid.name)) {
            mapOfChildren[SOLIDS::class]!!.put(solid.name, solid)
        }
        return mapOfChildren[STRUCTURE::class]!!.get("Vol" + name) as VOLUME
    }

    inner class PHYSVOL : GDMLElement() {
        fun volume(name: String,
                   material: Material,
                   solid: Solid,
                   physvols: VOLUME.() -> Unit = { }) {


            val volume = this@GDMLDocument.volume(name, material, solid, physvols)
            this.children.add(VOLUMEREF(volume.name))
        }
    }


    inner class VOLUME(name: String,
                       @GDMLExclude
                       val material: Material,
                       @GDMLExclude
                       val solid: Solid) : GDMLElement() {
        val name = "Vol" + name

        init {
            with(children) {
                add(material)
                add(SOLIDREF(solid.name))
            }
        }

        private fun _physvol(position: POSITION): PHYSVOL {
            if (!mapOfChildren[DEFINE::class]!!.containsKey(position.name)) {
                mapOfChildren[DEFINE::class]!!.put(position.name, position)
            }
            val physvol = PHYSVOL()
            physvol.children.add(POSITIONREF(position.name))
            this.children.add(physvol)
            return physvol
        }

        fun physvol(volume: VOLUME, position: POSITION = CENTER_POSITION) = _physvol(position).children.add(VOLUMEREF(volume.name))
        fun physvol(position: POSITION = CENTER_POSITION, reciver: PHYSVOL.() -> Unit) = _physvol(position).apply(reciver)

        fun auxiliary(type: String, value: Any, reciver: AUXILIARY.() -> Unit = {}): AUXILIARY {
            val auxiliary = AUXILIARY(type, value).apply(reciver)
            this.children.add(auxiliary)
            return auxiliary
        }

        fun auxiliary(auxiliary: AUXILIARY) {
            this.children.add(auxiliary)
        }
    }

    //Не полная реализация логического вычитания
    inner class Subtraction(name: String, first: Solid, second: Solid) : Solid(name) {
        init {
            with(children) {
                add(FIRST(first.name))
                add(SECOND(second.name))
            }
            order = 1
            for (solid in listOf(first, second)) {
                if (!mapOfChildren[SOLIDS::class]!!.containsKey(solid.name)) {
                    mapOfChildren[SOLIDS::class]!!.put(solid.name, solid)
                }
            }
        }
    }

    // служебные функции
    private fun update() {

        for (vol in mapOfChildren[STRUCTURE::class]!!.values) {
            if (vol.children.size <= 2) {
                vol.order = -1
            }
            if (vol.children.size == 3){
                for (ch in vol.children){
                    if (ch is AUXILIARY){
                        vol.order = -1
                    }
                }
            }
        }

        mapOfChildren.filterValues { it.isNotEmpty() }.forEach { t, u ->
            mapOfMainTag[t]!!.children.clear()
            mapOfMainTag[t]!!.children.addAll(u.values)
        }
    }

    // вывод документа
    override fun toString(): String {
        update()//Костыль
//    checkRefName()
        val text = (HEADER + topTag.toString())
        // Документы не валидные с точки зрения схемы, в некоторых случаях валидны с точки зрения geant, TODO(Обработка исключения валидации)
//        validator.validate( StreamSource(text.byteInputStream()))
        return text
    }
}

//Элемент верхнего уровня
class GDML : GDMLElement()

//Пять основных элементов, их порядок(order) важен
class DEFINE : GDMLElement() {
    init {
        order = 1
    }
}

class MATERIALS : GDMLElement() {
    init {
        order = 2
    }
}

class SOLIDS : GDMLElement() {
    init {
        order = 3
    }
}

class STRUCTURE : GDMLElement() {
    init {
        order = 4
    }
}

class SETUP(val version: String = "1.0", val name: String = "Default") : GDMLElement() {
    init {
        order = 5
    }
}

class AUXILIARY(val auxtype: String, val auxvalue: Any) : GDMLElement() {
    fun auxiliary(type: String, value: Any, reciver: AUXILIARY.() -> Unit = {}): AUXILIARY {
        val auxiliary = AUXILIARY(type, value).apply(reciver)
        children.add(auxiliary)
        return auxiliary
    }

    fun auxiliary(auxiliary: AUXILIARY) {
        children.add(auxiliary)
    }
}

// тэги-ссылки на элементы
class SOLIDREF(val ref: String) : GDMLElement()

class VOLUMEREF(val ref: String) : GDMLElement()
class WORLD(val ref: String) : GDMLElement()
class POSITIONREF(val ref: String) : GDMLElement()

// вычислительные тэги

class POSITION(val name: String, val x: Double, val y: Double, val z: Double) : GDMLElement() {
    val unit: String = "mm"

    constructor(position: Pair<String, Vector3D>) :
            this(
                    position.first,
                    position.second.x,
                    position.second.y,
                    position.second.z
            )
}


@GDMLName("materialref")
open class Material(ref: String) : GDMLElement(){
    open val ref = ref
}

enum class State(val state: String) {
    gas("gas")
}

@GDMLName("material")
class UserMaterial(name : String,
                   val state : State,
                   components: List<Pair<String, Double>>,
                   density : Double,
                   temperaure : Double = STP_Temperature,
                   pressure : Double = STP_Pressure
                   ): Material(name) {

    val name  = "Mat" + name
    @GDMLExclude
    override val ref: String = name

    init {
        with(children){
            add(T(temperaure/ kelvin))
            add(P(pressure/ pascal))
            add(D(density/(gram/ cm3)))
            for ((k,v) in components){
                add(Fraction(k,v))
            }
        }
            }

}
@GDMLName("T")
class T(val value: Double): GDMLElement(){
    val unit = "K"
}
@GDMLName("P")
class P(val value: Double): GDMLElement(){
    val unit = "pascal"
}
@GDMLName("D")
class D(val value: Double): GDMLElement(){
    val unit = "g/cm3"
}
class Fraction(val ref:String, val n: Double): GDMLElement()

////private val solidNames = mutableSetOf<String>()
//init {
//    if (name in solidNames){
//        throw Exception("Another solid have name ${name}!")
//    }
//    else{
//        solidNames.add(name)
//    }
//}
// Геометрические формы
open class Solid(val name: String) : GDMLElement() {
    var luint = "mm"
    var aunit = "radian"

}

/**
 * CSG box solid described by 3 dimensions of x, y, and z
 */
class Box(name: String, val x: Double, val y: Double, val z: Double) : Solid(name)

/**
 * CSG orb solid (simplified sphere with only rmax) is described by r  Outside radius
 */
class Orb(name: String, val r: Double) : Solid(name)

/**
 * CSG sphere or spherical shell segment solid described by
 * rmin        inner radius
 * rmax        outer radius
 * startphi    starting angle of the segment in radians(0 &lt;= phi &lt;= 2*PI)
 * deltaphi    delta angle of the segment in radians
 * starttheta  starting angle of the segment in radians(0 &lt;= theta &lt;= PI)
 * deltatheta  delta angle of the segment in radians
 */
class Sphere(name: String,
             val rmin :Double  = 0.0,
             val rmax :Double,
             val startphi  :Double = 0.0 ,
             val deltaphi :Double  =360.0,
             val starttheta :Double = 0.0,
             val deltatheta :Double = 180.0
) : Solid(name){
init {
    aunit = "degree"
}
}

/**
 * CSG tube or tube segement solid described by
 * rmin      Inner radius
 * rmax      Outer radius
 * z         length in z
 * startphi  The starting phi angle in radians, adjusted such that (startphi+deltaphi &lt;= 2PI, startphi &gt; -2PI)
 * deltaphi  Delta angle of the segment in radians
 */
class Tube(name: String,
           val z: Double,
           val rmin: Double = 0.0,
           val rmax: Double,
           val startphi: Double = 0.0,
           val deltaphi: Double
           ) :Solid(name)
class FIRST(val ref: String) : GDMLElement()

class SECOND(val ref: String) : GDMLElement()

fun GDMLDocument.saveGDML(pathname: String) {
    val file = File(pathname).outputStream().bufferedWriter()
    file.write(this.toString())
    file.close()
}

private operator fun Int.times(s: String): String {
    if (this == 0) {
        return ""
    }
    var newStr = ""
    for (i in 0..this - 1) {
        newStr += s
    }
    return newStr
}