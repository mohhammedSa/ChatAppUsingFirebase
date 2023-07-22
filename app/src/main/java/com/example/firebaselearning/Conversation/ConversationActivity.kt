package com.example.firebaselearning.Conversation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaselearning.R
import com.example.firebaselearning.databinding.ActivityConversationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ConversationActivity : AppCompatActivity() {
    lateinit var conversationBinding: ActivityConversationBinding
    private lateinit var authentication: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        conversationBinding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(conversationBinding.root)
        authentication = Firebase.auth

        val userName = intent.getStringExtra("userName")
        val id = intent.getStringExtra("id")
        val authKey = intent.getStringExtra("authKey")

        val senderAuthId = authentication.uid
    }
}