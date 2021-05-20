package com.coolcats.foodsharecc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class SignInActivity : AppCompatActivity() {

    private val postReference = FirebaseDatabase.getInstance().reference.child("Posts")
    private val userReference = FirebaseDatabase.getInstance().reference.child("Users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dish = Food("Jerk Chicken", "Jamaican", 760, "Chicken, Curry Powder, Steamed Rice","" )

        val user = User("DaloFood", "12340")


//        userReference.push().setValue(user)

//        val key: String = postReference.push().key ?: ""
//        Log.d("TAG_X", "pushed -> empty space -> $key")
//        postReference.child(key).setValue(dish)
//        Log.d("TAG_X", "Food item posted $key")

        //Read from Realtime Database
//        val postList = mutableListOf<Food>()
//        postReference.addValueEventListener(object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                snapshot.children.forEach {
//                    it.getValue(Food::class.java)?.let { food ->
//                        postList.add(food)
//                    }
//                }
////                addToRecyclerView(postList)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            //we never make mistakes..
//            }
//        })


    }


    fun signupUser(signUpUser: SignUpUser) {

    }

    data class Food(var dishName: String, val type: String, val calories: Int, val recipe: String, var key: String){
        constructor(): this("","",0,"", "")
    }

    data class User(val userName: String, val userId:String)
}