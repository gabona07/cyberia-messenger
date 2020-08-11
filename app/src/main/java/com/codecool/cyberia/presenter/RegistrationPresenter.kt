package com.codecool.cyberia.presenter

import android.net.Uri
import com.codecool.cyberia.contract.BaseContract
import com.codecool.cyberia.contract.RegistrationContract
import com.codecool.cyberia.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class RegistrationPresenter: RegistrationContract.RegistrationPresenter {
    private var view: RegistrationContract.RegistrationView ?= null

    override fun onAttach(view: BaseContract.BaseView) {
        this.view = view as RegistrationContract.RegistrationView
    }

    override fun onDetach() {
        this.view = null
    }

    override fun createNewUser(selectedPhotoUri: Uri, email: String, password: String, username: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    uploadImageToDatabase(selectedPhotoUri, username)
                    view?.startLatestMessagesActivity()
                }
            }
            .addOnFailureListener {
                view?.onError(it)
            }
    }

    override fun uploadImageToDatabase(selectedPhotoUri: Uri, username: String) {
        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/images/$filename")
        storage.putFile(selectedPhotoUri).addOnSuccessListener {
            storage.downloadUrl.addOnSuccessListener {
                saveUserToDatabase(it.toString(), username)
            }
        }
    }


    override fun saveUserToDatabase(profileImageUrl: String, username: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val database = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username, profileImageUrl)
        database.setValue(user)
    }
}