package scientific.simba.fxutils.packer

import hep.dataforge.io.read
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.control.SelectionMode
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.util.StringConverter
import kotlinx.io.text.readUtf8String
import tornadofx.*
import java.nio.file.Path

object DataFileTools{
//    fun peekFormat(path: Path) : String{
//        return path.fileName.
//    }
}

internal class FileItem(
    name: String,
    path: Path
) {
    val name = SimpleStringProperty(name)
    val path: SimpleObjectProperty<Path?> = SimpleObjectProperty(path)
}

internal class FileModel : ItemViewModel<FileItem>() {
    val name = bind(FileItem::name)
    val path = bind(FileItem::path)
}

class DataView : Fragment() {
    private val fileModel: FileModel by inject()
    private val dragFiles = mutableListOf<FileItem>().asObservable()

    override val root = hbox {
        vbox {

        }
        vbox {
            hbox {
                label("Drag a file to me.") {
                    style {
                        borderColor += box(Color.BLACK)
                    }
                    font = Font(28.0)
                    minHeight = 100.0
                    minWidth = 100.0
                    var success = false

                    setOnDragOver {
                        if ((it.gestureSource != this) && it.dragboard.hasFiles()) {
                            it.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
                        }
                        it.consume()
                    }

                    setOnDragDropped {
                        val db = it.dragboard
                        if (db.hasFiles()) {
                            dragFiles.addAll(db.files.map { FileItem(it.name, it.toPath()) })
                            success = true
                        }
                        it.isDropCompleted = success
                        it.consume()
                    }
                }

                listview(dragFiles) {
                    cellFormat {
                        text = it.name.get()
                        text(it.name)
                    }
                    selectionModel.selectionMode = SelectionMode.SINGLE
                    bindSelected(fileModel)
                    isEditable = true
                    setCellFactory {
                        val cell =
                            TextFieldListCell<FileItem>()
                        cell.converter = object : StringConverter<FileItem>() {
                            override fun toString(item: FileItem?) = item?.name?.get() ?: ""
                            override fun fromString(string: String?) = cell.item.apply { name.set(string) }
                        }
                        cell
                    }
                    setOnEditCommit {
                        fileModel.commit()
                    }
                    contextmenu {
                        item("View data").action {
                            class FileTextView(path: Path) : Fragment() {
                                override val root = textarea(null) {
                                    text = path.read {
                                        readUtf8String()
                                    }
                                }
                            }
                            val path = selectedItem?.path?.get()
                            if (path != null) {
                                FileTextView(path).openWindow()
                            }
                        }



                        item("Edit data").action {

                        }
                        item("Rename").action {
                            this.hide()
                            val indx = dragFiles.indexOf(selectedItem)
                            this@listview.edit(indx)
                        }
                        item("Delete item").action {
                            selectedItem?.apply { dragFiles.remove(this) }
                        }
                    }
                }
            }

        }


    }
}



class EnvelopeView(envelopeItem: EnvelopeItem) : Fragment() {


    override val root: Parent = vbox {

//        add(MetaView(envelopeItem.envelope.meta, envelopeItem.editable))
//        add(DataView())

    }
}