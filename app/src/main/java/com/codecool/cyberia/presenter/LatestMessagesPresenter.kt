package com.codecool.cyberia.presenter

import com.codecool.cyberia.contract.BaseContract
import com.codecool.cyberia.contract.LatestMessagesContract
import com.codecool.cyberia.model.ChatMessage
import com.codecool.cyberia.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LatestMessagesPresenter: LatestMessagesContract.LatestMessagesPresenter {
    private var view: LatestMessagesContract.LatestMessagesView ?= null
    val latestMessagesMap = HashMap<String, ChatMessage>()

    companion object {
        var currentUser: User? = null
    }

    override fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("/users/$uid")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun verifyUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            view?.startRegistrationActivity()
        }
    }

    override fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                latestMessagesMap[snapshot.key!!] = chatMessage!!
                view?.refreshRecyclerView(latestMessagesMap)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                latestMessagesMap[snapshot.key!!] = chatMessage!!
                view?.refreshRecyclerView(latestMessagesMap)
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    override fun onAttach(view: BaseContract.BaseView) {
        this.view = view as LatestMessagesContract.LatestMessagesView
    }

    override fun onDetach() {
        this.view = null
    }
}