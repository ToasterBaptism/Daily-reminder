package com.dailyreminder.data.util

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptionUtils @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val KEYSTORE_ALIAS = "DailyReminderMasterKey"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16
    }
    
    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context, KEYSTORE_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
    
    fun createEncryptedSharedPreferences(fileName: String) = 
        EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    
    fun createEncryptedFile(file: File): EncryptedFile = 
        EncryptedFile.Builder(
            context,
            file,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    
    fun encryptData(data: ByteArray, password: String? = null): EncryptedData {
        val secretKey = password?.let { generateKeyFromPassword(it) } ?: getOrCreateSecretKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data)
        
        return EncryptedData(encryptedData, iv)
    }
    
    fun decryptData(encryptedData: EncryptedData, password: String? = null): ByteArray {
        val secretKey = password?.let { generateKeyFromPassword(it) } ?: getOrCreateSecretKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, encryptedData.iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        
        return cipher.doFinal(encryptedData.data)
    }
    
    fun encryptString(text: String, password: String? = null): String {
        val encryptedData = encryptData(text.toByteArray(Charsets.UTF_8), password)
        return android.util.Base64.encodeToString(
            encryptedData.iv + encryptedData.data,
            android.util.Base64.DEFAULT
        )
    }
    
    fun decryptString(encryptedText: String, password: String? = null): String {
        val combined = android.util.Base64.decode(encryptedText, android.util.Base64.DEFAULT)
        val iv = combined.sliceArray(0..GCM_IV_LENGTH - 1)
        val data = combined.sliceArray(GCM_IV_LENGTH until combined.size)
        
        val decryptedData = decryptData(EncryptedData(data, iv), password)
        return String(decryptedData, Charsets.UTF_8)
    }
    
    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        
        return if (keyStore.containsAlias(KEYSTORE_ALIAS)) {
            keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
        } else {
            generateSecretKey()
        }
    }
    
    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEYSTORE_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }
    
    private fun generateKeyFromPassword(password: String): SecretKey {
        // For simplicity, using a basic key derivation
        // In production, use PBKDF2 or similar
        val keyBytes = password.toByteArray(Charsets.UTF_8)
        val paddedKey = ByteArray(32) // 256 bits
        System.arraycopy(keyBytes, 0, paddedKey, 0, minOf(keyBytes.size, 32))
        
        return javax.crypto.spec.SecretKeySpec(paddedKey, "AES")
    }
    
    fun generateSecureHash(data: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(data.toByteArray(Charsets.UTF_8))
        return android.util.Base64.encodeToString(hashBytes, android.util.Base64.DEFAULT)
    }
    
    fun verifyHash(data: String, hash: String): Boolean {
        return generateSecureHash(data) == hash
    }
    
    data class EncryptedData(
        val data: ByteArray,
        val iv: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            
            other as EncryptedData
            
            if (!data.contentEquals(other.data)) return false
            if (!iv.contentEquals(other.iv)) return false
            
            return true
        }
        
        override fun hashCode(): Int {
            var result = data.contentHashCode()
            result = 31 * result + iv.contentHashCode()
            return result
        }
    }
}