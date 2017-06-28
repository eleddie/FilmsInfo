package com.proyectosyntax.codingchallenge

import java.io.*
import kotlin.experimental.and

object ObjectSerializer {
    @Throws(IOException::class)
    fun serialize(obj: Serializable): String {
        try {
            val serialObj = ByteArrayOutputStream()
            val objStream = ObjectOutputStream(serialObj)
            objStream.writeObject(obj)
            objStream.close()
            return encodeBytes(serialObj.toByteArray())
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    fun deserialize(str: String): Any? {
        if (str.isEmpty()) return null
        try {
            val serialObj = ByteArrayInputStream(decodeBytes(str))
            val objStream = ObjectInputStream(serialObj)
            return objStream.readObject()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun encodeBytes(bytes: ByteArray): String {
        val strBuf = StringBuffer()
        for (i in bytes.indices) {
            strBuf.append(((bytes[i].toInt() shr 4 and 0xF) + 'a'.toInt()).toChar())
            strBuf.append(((bytes[i] and 0xF) + 'a'.toInt()).toChar())
        }
        return strBuf.toString()
    }

    private fun decodeBytes(str: String): ByteArray {
        val bytes = ByteArray(str.length / 2)
        for (i in 0..str.length - 1 step 2) {
            var c = str[i]
            bytes[i / 2] = (c - 'a' shl 4).toByte()
            c = str[i + 1]
            bytes[i / 2] = (bytes[i / 2] + (c - 'a').toByte()).toByte()
        }
        return bytes
    }
}