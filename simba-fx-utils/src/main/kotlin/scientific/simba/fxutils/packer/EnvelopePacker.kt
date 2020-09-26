package scientific.simba.fxutils.packer

import tornadofx.*




class MainView : View() {
//    private val envelopeController: EnvelopeController by inject()


    init {
        primaryStage.minWidthProperty().set(640.0)
        primaryStage.minHeightProperty().set(480.0)
    }

    private val leftScreen: LeftView by inject()
//    private val centerScreen: CenterView by inject()
//    private val topView: TopView by inject()

    override val root = borderpane {
        left = leftScreen.root
//        center = centerScreen.root
//        top = topView.root

    }
}

class EnvelopePacker : App(MainView::class)

fun main(args: Array<String>) {
    launch<EnvelopePacker>(args)
}