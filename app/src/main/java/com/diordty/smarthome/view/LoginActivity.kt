package com.diordty.smarthome.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.diordty.smarthome.R
import com.diordty.smarthome.databinding.ActivityLoginBinding
import com.diordty.smarthome.showToastShort
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        hideActionbar()
        login()
    }
    private fun hideActionbar (){
        supportActionBar?.hide()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun login(){
        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.LoginBtn.setOnClickListener {
            binding.progressBarLogin.visibility = View.VISIBLE
            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.progressBarLogin.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        binding.progressBarLogin.visibility = View.GONE
                        showToastShort(it.exception.toString())
                    }
                }
            } else {
                binding.progressBarLogin.visibility = View.GONE
                showToastShort("Empty fields is not allowed!")
            }
        }
        binding.newAccountBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}