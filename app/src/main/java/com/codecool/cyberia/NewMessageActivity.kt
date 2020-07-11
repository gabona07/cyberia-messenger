package com.codecool.cyberia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = getString(R.string.select_user)
        fetchUsers()
    }

    private fun fetchUsers() {
        val reference = FirebaseDatabase.getInstance().getReference("/users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null) adapter.add(UserItem(user))
                }
                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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
