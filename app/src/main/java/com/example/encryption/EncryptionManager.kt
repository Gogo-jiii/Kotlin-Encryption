package com.example.encryption

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionManager private constructor() {

    private lateinit var digest: MessageDigest
    var hash: String? = null

    fun getSHA256(): String? {
        try {
            digest = MessageDigest.getInstance("SHA-256")
            digest.update(TOKEN_KEY.toByteArray())
            hash = bytesToHexString(digest.digest())
            return hash
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return ""
    }

    fun encodeBase64(data: String): String {
        try {
            return Base64.encodeToString(data.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun decodeBase64(data: String?): String {
        try {
            return String(Base64.decode(data, Base64.DEFAULT), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun encrypt(plain: String): String? {
        return try {
            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(
                Cipher.ENCRYPT_MODE, SecretKeySpec(
                    TOKEN_KEY.toByteArray(charset("utf-8")),
                    "AES"
                ), IvParameterSpec(iv)
            )
            val cipherText = cipher.doFinal(plain.toByteArray(charset("utf-8")))
            val ivAndCipherText = getCombinedArray(iv, cipherText)
            Base64.encodeToString(ivAndCipherText, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun decrypt(encoded: String?): String? {
        return try {
            val ivAndCipherText = Base64.decode(encoded, Base64.NO_WRAP)
            val iv = Arrays.copyOfRange(ivAndCipherText, 0, 16)
            val cipherText = Arrays.copyOfRange(ivAndCipherText, 16, ivAndCipherText.size)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(
                Cipher.DECRYPT_MODE, SecretKeySpec(
                    TOKEN_KEY.toByteArray(charset("utf-8")),
                    "AES"
                ), IvParameterSpec(iv)
            )
            String(cipher.doFinal(cipherText), Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getCombinedArray(one: ByteArray, two: ByteArray): ByteArray {
        val combined = ByteArray(one.size + two.size)
        for (i in combined.indices) {
            combined[i] = if (i < one.size) one[i] else two[i - one.size]
        }
        return combined
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        // http://stackoverflow.com/questions/332079
        val sb = StringBuffer()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    companion object {
        var instance: EncryptionManager? = null
            get() {
                if (field == null) {
                    field = EncryptionManager()
                }
                return field
            }
            private set
        private const val TOKEN_KEY = "fqJfdzGDvfwbedsKSUGty3VZ9taXxMVw"
    }
}