package com.example.firebaselearning.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaselearning.MainActivity.MainActivity
import com.example.firebaselearning.Register.RegisterActivity
import com.example.firebaselearning.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    private lateinit var database: FirebaseDatabase
    lateinit var authentication: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()

        goToRegisterActivity()

        loginBinding.loginBtn.setOnClickListener {
            val email = loginBinding.emailLoginET.text.toString()
            val password = loginBinding.passwordLoginEt.text.toString()
            authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToMainActivity()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun verifyEmail() {
        val user = authentication.currentUser
        if (user!!.isEmailVerified) {
            goToMainActivity()
        } else {
            toast("Please Verify Your Account")
        }
    }

    private fun goToMainActivity() {
        val i = Intent(Intent(this, MainActivity::class.java))
        startActivity(i)
    }

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun goToRegisterActivity() {
        loginBinding.gotoRegister.setOnClickListener {
            val i = Intent(Intent(this, RegisterActivity::class.java))
            startActivity(i)
        }
    }

    private fun initialize() {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        database = Firebase.database
        authentication = Firebase.auth
    }
}