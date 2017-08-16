package com.appspot.mistapostle.hssbc.core.model

import com.appspot.mistapostle.hssbc.core.BCSystem
import com.google.common.collect.ImmutableList

/**
 * Created by mistapostle on 17/7/30.
 * Base Block chain models
 */

data class UnsignedTransation(val version:String,val signerPublicKeys:ImmutableList<String>,val content:String){
    fun toTransation(signature: String) : Transaction{
        return Transaction(TransactionHeader(this.version,signerPublicKeys,signature ),content)
    }
}

data class TransactionHeader(val version: String , val signerPublicKeys:ImmutableList<String>, val signature:String){

}
data class Transaction(val header:TransactionHeader ,   val content:String){
    val id: String = BCSystem.hash("${header} ${content}")

//    override fun toString(): String {
//        return "Transaction(id=$id,content=$content)"
//    }
}

data class BlockHeader(val version : String , val pow : String ){

}
data class Block( val previousBlockId: String, val header: BlockHeader, val transactions : ImmutableList<Transaction> ){
    private val transactionIdsStr = transactions.joinToString(",") { it.id }
    val id: String = BCSystem.hash("${previousBlockId} ${header.version} ${header.pow} $transactionIdsStr")

//    override fun toString(): String {
//        return "Block(id=$id,previousBlockId=$previousBlockId,header=$header,transactions:$transactions)"
//    }
}

data class BlockChain(var lastBlock: Block,  var deep: Int ){
    fun addBlock(  block:Block){
        if(block.previousBlockId != lastBlock.id){
            throw   IllegalArgumentException("block.previousBlockId ( ${ block.previousBlockId} ) !=  lastBlock.id ${ lastBlock.id }")
        }
        this.lastBlock = block
        this.deep++
    }
}



