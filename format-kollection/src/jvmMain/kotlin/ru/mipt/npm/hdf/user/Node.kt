package ru.mipt.npm.hdf.user


interface Node {
    val parent: Group?
}



class Group internal constructor(override val parent: Group?, val name: String, val title:String = ""): Node{

}

abstract class Leaf: Node

//class Array: Leaf()
//class Table: Leaf()

