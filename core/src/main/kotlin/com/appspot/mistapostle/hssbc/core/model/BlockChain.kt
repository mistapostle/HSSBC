package com.appspot.mistapostle.hssbc.core.model

import com.appspot.mistapostle.hssbc.core.BCSystem

/**
 * Created by mistapostle on 17/7/30.
 * Base Block chain models
 */

data class Transaction(   val content:String){
    val id: String = BCSystem.hash(content)

    override fun toString(): String {
        return "Transaction(id=$id,content=$content)"
    }
}

data class Block( val previousBlockId: String, val transactions : List<Transaction> ){
    val id: String = BCSystem.hash(transactions.joinToString(","){ it.content})

    override fun toString(): String {
        return "Block(id=$id,previousBlockId=$previousBlockId,transactions:$transactions)"
    }
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



