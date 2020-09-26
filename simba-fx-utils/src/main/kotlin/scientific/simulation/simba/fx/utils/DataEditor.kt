package scientific.simulation.simba.fx.utils


import hep.dataforge.io.Envelope
import javafx.collections.FXCollections
import javafx.scene.Parent
import scientific.simba.fxutils.packer.EnvelopePacker
//import scientific.simulation.simba.physics.data.DataSpecification
import tornadofx.*
import java.io.File




sealed class EditorItem {
}

//data class TableItem(
//    val meta: DataSpecification,
//    val table: MutableTable<Value>
//) : EditorItem()



class MyController : Controller() {

    val openFiles = mutableListOf<File>().asObservable()

    val editorItems = mutableListOf<EditorItem>().asObservable()

    val envelopes = FXCollections.observableList(mutableListOf<Envelope>())

//    val dataSpecification = DataSpecification()
//    val table = Table<Value> {
//        val a = column<Value>("a")
//        val b = column<Value>("b")
//        row(a to 1, b to "b1")
//        row(a to 2, b to "b2")
//    }
}

//object EditorItemViewer {
//    fun drawTableItem(item: TableItem, tab: Tab) {
//        tab.apply {
//            vbox {
//                val table = item.table
//                tableview<Value> {
//
//                }
//            }
//        }
//    }
//}



object SimbaMetaViewFactory : MetaViewFactory{
    override fun resolve(envelope: Envelope): MetaView {
        TODO("Not yet implemented")
    }

}


object EmptyDataView : DataView(){
    override val root: Parent
        get() = hbox { label("No data") }
}

object SimbaDataViewFactory : DataViewFactory{
    override fun resolve(envelope: Envelope): DataView {
       return EmptyDataView 
    }

}

class MyView : View() {
    private val envelopeFileController: EnvelopeFileController by inject()
    private val envelopeController: EnvelopeController by inject()


    override val root = borderpane {
        top = menubar {
            menu("Create new ...") {
                item("Array").action {

                }
                item("Table")
                item("Tree")
            }
        }
        left = find(EnvelopeFileTree::class).root
        center = hbox {
            tabpane {

                envelopeController.envelopes.onChange {
                    when{
                        it.wasAdded() ->
                            for (item in it.addedSubList) {
                                tab(item.toString()){
                                    add(EnvelopeView(item, SimbaMetaViewFactory, SimbaDataViewFactory))
                                    //TODO(Tab function)
                                }
                            }
                    }

                }
            }
        }
    }
}

class MyApp : App(MyView::class)

fun main(args: Array<String>) {
    launch<EnvelopePacker>(args)
}