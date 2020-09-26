package scientific.simulation.simba.physics.data

import hep.dataforge.names.Name
import org.jopendocument.dom.ODDocument
import org.jopendocument.dom.spreadsheet.SpreadSheet

interface SimbaData<T>{
    val name : Name
    val data : T
}

class SimbaArray<T>(
    override val name: Name,
    override val data: Array<T>
) : SimbaData<Array<T>>{
}


interface SimbaDataContainer{
    fun <T>get(name: Name) : SimbaData<T>
}

abstract class ODFSimbaDataContainer(
    val document : ODDocument
) : SimbaDataContainer{


}

class ODTSimbaDataContainer(
 val spreadSheet: SpreadSheet
) : ODFSimbaDataContainer(spreadSheet){
    override fun <T> get(name: Name): SimbaData<T> {
//        val sheet = spreadSheet.getSheet(name.toString())
//        sheet.

        TODO("Not yet implemented")
    }
}




