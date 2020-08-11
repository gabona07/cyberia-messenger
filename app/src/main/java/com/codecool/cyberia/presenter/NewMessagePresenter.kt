package com.codecool.cyberia.presenter

import com.codecool.cyberia.contract.BaseContract
import com.codecool.cyberia.contract.NewMessageContract
import com.codecool.cyberia.model.User
import com.codecool.cyberia.view.UserItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class NewMessagePresenter: NewMessageContract.NewMessagePresenter {

    private var activity: NewMessageContract.NewMessageView ?= null

    override fun fetchUsers() {
        val reference = FirebaseDatabase.getInstance().getReference("/users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null) adapter.add(
                        UserItem(user)
                    )
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    activity?.startChatLogActivity(userItem.user)
                }
                activity?.setupAdapter(adapter)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onAttach(view: BaseContract.BaseView) {
        this.activity = view as NewMessageContract.NewMessageView
    }

    override fun onDetach() {
        this.activity = null
    }
}