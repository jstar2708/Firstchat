package com.example.firstchat.auth.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://firstchat2-73ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
    var action: MutableLiveData<Int> = MutableLiveData(0)

    fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                //Move to main screen
                action.value = 1
            }
            else{
                //Displaying error message
                action.value = 2
            }
        }
    }

    fun isUserIsAlreadySignedIn(){
        action.value = if(auth.currentUser != null) 1 else 0
    }
}