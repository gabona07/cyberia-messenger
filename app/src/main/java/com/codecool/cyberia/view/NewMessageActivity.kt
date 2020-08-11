package com.codecool.cyberia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codecool.cyberia.R
import com.codecool.cyberia.contract.NewMessageContract
import com.codecool.cyberia.model.User
import com.codecool.cyberia.presenter.NewMessagePresenter
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import java.lang.Exception

class NewMessageActivity : AppCompatActivity(), NewMessageContract.NewMessageView {

    private val presenter = NewMessagePresenter()

    companion object {
        const val USER_KEY = "USER_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = getString(R.string.select_user)
        presenter.onAttach(this)
        presenter.fetchUsers()
    }

    override fun startChatLogActivity(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        intent.putExtra(USER_KEY, user)
        startActivity(intent)
        finish()
    }

    override fun setupAdapter(adapter: GroupAdapter<ViewHolder>) {
        recyclerview_newmessage.adapter = adapter
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

class UserItem(val user: User) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.user_name_new_message.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.user_image_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
