package com.coolcats.foodsharecc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class StartUpChooser : AppCompatActivity() {

    lateinit var openIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuth.getInstance().currentUser?.let{
            //Logged in...navigate to home
            openIntent = Intent(this, HomePageActivity::class.java)
        } ?:
        run {
            //Not logged in... navigate to login
            openIntent = Intent(this, SignInActivity::class.java)
        }

        startActivity(openIntent.also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}