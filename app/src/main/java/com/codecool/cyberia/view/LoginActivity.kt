package com.codecool.cyberia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.codecool.cyberia.LatestMessagesActivity
import com.codecool.cyberia.R
import com.codecool.cyberia.contract.LoginContract
import com.codecool.cyberia.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class LoginActivity : AppCompatActivity(), LoginContract.LoginView {

    private val presenter: LoginContract.LoginPresenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onAttach(this)
        attachOnClickListeners()
    }

    override fun attachOnClickListeners() {

        // Login button
        login_button.setOnClickListener {
            val email = email_login.text.toString()
            val password = password_login.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }
            presenter.signIn(email, password)
        }

        // Back to registration button
        back_to_registration.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun openLatestMessagesActivity() {
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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
