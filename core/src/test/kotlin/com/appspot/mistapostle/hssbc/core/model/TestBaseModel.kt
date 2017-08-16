package com.appspot.mistapostle.hssbc.core.model


import com.appspot.mistapostle.hssbc.core.BCSystem
import com.google.common.collect.ImmutableList
import com.google.common.io.BaseEncoding
import org.junit.Test as test
import org.junit.Assert.*
import org.junit.BeforeClass
import sun.misc.BASE64Encoder
import java.security.Key
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.KeyPair




class TestBaseModel {
    private var signerPublicKeys: ImmutableList<String>
    private var signerPrivateKeys:ImmutableList<String>
    init {

        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(512, SecureRandom())
        val keyPair = keyGen.generateKeyPair()
        val privateKey = BaseEncoding.base64().encode(   keyPair.private.encoded)
        this.signerPrivateKeys = ImmutableList.of(privateKey)
        val pubKey = BaseEncoding.base64().encode(   keyPair.public.encoded)
        this.signerPublicKeys = ImmutableList.of(pubKey)

    }

    @test
    fun newChain() {
        val tr = BCSystem.sign(  UnsignedTransation("1.0.0", this.signerPublicKeys, "root transation  "),this.signerPrivateKeys)
        val t1 =BCSystem.sign(   UnsignedTransation("1.0.0", this.signerPublicKeys, "transation 1 "),this.signerPrivateKeys)
        val t2= BCSystem.sign(  UnsignedTransation("1.0.0", this.signerPublicKeys, "transation 2 "),this.signerPrivateKeys)

        val br = Block("", BlockHeader("POW EVAN 1.0.0", "1"), ImmutableList.of(tr))
        val b1 = Block(br.id,BlockHeader("POW EVAN 1.0.0", "1"),ImmutableList.of(t1,t2))

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
        val tr = BCSystem.sign(  UnsignedTransation("1.0.0", this.signerPublicKeys, "root transation  "),this.signerPrivateKeys)
        val t1 = BCSystem.sign( UnsignedTransation("1.0.0", this.signerPublicKeys, "transation 1 "),this.signerPrivateKeys)
        val t2= BCSystem.sign( UnsignedTransation("1.0.0", this.signerPublicKeys, "transation 2 "),this.signerPrivateKeys)

        fun mine(preId : String ,trs : ImmutableList<Transaction>) : Block?{
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
        val rootBlock = mine("", ImmutableList.of(tr))
        println("rootBlock: $rootBlock")
        if(rootBlock!=null) {

            val block1 = mine(rootBlock.id, ImmutableList.of(t1, t2))
            println("block1: $block1")
        }
    }
}