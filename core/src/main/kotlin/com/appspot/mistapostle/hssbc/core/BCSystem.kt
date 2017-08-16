package com.appspot.mistapostle.hssbc.core

import com.appspot.mistapostle.hssbc.core.model.Block
import com.appspot.mistapostle.hssbc.core.model.Transaction
import com.appspot.mistapostle.hssbc.core.model.TransactionHeader
import com.appspot.mistapostle.hssbc.core.model.UnsignedTransation
import com.google.common.base.Preconditions
import com.google.common.base.Preconditions.checkArgument
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import com.google.common.io.BaseEncoding
import java.nio.charset.Charset
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import com.oracle.util.Checksums.update





/**
 * Created by mistapostle on 17/7/30.
 * BC System is the base of the while system, one should config the system as first thing to statup the system
 */


private fun simpleSign(transation: UnsignedTransation, signerPrivateKeys:ImmutableList<String>) : Transaction{
    checkArgument(transation.signerPublicKeys.size == signerPrivateKeys.size ,
            "size of signerPublicKeys and size of signerPrivateKeys are not matched : " +
                    "${transation.signerPublicKeys.size} != ${ signerPrivateKeys.size}")

    val sb =  StringBuilder()

    val publicKeysStr = transation.signerPublicKeys.joinToString(",")
    val signContent = "${transation.version} $publicKeysStr ${transation.content}".toByteArray(Charsets.UTF_8)

    for( privateKey    in signerPrivateKeys){
        val signature  = Signature.getInstance("SHA256withRSA")
        val pkcs8EncodedBytes = BaseEncoding.base64().decode(privateKey)
        val keySpec = PKCS8EncodedKeySpec(pkcs8EncodedBytes)
        val kf = KeyFactory.getInstance("RSA")
        val privKey = kf.generatePrivate(keySpec)
        signature.initSign(privKey, SecureRandom())
        signature.update(signContent)
        val sigBytes = signature.sign( )
        sb.append(BaseEncoding.base64().encode(sigBytes))
        sb.append("\r\n")

        //signature.initVerify(keyPair.getPublic())
        //signature.update(message)
        // System.out.println(signature.verify(sigBytes))
    }
    return transation.toTransation(sb.toString())
}
object BCSystem{
    private var hashFunction: HashFunction = Hashing.sha512()
    private var powFuncton: (Block)-> Boolean = { block -> val h = Math.abs(  hashAsLong(block.header.pow + block.header.version + block.id)) %2 ;  println("h=$h") ; h ==0L  }
    private var signatureFunction : (transation: UnsignedTransation, signerPrivateKeys:ImmutableList<String>) -> Transaction = ::simpleSign // { transation, signerPrivateKeys -> simpleSign(transation,signerPrivateKeys) }

    fun config( options : ImmutableMap<String, String>){
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
    fun sign(transation: UnsignedTransation, signerPrivateKeys:ImmutableList<String>): Transaction {
        return  signatureFunction(transation,signerPrivateKeys)
    }


}
