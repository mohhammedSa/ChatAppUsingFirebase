package com.example.firebaselearning.Conversation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselearning.DataClasses.MessageClass
import com.example.firebaselearning.DataClasses.UserDataClass
import com.example.firebaselearning.MainActivity.MainActivity
import com.example.firebaselearning.databinding.ActivityConversationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConversationActivity : AppCompatActivity() {
    lateinit var conversationBinding: ActivityConversationBinding
    private lateinit var authentication: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    var list = ArrayList<MessageClass>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        conversationBinding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(conversationBinding.root)
        authentication = Firebase.auth
        database = Firebase.database
        list = ArrayList()
        ref = database.reference.child("Chats")


        conversationBinding.returnBtn.setOnClickListener {
            val i = Intent(Intent(this, MainActivity::class.java))
            startActivity(i)
        }


        val userName = intent.getStringExtra("userName")
        val receiverUserAuthKey = intent.getStringExtra("authKey")
        val currentUserAuthKEy = authentication.uid

        conversationBinding.UserNameTv.text = userName
        conversationBinding.sendMessageBtn.setOnClickListener {
            val message = conversationBinding.messageET.text.toString()
            sendMessage(currentUserAuthKEy!!, receiverUserAuthKey!!, message)
            conversationBinding.messageET.text.clear()
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (n in snapshot.children) {
                    val messageData = n.getValue(MessageClass::class.java)
                    if (messageData?.senderId.equals(currentUserAuthKEy) && messageData?.receiverId.equals(
                            receiverUserAuthKey
                        ) ||
                        messageData?.senderId.equals(receiverUserAuthKey) && messageData?.receiverId.equals(
                            currentUserAuthKEy
                        )
                    ) {
                        list.add(messageData!!)
                    }
                }
                recyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        val messageData = MessageClass(senderId, receiverId, message)
        ref.push().setValue(messageData)
    }

    fun recyclerView() {
        conversationBinding.conversationRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        conversationBinding.conversationRecyclerView.adapter =
            ConversationAdapter(this, list, authentication)
    }
}