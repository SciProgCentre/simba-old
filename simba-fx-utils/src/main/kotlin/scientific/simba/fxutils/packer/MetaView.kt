package scientific.simba.fxutils.packer

import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaItem
import hep.dataforge.meta.get
import hep.dataforge.names.Name
import hep.dataforge.names.NameToken
import hep.dataforge.names.asName
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.StringProperty
import javafx.scene.Parent
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeSortMode
import javafx.scene.control.TreeTableView
import tornadofx.*


//internal sealed class ModelItem{
//    abstract val name : StringProperty
//    abstract val value : StringProperty
//}
//
//internal class ModelNodeItem(val meta: Meta): ModelItem(){
//    override val name =  SimpleStringProperty(item.)
//}

class MetaView(val meta : Meta, val editable: ReadOnlyBooleanProperty): Fragment(){

    override val root: Parent = vbox {
        treetableview<Pair<NameToken, MetaItem<*>>> {
            root = TreeItem("root".asName().get(0) to meta.get(Name.EMPTY)!!)
            isShowRoot = false
            root.isExpanded = true
            sortMode = TreeSortMode.ALL_DESCENDANTS
            columnResizePolicy = TreeTableView.CONSTRAINED_RESIZE_POLICY
            editableProperty().bind(editable)
            column<Pair<NameToken, MetaItem<*>>, String>("Name"){
                val value = it.value.value
                val name = value.first
                ReadOnlyStringWrapper(name.body)
            }
            column<Pair<NameToken, MetaItem<*>>, String>("Value"){
                val value = it.value.value
                val item = value.second
                when{
                    item is MetaItem.NodeItem<*> -> ReadOnlyStringWrapper("")
                    item is MetaItem.ValueItem -> ReadOnlyStringWrapper(item.value.toString())
                    else -> kotlin.error("Bad meta item")
                }
            }
            populate {
                parent ->
                val item = parent.value.second
                when{
                    item is MetaItem.NodeItem<*> -> item.node.items.map { it.key to it. value }.asIterable()
                    item is MetaItem.ValueItem -> null
                    else -> kotlin.error("Bad meta item")
                }
            }
        }
    }
}