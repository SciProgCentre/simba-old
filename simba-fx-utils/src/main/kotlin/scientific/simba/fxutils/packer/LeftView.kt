package scientific.simba.fxutils.packer

import hep.dataforge.meta.Meta
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import javafx.scene.control.cell.TextFieldListCell
import javafx.util.StringConverter
import kotlinx.io.Binary
import tornadofx.*
import java.io.File
import java.nio.file.Path


class LeftView : View("Left screen") {
    private val envelopeController: EnvelopeController by inject()
    val model: EnvelopeModel by inject()



    override val root = vbox {




//        treeview<Any?> {
//            isShowRoot = false
//            root = rootItem
//
//            cellFormat {
//                text = when(it){
//                    is File -> it.name
//                    is EnvelopeItem -> it.path.get().fileName.toString()
//                    is Meta -> "meta"
//                    is Binary -> "data"
//                    else -> ""
//                }
//            }
//
//            populate {
//                val item = it.value
//                when (item) {
//                    is File -> when {
//                        item.isDirectory -> item.listFiles { file ->
//                            (file.isDirectory || file.extension == "df") && (!file.isHidden)
//
//                        }.map {
//                            if (it.isDirectory){
//                                it
//                            } else{
//                                envelopeController.openEnvelope(it.toPath())}
//                            }
//                        else -> null
//                    }
//                    is EnvelopeItem -> listOf(item.envelope?.meta, item.envelope?.data)
//
//                    else -> null
//                }
//            }
//        }

//        listview(envelopeController.envelopesItems) {
//            title = "Open envelopes"
//            cellFormat {
//                text = it.name.get()
//            }
//            bindSelected(model)
//            selectionModel.selectionMode = SelectionMode.SINGLE
//            isEditable = true
//            setCellFactory {
//                val cell =
//                    TextFieldListCell<EnvelopeItem>()
//                cell.converter = object : StringConverter<EnvelopeItem>() {
//                    override fun toString(item: EnvelopeItem?) = item?.name?.get() ?: ""
//                    override fun fromString(string: String?) = cell.item.apply { name.set(string) }
//                }
//                cell
//            }
//            setOnEditCommit {
//                model.commit()
//            }
//
//        }
    }
}