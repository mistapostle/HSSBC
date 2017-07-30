package com.appspot.mistapostle.hssbc.core.model


import org.junit.Test as test
import org.junit.Assert.*

class TestBaseModel {
    @test
    fun newChain() {
        val tr = Transaction("root transation  ")
        val t1 = Transaction("transation 1 ")
        val t2= Transaction("transation 2 ")

        val br = Block("", listOf(tr))
        val b1 = Block(br.id,listOf(t1,t2))

        val bc = BlockChain(br,1)
        bc.addBlock(b1)

        println("bc : \n ${bc}")
        println("bc.preblock : \n ${br}")


        try{
            bc.addBlock(br)
            fail()
        }catch (e: IllegalArgumentException){
            assertTrue(e.message!!.contains(Regex("block.previousBlockId.*!=\\s*lastBlock.id")))
        }
     }
}