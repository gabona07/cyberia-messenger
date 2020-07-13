package com.codecool.cyberia.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codecool.cyberia.R
import com.codecool.cyberia.model.ChatMessage
import com.codecool.cyberia.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_left.view.*
import kotlinx.android.synthetic.main.chat_right.view.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        setupAdapter()
        changeActionBarTitle()
        listenForMessages()
        sendMessage()
    }

    private fun changeActionBarTitle() {
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        reference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessagesActivity.currentUser
                        adapter.add(ChatItemRight(chatMessage.text, currentUser!!))
                    } else {
                        adapter.add(ChatItemLeft(chatMessage.text, toUser!!))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }

    private fun sendMessage() {
        send_button.setOnClickListener {
            val text = send_text.text.toString()
            val fromId = FirebaseAuth.getInstance().uid
            val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
            val toId = user?.uid

            val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
            val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
            val latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
            val latestMessageToReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")


            val chatMessage = ChatMessage(reference.key!!, text, fromId!!, toId!!, System.currentTimeMillis()/1000  )
            reference.setValue(chatMessage)
            toReference.setValue(chatMessage)
            latestMessageReference.setValue(chatMessage)
            latestMessageToReference.setValue(chatMessage)

            send_text.text.clear()
            chat_log_recyclerview.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun setupAdapter() {
        chat_log_recyclerview.adapter = adapter
    }
}

class ChatItemLeft(val text : String, val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_left
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_left.text = text
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.profile_image_left)
    }
}

class ChatItemRight(val text : String, val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_right
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_right.text = text
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.profile_image_right)
    }
}