package com.appspot.mistapostle.hssbc.core

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
}
