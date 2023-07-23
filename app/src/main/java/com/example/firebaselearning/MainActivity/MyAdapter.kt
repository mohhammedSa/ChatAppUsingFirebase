package com.example.firebaselearning.MainActivity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.firebaselearning.Conversation.ConversationActivity
import com.example.firebaselearning.DataClasses.UserDataClass
import com.example.firebaselearning.R
import com.google.firebase.auth.FirebaseAuth

class MyAdapter(
    private val context: Context,
    private val list: ArrayList<UserDataClass>,
    val authentication: FirebaseAuth
) :
    RecyclerView.Adapter<MyAdapter.MyViewHold>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHold {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_card, parent, false)
        return MyViewHold(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHold(itemView: View) : ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.userNameTV)
    }

    override fun onBindViewHolder(holder: MyViewHold, position: Int) {
        val item = list[position]
        holder.nameTV.text = item.userName

        holder.itemView.setOnClickListener {
            val i = Intent(Intent(context, ConversationActivity::class.java))
            i.putExtra("userName", item.userName)
            i.putExtra("id", item.dbId)
            i.putExtra("authKey", item.authId)
            context.startActivity(i)
        }

    }
}