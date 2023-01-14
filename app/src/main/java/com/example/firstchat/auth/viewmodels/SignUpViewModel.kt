package com.example.firstchat.auth.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstchat.data_models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://firstchat2-73ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
    var action: MutableLiveData<Int> = MutableLiveData(0)

    fun signUp(user: User) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener {
            if(it.isSuccessful){
                user.setUserId(auth.currentUser?.uid.toString())
                database.reference.child("User").child(auth.currentUser?.uid.toString()).setValue(user).addOnCompleteListener {
                    if(it.isSuccessful){
                        action.value = 3
                    }
                    else{
                        //Display error message on screen
                        auth.currentUser?.delete()
                        action.value = 1
                    }
                }
            }
            else{
                //Display that user cannot be signed up
                action.value = 2
            }
        }
    }

}