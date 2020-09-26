package scientific.simba.fxutils.packer

import javafx.scene.control.Alert
import javafx.stage.FileChooser
import tornadofx.*

class TopView: View(){
    private val envelopeController: EnvelopeController by inject()
    val fileChooser by lazy {
        FileChooser().apply {
            this.extensionFilters.addAll(
                FileChooser.ExtensionFilter("Dataforge files", "*.df")
            )
        }
    }
    override val root = menubar {
        menu("File") {
            item("New envelope").action {
                envelopeController.createNewEnvelope()
            }
            item("View envelope").action {
                openFile("View envelope")
            }
            item("Edit envelope").action {
                openFile("Edit envelope", true)
            }
            item("Save")
            item("Save as ...")
        }
    }

    fun openFile(header: String, editable : Boolean = false){
//        fileChooser.title = header
//        val file = fileChooser.showOpenDialog(primaryStage)
//        if (file != null){
//            if (!envelopeController.openEnvelope(file.toPath(), editable)){
//                alert(Alert.AlertType.ERROR, "Can't parse envelope file ${file}")
//            }
//        }
    }
}