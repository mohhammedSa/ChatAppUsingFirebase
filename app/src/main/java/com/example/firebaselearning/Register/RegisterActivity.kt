package com.example.firebaselearning.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.firebaselearning.DataClasses.UserDataClass
import com.example.firebaselearning.Login.LoginActivity
import com.example.firebaselearning.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var authentication: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()

        registerBinding.gotoLogin.setOnClickListener {
            goToLoginActivity()
        }
        registerBinding.registerBtn.setOnClickListener {
            register()
        }
    }

    private fun register() {
        registerBinding.progressBarSignUp.visibility = View.VISIBLE
        val email = registerBinding.emailRegisterET.text.toString()
        val password = registerBinding.passwordRegisterEt.text.toString()
        val userName = registerBinding.userNameRegisterEt.text.toString()
        authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, authentication.uid, Toast.LENGTH_SHORT).show()
                val key = ref.push().key
                val userInfo = UserDataClass(key!!, userName, authentication.uid!!, email)
                ref.push().setValue(userInfo)
                clearingEditTexts()
                registerBinding.progressBarSignUp.visibility = View.GONE
                goToLoginActivity()
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun verifyEmail() {
        val user = authentication.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                goToLoginActivity()
            }
        }?.addOnFailureListener {
            toast(it.message!!)
        }
    }

    private fun goToLoginActivity() {
        val i = Intent(Intent(this, LoginActivity::class.java))
        startActivity(i)
    }

    private fun initialize() {
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        authentication = Firebase.auth

        database = Firebase.database
        ref = database.reference.child("Users")

    }

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun clearingEditTexts() {
        registerBinding.userNameRegisterEt.text.clear()
        registerBinding.emailRegisterET.text.clear()
        registerBinding.passwordRegisterEt.text.clear()
    }
}