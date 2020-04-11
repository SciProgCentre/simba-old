package scientific.simulation.simba.fx.utils

import hep.dataforge.tables.MutableTable
import hep.dataforge.tables.Table
import hep.dataforge.tables.row
import hep.dataforge.values.Value
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Tab
import javafx.scene.control.TreeItem
import javafx.stage.DirectoryChooser
import scientific.simulation.simba.physics.data.DataSpecification
import tornadofx.*
import java.io.File


sealed class EditorItem{
}

data class TableItem(
    val meta : DataSpecification,
    val table: MutableTable<Value>
) : EditorItem()

class MyController : Controller() {
    val rootDir = SimpleObjectProperty<File>(File("."))
    val openFiles  = mutableListOf<File>().asObservable()

    val editorItems = mutableListOf<EditorItem>().asObservable()

//    val dataSpecification = DataSpecification()
//    val table = Table<Value> {
//        val a = column<Value>("a")
//        val b = column<Value>("b")
//        row(a to 1, b to "b1")
//        row(a to 2, b to "b2")
//    }
}

object EditorItemViewer{
    fun drawTableItem(item: TableItem, tab: Tab){
        tab.apply {
            vbox {
                tableview {

                }
            }
        }
    }
}


class MyView : View() {
    private val myController: MyController by inject()
    private val dirChooser = DirectoryChooser().apply {
        title = "Choose a root directory with simba data"
    }
    override val root = borderpane {
        top = menubar{
            menu ("Create new ..."){
                item("Array").action {

                }
                item("Table")
                item("Tree")
            }
        }
        left = vbox {
            button("Change root directory") {
                action {
                    val file = dirChooser.showDialog(this@MyView.primaryStage)
                    if (file != null) {
                        myController.rootDir.set(file)
                    }
                }
            }
            treeview<File> {
                root = TreeItem(myController.rootDir.get())
                val updateFileTree: (TreeItem<File>) -> Iterable<File>? = {
                    it.value.listFiles { file ->
                        (file.isDirectory || file.extension == "df") &&
                                !file.isHidden
                    }?.toList()
                }

                myController.rootDir.onChange {
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
        center = hbox {
            tabpane {
                myController.editorItems.onChange {
                    if (it.wasAdded()){
                     for (item in it.addedSubList){
                         tab(it.toString()) {
                             when (item){
                                 is TableItem -> this.apply()
                             }
                         }
                     }
                    }
                }


//                myController.openFiles.onChange {
//                    val files = it.addedSubList
//                    if (it.wasAdded()){
//                        files.map {
//                            tab(it.name){
//
//                            }
//                        }
//                    }
//                }

            }
        }
    }
}

class MyApp : App(MyView::class)

fun main(args: Array<String>) {
    launch<MyApp>(args)
}