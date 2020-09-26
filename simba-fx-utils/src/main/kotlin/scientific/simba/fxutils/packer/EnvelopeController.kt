package scientific.simba.fxutils.packer

import hep.dataforge.context.Global
import hep.dataforge.io.Envelope
import hep.dataforge.io.io
import hep.dataforge.io.readEnvelopeFile
import hep.dataforge.meta.Meta
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicLong

class EnvelopeItem(
    name: String,
    val envelope: Envelope?,
    path: Path,
    editable: Boolean = false
) {
    val name = SimpleStringProperty(name)
    val path: SimpleObjectProperty<Path> = SimpleObjectProperty(path)
    val editable = SimpleBooleanProperty(editable)
}

class EnvelopeModel : ItemViewModel<EnvelopeItem>() {
    val name = bind(EnvelopeItem::name)
    val file = bind(EnvelopeItem::path)
}

class EnvelopeController : Controller() {
    val envelopesItems =
        FXCollections.observableList(mutableListOf<EnvelopeItem>())
    val counter = AtomicLong(0)

    init {
//        envelopesItems.onChange {
//            print(it)
//        }
//
//        val item1 = EnvelopeItem(
//            name = "Item 1",
//            envelope = Envelope {
//                meta{
//                    "a" put Meta {
//                        "b" put 1
//                        "c" put "aaa"
//                    }
//                    "d" put listOf(1, 2, 3)
//                }
//
//            }
//        )
//
//        val item2 = EnvelopeItem(
//            name = "Item 2",
//            envelope = Envelope {
//                meta{
//                    "a" put Meta {
//                        "b" put 1
//                        "c" put "aaa"
//                    }
//                    "d" put listOf(1, 2, 3)
//                }
//            }
//        )
//
//        envelopesItems.apply {
//            add(item1)
//            add(item2)
//        }


    }

    fun openEnvelope(path: Path, editable: Boolean = false): EnvelopeItem {
        val envelope = Global.io.readEnvelopeFile(path)
        return EnvelopeItem(
                    name = path.fileName.toString(),
                    envelope = envelope,
                    path = path,
                    editable = editable
                )
    }

    fun createNewEnvelope() {
//        val envelope = Envelope {}
//        envelopesItems.add(
//            EnvelopeItem(
//                name = "Untitled ${counter.getAndIncrement()}",
//                envelope = envelope,
//                editable = true
//            )
//        )

    }
}