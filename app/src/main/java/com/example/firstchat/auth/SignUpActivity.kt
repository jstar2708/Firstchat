package com.example.firstchat.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firstchat.MainActivity
import com.example.firstchat.R
import com.example.firstchat.auth.viewmodels.SignUpViewModel
import com.example.firstchat.data_models.User
import com.example.firstchat.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var progressDialogs: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialogs = ProgressDialog(this, R.style.MyAlertDialogStyle)
        progressDialogs.setTitle("Signing you up")
        progressDialogs.setMessage("Please wait while we sign you up")

        supportActionBar?.hide()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[SignUpViewModel::class.java]

        viewModel.action.observe(this, Observer {
            when (it) {
                1 -> {
                    progressDialogs.dismiss()
                    Snackbar.make(binding.root, "Error", Snackbar.LENGTH_SHORT).show()
                }
                2 -> {
                    progressDialogs.dismiss()
                    Snackbar.make(binding.root, "Error while signing you up", Snackbar.LENGTH_SHORT)
                        .show()
                }
                3 -> moveToMainPage()
            }
        })

        val animation = AnimationUtils.loadAnimation(this, R.anim.card_animation)

        binding.card.startAnimation(animation)

        binding.alreadyHaveAnAccount.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            finish()
        }

        binding.siSignUpButton.setOnClickListener {
            if (binding.siEmailEditText.text.isEmpty() || binding.siPasswordEditText.text.isEmpty()) {
                //Show message is password or email is empty
                Snackbar.make(binding.root, "Fill complete details", Snackbar.LENGTH_SHORT).show()
                progressDialogs.dismiss()
            } else {
                progressDialogs.show()
                Handler().postDelayed({
                    val user = User()
                    user.setEmail(binding.siEmailEditText.text.toString())
                    user.setPassword(binding.siPasswordEditText.text.toString())
                    user.setUserName(binding.siUserNameEditText.text.toString())
                    user.setProfilePic("")
                    user.setUserId("")
                    user.setLastMessage("")
                    viewModel.signUp(user)
                }, 2000)
            }
        }
    }

    private fun moveToMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        progressDialogs.dismiss()
        finish()
    }
}