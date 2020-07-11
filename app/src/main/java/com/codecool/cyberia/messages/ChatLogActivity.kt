package com.codecool.cyberia.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codecool.cyberia.R
import com.codecool.cyberia.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatItemLeft())
        adapter.add(ChatItemRight())
        adapter.add(ChatItemLeft())
        adapter.add(ChatItemRight())
        chat_log_recyclerview.adapter = adapter
    }
}

class ChatItemLeft : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_left
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

class ChatItemRight : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_right
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}