package com.appspot.mistapostle.hssbc.core.model


import com.appspot.mistapostle.hssbc.core.BCSystem
import org.junit.Test as test
import org.junit.Assert.*

class TestBaseModel {
    @test
    fun newChain() {
        val tr = Transaction("root transation  ")
        val t1 = Transaction("transation 1 ")
        val t2= Transaction("transation 2 ")

        val br = Block("", BlockHeader("POW EVAN 1.0.0", "1"), listOf(tr))
        val b1 = Block(br.id,BlockHeader("POW EVAN 1.0.0", "1"),listOf(t1,t2))

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

    @test
    fun testPow(){
        val tr = Transaction("root transation  ")
        val t1 = Transaction("transation 1 ")
        val t2= Transaction("transation 2 ")

        fun mine(preId : String ,trs : List<Transaction>) : Block?{
            var i = 0
            var block : Block? = null

            while( i<30) {
                block = Block(preId, BlockHeader("POW EVAN 1.0.0", "${i++}"), trs)
                if(BCSystem.verifyPow(block)){
                    break
                }
            }
            return block

        }
        val rootBlock = mine("",listOf(tr))
        println("rootBlock: $rootBlock")
        if(rootBlock!=null) {

            val block1 = mine(rootBlock.id, listOf(t1, t2))
            println("block1: $block1")
        }
    }
}