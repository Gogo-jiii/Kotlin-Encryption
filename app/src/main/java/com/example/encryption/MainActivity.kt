package com.example.encryption

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.encryption.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var encryptionManager: EncryptionManager? = null
    private var result: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        encryptionManager = EncryptionManager.instance

        binding.btnEncryptData.setOnClickListener {
            val data: String = binding.tilData.editText?.text.toString()
            if (!TextUtils.isEmpty(data)) {
                result = encryptionManager?.encrypt(data)
                binding.txtResult.text = result
            } else {
                Toast.makeText(this@MainActivity, "Field is empty.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDecryptData.setOnClickListener {
            if (!TextUtils.isEmpty(result)) {
                result = encryptionManager!!.decrypt(result)
                binding.txtResult.text = result
            } else {
                Toast.makeText(this@MainActivity, "No data to decrypt.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCreateSHA256.setOnClickListener {
            val data: String = binding.tilData.editText?.text.toString()
            if (!TextUtils.isEmpty(data)) {
                result = encryptionManager!!.getSHA256()
                binding.txtResult.text = result
                if (result == encryptionManager!!.getSHA256()) {
                    Log.d("TAG", "same")
                } else {
                    Log.d("TAG", "NOT same")
                }
            } else {
                Toast.makeText(this@MainActivity, "Field is empty.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEncodeBase64.setOnClickListener {
            val data: String = binding.tilData.editText?.text.toString()
            if (!TextUtils.isEmpty(data)) {
                result = encryptionManager!!.encodeBase64(data)
                binding.txtResult.text = result
            } else {
                Toast.makeText(this@MainActivity, "Field is empty.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDecodeBase64.setOnClickListener {
            if (!TextUtils.isEmpty(result)) {
                result = encryptionManager!!.decodeBase64(result)
                binding.txtResult.text = result
            } else {
                Toast.makeText(this@MainActivity, "No data to decode.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}