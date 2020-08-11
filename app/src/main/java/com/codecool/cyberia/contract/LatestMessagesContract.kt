package com.codecool.cyberia.contract

import com.codecool.cyberia.model.ChatMessage

interface LatestMessagesContract: BaseContract {

    interface LatestMessagesPresenter: BaseContract.BasePresenter {
        fun verifyUserLoggedIn()
        fun fetchCurrentUser()
        fun listenForLatestMessages()
    }

    interface LatestMessagesView: BaseContract.BaseView {
        fun startRegistrationActivity()
        fun setupRecyclerView()
        fun refreshRecyclerView(latestMessages: HashMap<String, ChatMessage>)
    }
}