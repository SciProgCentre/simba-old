package scientific.simba.fxutils.packer

import javafx.event.EventHandler
import javafx.scene.control.Control
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import tornadofx.*

class CenterView : View(){
    private val envelopeController: EnvelopeController by inject()

    private val tabpanes = mutableListOf<TabPane>().asObservable()

    override val root =
            hbox {
//                AnchorPane.setTopAnchor(this, 0.0)
//                AnchorPane.setBottomAnchor(this, 0.0)
//                AnchorPane.setLeftAnchor(this, 0.0)
//                AnchorPane.setRightAnchor(this, 0.0)
                tabpanes.onChange {
                    when(it.next()){
                        it.wasAdded() -> it.addedSubList.forEach {
                            add(it)
                        }
                    }
                }
        }



    init {
        envelopeController.envelopesItems.onChange {
            when (it.next()){
                it.wasAdded() -> createNewTabs(it.addedSubList)
            }
        }

        if (envelopeController.envelopesItems.isNotEmpty()){
            createNewTabs(envelopeController.envelopesItems)
        }
    }

    fun createNewTabs(items: List<out EnvelopeItem>) {
        if (tabpanes.isEmpty()){
            val tabPane = TabPane().apply {
//                AnchorPane.setTopAnchor(this, 0.0)
//                AnchorPane.setBottomAnchor(this, 0.0)
//                AnchorPane.setLeftAnchor(this, 0.0)
//                AnchorPane.setRightAnchor(this, 0.0)
            }
            tabpanes.add(tabPane)
        }
        val tabPane = tabpanes.first()
        tabPane.apply {
            items.forEach {
                item ->
                val view = EnvelopeView(item)
                tab(item.name.get()){
                    this.add(view)
                    titleProperty.bind(item.name)
                    onClosed = EventHandler { envelopeController.envelopesItems.remove(item) }
                }
            }

        }
    }
}