package com.diordty.smarthome.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.diordty.smarthome.R
import com.diordty.smarthome.crypto.CryptoManager
import com.diordty.smarthome.databinding.ActivityRegisterBinding
import com.diordty.smarthome.models.User
import com.diordty.smarthome.showToastShort
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.M)
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    val cryptoManager = CryptoManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        hideActionbar()
        register()

    }
    private fun hideActionbar (){
        supportActionBar?.hide()
    }
    private fun register(){
        // binding init
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // firebase init
        firebaseAuth = FirebaseAuth.getInstance()
        // sing up action
        binding.signUpBtn.setOnClickListener {
            binding.progressBarRegister.visibility = View.VISIBLE
            val email = binding.signUpEmailEdt.text.toString()
            val password = binding.signUpPasswordEdt.text.toString()
            val confirmPassword = binding.signUpRetypePasswordEdt.text.toString()
            val name = binding.nameRegister.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful){
                            val bytes = password.encodeToByteArray()
                            val file = File(filesDir, "secret.txt")
                            if(!file.exists()) {
                                file.createNewFile()
                            }
                            val fos = FileOutputStream(file)
                            val passwordEncrypted = cryptoManager.encrypt(bytes, fos).toString()
                            println("Password Encrypted from [$password] to-->>>: $passwordEncrypted")
                            val user = User(name, email, passwordEncrypted)
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(user).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        binding.progressBarRegister.visibility = View.GONE
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else {
                                        binding.progressBarRegister.visibility = View.GONE
                                        showToastShort(it.exception.toString())
                                        Log.e("TAG", it.exception.toString())
                                    }
                                }
                        }
                        else{
                            binding.progressBarRegister.visibility = View.GONE
                            showToastShort(it.exception.toString())
                            Log.e("TAG", it.exception.toString())
                        }
                    }
                } else{
                    binding.progressBarRegister.visibility = View.GONE
                    showToastShort("Password is not matching")
                }
            } else {
                binding.progressBarRegister.visibility = View.GONE
                showToastShort("Empty fields is not allowed!")
            }
        }

    }
}