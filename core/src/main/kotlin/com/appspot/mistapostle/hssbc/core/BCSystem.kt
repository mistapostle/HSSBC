package com.appspot.mistapostle.hssbc.core

import com.appspot.mistapostle.hssbc.core.model.Block
import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import com.google.common.io.BaseEncoding
import java.nio.charset.Charset

/**
 * Created by mistapostle on 17/7/30.
 * BC System is the base of the while system, one should config the system as first thing to do
 */

object BCSystem{
    private var hashFunction: HashFunction = Hashing.sha512()
    private var powFuncton: (Block)-> Boolean = { block -> val h = Math.abs(  hashAsLong(block.header.pow + block.header.version + block.id)) %2 ;  println("h=$h") ; h ==0L  }

    fun config( options : Map<String , String>){
        //TODO: config able hashing function etc
//        val hashMethod = options.get("hachMethod" )
//        if ( hashMethod !=null) {
//            if (hashMethod !in arrayOf("sha512", "sha256")) {
//                throw IllegalArgumentException("Invalid hash method : ${hashMethod}")
//            }
//            hashFunction = Hashing.sha512()
//        }
    }
    fun hash(value : String ) : String {
        val hc = hashFunction.newHasher().putString(value, Charset.forName("UTF8")).hash()
        return BaseEncoding.base64().encode(hc.asBytes())

    }
    //TODO: it seems this always return 0 or 1 
    fun hashAsLong(value : String ) : Long {
        val hc = hashFunction.newHasher().putString(value, Charset.forName("UTF8")).hash()
        return hc.asLong()

    }
    fun verifyPow(block:Block) : Boolean{
        return powFuncton(block)
    }
}
