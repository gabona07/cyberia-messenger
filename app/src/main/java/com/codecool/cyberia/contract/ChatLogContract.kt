package com.codecool.cyberia.contract

import com.codecool.cyberia.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

interface ChatLogContract : BaseContract {

    interface ChatLogPresenter : BaseContract.BasePresenter {
        fun listenForMessages(toUser: User, adapter: GroupAdapter<ViewHolder>)
        fun uploadMessageToDatabase(text: String, fromId: String, toId: String)
    }

    interface ChatLogView : BaseContract.BaseView {
        fun changeActionBarTitle()
        fun setupAdapter()
    }
}