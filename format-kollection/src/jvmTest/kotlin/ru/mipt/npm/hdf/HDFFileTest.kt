package ru.mipt.npm.hdf

import mu.KotlinLogging
import kotlin.test.Test

class HDFFileTest {
    @Test
    fun hdfTest(){
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
        val HDFFile = HDFFile.open("/home/zelenyy/npm/simulations/simba.kt/format-kollection/example/NIST_XCOM.hdf5")
        val rootGroup = HDFFile.superblock.rootGroupSymbolTableEntry
        println(rootGroup?.cacheType)
        println(rootGroup?.objectHeaderAddress)
        println( rootGroup?.cacheSpace?.array()?.map { it.toChar() }?.joinToString(""))
    }

    @Test
    fun testFirst(){
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
        val HDFFile = HDFFile.open("/home/zelenyy/npm/simulations/simba.kt/format-kollection/example/test_1.hdf5")
        val rootGroup = HDFFile.superblock.rootGroupSymbolTableEntry
        println(rootGroup?.cacheType)
        println(rootGroup?.objectHeaderAddress)
    }
}