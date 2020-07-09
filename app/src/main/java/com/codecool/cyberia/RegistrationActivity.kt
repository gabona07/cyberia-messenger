package com.codecool.cyberia

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        have_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        performRegistration()
        selectPhoto()
    }

    private fun performRegistration() {

        //Add onClickListener to registration button
        register_button.setOnClickListener {
            val email = email_registration.text.toString()
            val password = password_registration.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter e-mail and password!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Firebase authentication to create new user with the given e-mail and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        return@addOnCompleteListener
                    } else {
                        uploadImageToDatabase()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun uploadImageToDatabase() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/images/$filename")
        storage.putFile(selectedPhotoUri!!).addOnSuccessListener {
            storage.downloadUrl.addOnSuccessListener {
                saveUserToDatabase(it.toString())
                // Start new activity
                val intent = Intent(this, LatestMessagesActivity::class.java)
                // Clear previous activities
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun saveUserToDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val database = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username_registration.text.toString(), profileImageUrl)
        database.setValue(user)
    }

    private fun selectPhoto() {
        select_photo_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
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

    class User (val uid: String, val username: String, val profileImageUrl: String)
}
