package com.codecool.cyberia.presenter

import com.codecool.cyberia.contract.BaseContract
import com.codecool.cyberia.contract.LoginContract
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter: LoginContract.LoginPresenter {

    private var view: LoginContract.LoginView ?= null

    override fun onAttach(view: BaseContract.BaseView) {
        this.view = view as LoginContract.LoginView
    }

    override fun onDetach() {
        this.view = null
    }

    override fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    view?.openLatestMessagesActivity()
                }
            }
            .addOnFailureListener {
                view?.onError(it)
            }
    }
}