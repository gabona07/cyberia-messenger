package com.codecool.cyberia.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codecool.cyberia.R
import com.codecool.cyberia.contract.ChatLogContract
import com.codecool.cyberia.model.User
import com.codecool.cyberia.presenter.ChatLogPresenter
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_left.view.*
import kotlinx.android.synthetic.main.chat_right.view.*
import java.lang.Exception

class ChatLogActivity : AppCompatActivity(), ChatLogContract.ChatLogView {

    private val presenter = ChatLogPresenter()
    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        setupAdapter()
        changeActionBarTitle()
        presenter.onAttach(this)
        presenter.listenForMessages(toUser!!, adapter)
        sendMessage()
    }

    override fun changeActionBarTitle() {
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username
    }

    private fun sendMessage() {
        send_button.setOnClickListener {
            val text = send_text.text.toString()
            val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
            val toId = user?.uid
            val fromId = FirebaseAuth.getInstance().uid

            presenter.uploadMessageToDatabase(text, fromId!!, toId!!)

            send_text.text.clear()
            chat_log_recyclerview.scrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun setupAdapter() {
        chat_log_recyclerview.adapter = adapter
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun onError(exception: Exception) {
        TODO("Not yet implemented")
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