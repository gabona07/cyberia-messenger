package com.codecool.cyberia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.codecool.cyberia.LatestMessageRow
import com.codecool.cyberia.NewMessageActivity
import com.codecool.cyberia.R
import com.codecool.cyberia.contract.LatestMessagesContract
import com.codecool.cyberia.model.ChatMessage
import com.codecool.cyberia.presenter.LatestMessagesPresenter
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import java.lang.Exception

class LatestMessagesActivity : AppCompatActivity(), LatestMessagesContract.LatestMessagesView {
    private val presenter = LatestMessagesPresenter()
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        setupRecyclerView()
        presenter.onAttach(this)
        presenter.fetchCurrentUser()
        presenter.listenForLatestMessages()
        presenter.verifyUserLoggedIn()
    }

    override fun setupRecyclerView() {
        latest_message_recyclerview.adapter = adapter
        latest_message_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }
    }

    override fun refreshRecyclerView(latestMessages: HashMap<String, ChatMessage>) {
        adapter.clear()
        latestMessages.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    override fun startRegistrationActivity() {
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_message_menu -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out_menu -> {
                FirebaseAuth.getInstance().signOut()
                startRegistrationActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
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
