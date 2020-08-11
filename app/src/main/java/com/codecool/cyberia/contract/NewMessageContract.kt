package com.codecool.cyberia.contract

import com.codecool.cyberia.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

interface NewMessageContract : BaseContract {

    interface NewMessagePresenter : BaseContract.BasePresenter {
        fun fetchUsers()
    }

    interface NewMessageView : BaseContract.BaseView {
        fun startChatLogActivity(user: User)
        fun setupAdapter(adapter: GroupAdapter<ViewHolder>)
    }
}