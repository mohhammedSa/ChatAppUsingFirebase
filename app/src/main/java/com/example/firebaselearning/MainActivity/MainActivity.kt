package com.example.firebaselearning.MainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselearning.DataClasses.UserDataClass
import com.example.firebaselearning.Login.LoginActivity
import com.example.firebaselearning.R
import com.example.firebaselearning.Register.RegisterActivity
import com.example.firebaselearning.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var usersList: ArrayList<UserDataClass>
    private lateinit var authentication: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
        toolBarAndSignOut()
        checkIfAlreadySignIn()
        readUserListFromFirebase()

        binding.mainActivityAuthKey.text = authentication.uid


    }

    private fun initialize() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersList = ArrayList()

        authentication = Firebase.auth

        database = Firebase.database
        ref = database.reference.child("Users")
    }

    private fun readUserListFromFirebase() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (n in snapshot.children) {
                    val userInfo = n.getValue(UserDataClass::class.java)
                    if (userInfo?.authId != authentication.uid)
                        usersList.add(userInfo!!)
                }
                recyclerviewHandler()
            }

            override fun onCancelled(error: DatabaseError) {
                toast(error.message)
            }
        })
    }

    private fun checkIfAlreadySignIn() {
        if (authentication.currentUser == null) {
            gotoLoginPage()
        }
    }

    private fun toolBarAndSignOut() {
        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.signOut) {
                authentication.signOut()
                gotoLoginPage()
            }
            true
        }
    }

    private fun gotoLoginPage() {
        val i = Intent(Intent(this, LoginActivity::class.java))
        startActivity(i)
    }

    private fun recyclerviewHandler() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = MyAdapter(this, usersList, authentication)
    }

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun currentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        return dateFormat.format(currentTime)
    }
}