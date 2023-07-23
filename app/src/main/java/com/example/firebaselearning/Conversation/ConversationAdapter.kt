package com.example.firebaselearning.Conversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.firebaselearning.DataClasses.MessageClass
import com.example.firebaselearning.DataClasses.UserDataClass
import com.example.firebaselearning.R
import com.google.firebase.auth.FirebaseAuth

class ConversationAdapter(
    val context: Context,
    val list: ArrayList<MessageClass>,
    val authentication: FirebaseAuth
) :
    RecyclerView.Adapter<ConversationAdapter.MyViewHold>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHold {
        return if (viewType == MESSAGE_TYPE_RIGHT) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.right_side_layout, parent, false)
            MyViewHold(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.left_side_layout, parent, false)
            MyViewHold(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHold(itemView: View) : ViewHolder(itemView) {
        val messageField: TextView = itemView.findViewById(R.id.messageTV)
    }

    override fun onBindViewHolder(holder: MyViewHold, position: Int) {
        val item = list[position]
        holder.messageField.text = item.message
    }

    override fun getItemViewType(position: Int): Int {
        val currentUserAuthKey = authentication.uid
        return if (list[position].senderId == currentUserAuthKey) {
            MESSAGE_TYPE_RIGHT
        } else {
            MESSAGE_TYPE_LEFT
        }
    }
}