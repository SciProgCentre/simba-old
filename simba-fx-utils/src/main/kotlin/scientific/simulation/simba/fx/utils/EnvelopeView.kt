package scientific.simulation.simba.fx.utils

import hep.dataforge.context.Global
import hep.dataforge.io.Envelope
import hep.dataforge.io.io
import hep.dataforge.io.readEnvelopeFile
import hep.dataforge.meta.Scheme
import hep.dataforge.meta.descriptors.NodeDescriptor
import hep.dataforge.meta.descriptors.ValueDescriptor
import hep.dataforge.meta.setProperty
import hep.dataforge.meta.string
import hep.dataforge.names.asName
import hep.dataforge.values.asValue
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.TreeItem
import javafx.stage.DirectoryChooser
import tornadofx.*
import java.io.File

interface MetaViewFactory{
    fun resolve(envelope: Envelope) : MetaView
}

interface DataViewFactory{
    fun resolve(envelope: Envelope) : DataView
}

abstract class MetaView : View(){

}

abstract class DataView : View(){

}

class EnvelopeView(envelope : Envelope, metaViewFactory: MetaViewFactory, dataViewFactory: DataViewFactory) : Fragment(){

    val metaView = metaViewFactory.resolve(envelope)
    val dataView = dataViewFactory.resolve(envelope)

    override val root: Parent = vbox {
        add(metaView)
        add(dataView)
    }
}


open class EnvelopeController : Controller(){
    val envelopes = FXCollections.observableList(mutableListOf<Envelope>())
}

open class EnvelopeFileController : Controller(){
    private val envelopeController: EnvelopeController by inject()

    val rootDir = SimpleObjectProperty<File>(File("."))
    val files = HashMap<Envelope, File>()

    open fun checkEnvelopeFile(file: File) : Boolean{
        return (file.isDirectory || file.extension == "df") &&
                !file.isHidden
    }


}

open class EnvelopeFileTree() : View(){
    val envelopeFileController: EnvelopeFileController by inject()
    val dirChooser = DirectoryChooser().apply  {
        title = "Choose a root directory with envelope data"
    }
    override val root: Parent = vbox {
        button("Change root directory") {
            action {
                val file = dirChooser.showDialog(this@EnvelopeFileTree.primaryStage)
                if (file != null) {
                    envelopeFileController.rootDir.set(file)
                }
            }
        }
        treeview<File> {
            root = TreeItem(envelopeFileController.rootDir.get())
            val updateFileTree: (TreeItem<File>) -> Iterable<File>? = {
                it.value.listFiles(envelopeFileController::checkEnvelopeFile)?.toList()
            }

            envelopeFileController.rootDir.onChange {
                root = TreeItem(it)
                populate(childFactory = updateFileTree)
            }
            cellFormat {
                text = it.name
            }
            populate(childFactory = updateFileTree)
//                this.
        }
    }
}

fun Scheme.intoForm(form : Form){
    form.apply {
        this@intoForm.descriptor?.items?.map {
            val (name, item) = it
            when (item){
                is ValueDescriptor ->
                    field(name){
                        val configProperty = this@intoForm.getProperty(name.asName())
                        textfield(configProperty.string) {
                          textProperty().addListener {
                                  obs, old, new ->
                              this@intoForm.setProperty(name, new.asValue())
                          }
                        }
                    }
                is NodeDescriptor -> TODO()
            }
        }
    }

}