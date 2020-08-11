package com.codecool.cyberia.contract

interface LoginContract: BaseContract {

    interface LoginPresenter: BaseContract.BasePresenter {
        fun signIn(email: String, password: String)
    }

    interface LoginView: BaseContract.BaseView {
        fun openLatestMessagesActivity()
        fun attachOnClickListeners()
    }
}