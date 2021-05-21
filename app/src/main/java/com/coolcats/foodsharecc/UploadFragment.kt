package com.coolcats.foodsharecc

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaCas
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.feed_item_layout.*
import kotlinx.android.synthetic.main.upload_fragment_layout.*
import java.io.ByteArrayOutputStream

class UploadFragment: Fragment() {


    private lateinit var image: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.upload_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upload_imageview.setOnClickListener {
            val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camIntent, 707)
        }

        upload_button.setOnClickListener {
            if(this::image.isInitialized){
                val outputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                val storageReference = FirebaseStorage.getInstance().reference.child("Images/${FirebaseAuth.getInstance().currentUser?.uid}")

                val uploadTask = storageReference.putBytes(outputStream.toByteArray())
                uploadTask.addOnCompleteListener{ task ->
                    if(task.isSuccessful){

                        storageReference.downloadUrl.addOnCompleteListener {
                            if(task.isSuccessful){
                                uploadFood(it.result.toString())
                            }
                        }

                    }
                }
            } else {
                Toast.makeText(requireContext(), "You need an image!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun uploadFood(imageUrl: String) {

        val ref = FirebaseDatabase.getInstance().reference.child("FoodPosts")
        val key = ref.push().key ?: ""
        val post = FoodPost(
            FirebaseAuth.getInstance().currentUser.toString(),
            key,
            imageUrl,
            feed_caption.text.toString().trim()
            )
        ref.child(key).setValue(post)
        Toast.makeText(requireContext(), "Success!!", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 707){

            val bitmap = data?.extras?.get("data") as Bitmap?

            bitmap?.let {
                Glide.with(this)
                    .applyDefaultRequestOptions(RequestOptions().centerCrop())
                    .load(it).into(upload_imageview)
                image = it

            }
        }
    }
}