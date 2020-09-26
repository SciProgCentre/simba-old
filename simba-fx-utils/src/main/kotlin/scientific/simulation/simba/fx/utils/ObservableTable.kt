package scientific.simulation.simba.fx.utils

import hep.dataforge.meta.Meta
import hep.dataforge.tables.*
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import tornadofx.*
import kotlin.reflect.KClass


interface MutableColumnTable<C : Any> : Table<C> {

    /**
     *  Zero element for fill empty place in columns
     */
    val zero: C

    /**
     * Adds the specified column to the table.
     *
     * @return `true` if the column has been added, `false` if the table does not support duplicates
     * and the column is already contained in the collection.
     */
    fun addColumn(column: Column<C>): Boolean

    /**
     * Adds all of the columns of the specified collection to this table.
     *
     * @return `true` if any of the specified columns was added to the table, `false` if the table was not modified.
     */
    fun addColumns(columns: Collection<Column<C>>): Boolean


}

interface MutableRowTable<C : Any> : Table<C> {

    /**
     *  Empty row
     */
    val emptyRow : Row<C>

    /**
     * Adds the specified row to the table.
     *
     * @return `true` if the row has been added, `false` if the table does not add row.
     */
    fun addRow(row: Row<C>): Boolean

    /**
     * Adds all of the rows of the specified collection to this table.
     *
     * @return `true` if any of the specified rows was added to the table, `false` if the table was not modified.
     */
    fun addRows(rows: Collection<Row<C>>): Boolean
}

//class ObservableRowTable<C : Any>(override val emptyRow: Row<C>) : MutableRowTable<C>{
//    private  val _rows = mutableListOf<Row<C>>().asObservable()
//    val rows :  List<Row<C>>
//}

@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.cast(value: Any?): T? {
    return when {
        value == null -> null
        !isInstance(value) -> kotlin.error("Expected type is $this, but found ${value::class}")
        else -> value as T
    }
}

data class ColumnsListener<C: Any>(
    val owner: Any? = null,
    val action: (column: Column<C>, ListChangeListener.Change<out C>) -> Unit
)

data class RowsListener<C: Any>(
    val owner: Any? = null,
    val action: (row: Row<C>, ListChangeListener.Change<out C>) -> Unit
)

interface MutableColumn<C : Any> : Column<C> {
    fun add(element : C): Boolean
    fun addAll(elements : Collection<C>): Boolean
}

class ObservableColumn<T : Any>(
    override val name: String,
    private val data: ObservableList<T>,
    override val type: KClass<out T>,
    override val meta: Meta = Meta.EMPTY
) : MutableColumn<T>{
    override val size: Int get() = data.size

    override fun get(index: Int): T? = data[index]

    override fun add(element: T): Boolean = data.add(element)

    override fun addAll(elements: Collection<T>): Boolean = data.addAll(elements)

    fun onChange(op: (ListChangeListener.Change<out T>) -> Unit) = data.onChange(op)
}

class ObservableColumnTable<C : Any>(override val zero: C) : MutableRowTable<C>, MutableColumnTable<C> {



    override val emptyRow: Row<C>
        get() = MapRow<C>(
            columns.map { it.name to zero }.toMap()
        )

    private val _columns = mutableListOf<Column<C>>().asObservable()
    override val columns: List<Column<C>> get() = _columns

    val size = _columns.sizeProperty

    override val rows: List<Row<C>> get() = (0..size.value).map{ indx ->
        MapRow(_columns.map { it.name to it.get(indx) }.toMap())
    }

    fun onChangeColumns(op: (ListChangeListener.Change<out Column<C>>) -> Unit) = _columns.onChange(op)

    override fun <T : C> getValue(row: Int, column: String, type: KClass<out T>): T? {
        val value = columns[column]?.get(row)
        return type.cast(value)
    }

    private val mutableColumns : List<MutableColumn<C>>?
    get() {
        val temp = _columns.map {it as? MutableColumn<C>}.filterNotNull()
        if (temp.size != _columns.size){
            return null
        }
        return temp
        }

    override fun addRow(row: Row<C>): Boolean {
        mutableColumns?.map {
            val item = row.get(it)
            if (item != null){
                it.add(item)
            } else{
                it.add(zero)
            }
        } ?: return false
        return true
    }

    override fun addRows(rows: Collection<Row<C>>): Boolean {
        mutableColumns?.map {
            for (row in rows){
                val item = row.get(it)
                if (item != null){
                    it.add(item)
                } else{
                    it.add(zero)
                }
            }
        } ?: return false
        return true
    }

    override fun addColumn(column: Column<C>)= _columns.add(column)

    override fun addColumns(columns: Collection<Column<C>>) = _columns.addAll(columns)
}