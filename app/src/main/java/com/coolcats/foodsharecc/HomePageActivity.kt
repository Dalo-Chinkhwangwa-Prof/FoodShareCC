package com.coolcats.foodsharecc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : AppCompatActivity() {

    private val postAdapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        feed_recyclerview.adapter = postAdapter

        FirebaseDatabase.getInstance().reference.child("FoodPosts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val foodList = mutableListOf<FoodPost>()
                    snapshot.children.forEach {
                        it.getValue(FoodPost::class.java)?.let { item ->
                            foodList.add(item)
                        }
                    }
                    postAdapter.posts = foodList
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        add_post_imageview.setOnClickListener {
            val fragment = UploadFragment()

            supportFragmentManager.beginTransaction()
                .addToBackStack(fragment.tag)
                .replace(R.id.main_frame, fragment)
                .commit()
        }
    }
}