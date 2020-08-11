package com.codecool.cyberia.contract

import android.net.Uri

interface RegistrationContract: BaseContract {

    interface RegistrationPresenter: BaseContract.BasePresenter {
        fun saveUserToDatabase(profileImageUrl: String,  username: String)
        fun createNewUser(selectedPhotoUri: Uri, email: String, password: String, username: String)
        fun uploadImageToDatabase(selectedPhotoUri: Uri, username: String)
    }

    interface RegistrationView : BaseContract.BaseView {
        fun selectPhotoListener()
        fun setRegistrationButtonListener()
        fun setBackToLoginListener()
        fun startLatestMessagesActivity()
    }
}