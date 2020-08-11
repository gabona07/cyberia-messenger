package com.codecool.cyberia.presenter

import com.codecool.cyberia.contract.BaseContract
import com.codecool.cyberia.contract.ChatLogContract
import com.codecool.cyberia.model.ChatMessage
import com.codecool.cyberia.model.User
import com.codecool.cyberia.view.ChatItemLeft
import com.codecool.cyberia.view.ChatItemRight
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class ChatLogPresenter: ChatLogContract.ChatLogPresenter {

    private var view: ChatLogContract.ChatLogView ?= null

    override fun listenForMessages(toUser: User, adapter: GroupAdapter<ViewHolder>) {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser.uid
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        reference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessagesPresenter.currentUser
                        adapter.add(
                            ChatItemRight(
                                chatMessage.text,
                                currentUser!!
                            )
                        )
                    } else {
                        adapter.add(
                            ChatItemLeft(
                                chatMessage.text,
                                toUser
                            )
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
    }

    override fun uploadMessageToDatabase(text: String, fromId: String, toId: String) {
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val latestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        val latestMessageToReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")


        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis()/1000  )
        reference.setValue(chatMessage)
        toReference.setValue(chatMessage)
        latestMessageReference.setValue(chatMessage)
        latestMessageToReference.setValue(chatMessage)
    }

    override fun onAttach(view: BaseContract.BaseView) {
        this.view = view as ChatLogContract.ChatLogView
    }

    override fun onDetach() {
        this.view = null
    }
}