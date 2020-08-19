package com.codecool.cyberia.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.codecool.cyberia.R
import com.codecool.cyberia.contract.RegistrationContract
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.inject
import java.lang.Exception

class RegistrationActivity : AppCompatActivity(), RegistrationContract.RegistrationView {

    private val presenter: RegistrationContract.RegistrationPresenter by inject()
    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        presenter.onAttach(this)
        setBackToLoginListener()
        setRegistrationButtonListener()
        selectPhotoListener()
    }

    override fun setBackToLoginListener() {
        have_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun setRegistrationButtonListener() {
        register_button.setOnClickListener {
            val email = email_registration.text.toString()
            val password = password_registration.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter e-mail and password!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            presenter.createNewUser(selectedPhotoUri!!, email, password, username_registration.text.toString())
        }
    }

    override fun selectPhotoListener() {
        select_photo_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }


    override fun startLatestMessagesActivity() {
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            select_photo_circular.setImageBitmap(bitmap)
            select_photo_button.alpha = 0f
        }
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun onError(exception: Exception) {
        Toast.makeText(this, "${exception.message}", Toast.LENGTH_LONG).show()
    }
}
