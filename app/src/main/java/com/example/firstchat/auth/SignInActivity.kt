package com.example.firstchat.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firstchat.MainActivity
import com.example.firstchat.R
import com.example.firstchat.auth.viewmodels.SignInViewModel
import com.example.firstchat.databinding.ActivitySignInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var progressDialogs: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[SignInViewModel::class.java]

        progressDialogs = ProgressDialog(this, R.style.MyAlertDialogStyle)
        progressDialogs.setTitle("Signing you in")
        progressDialogs.setMessage("Please wait while we sign you in")

        viewModel.action.observe(this, Observer {
            if(it == 1){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                progressDialogs.dismiss()
                finish()
            }
            else if(it == 2){
                Snackbar.make(binding.root, "Error while signing you in", Snackbar.LENGTH_SHORT).show()
                progressDialogs.dismiss()
            }
        })

        viewModel.isUserIsAlreadySignedIn()

        val animation = AnimationUtils.loadAnimation(this, R.anim.card_animation)

        binding.card.startAnimation(animation)

        binding.siClickForSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            finish()
        }

        binding.siSignInButton.setOnClickListener {
            if(binding.siEmailEditText.text.isEmpty() || binding.siPasswordEditText.text.isEmpty()){
                //Show message is password or email is empty
                Snackbar.make(binding.root, "Fill complete details", Snackbar.LENGTH_SHORT).show()
            }
            else{
                progressDialogs.show()
                Handler().postDelayed({
                    viewModel.signIn(binding.siEmailEditText.text.toString(), binding.siPasswordEditText.text.toString())
                }, 2000)
            }
        }
    }
}