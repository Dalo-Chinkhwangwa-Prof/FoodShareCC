package com.coolcats.foodsharecc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.upload_fragment_layout.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UploadFragment : Fragment() {


    private lateinit var file: File
    private lateinit var fileName: String
    private lateinit var filePath: String

    private lateinit var image: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.upload_fragment_layout, container, false)

    /*Creating a temporary file in memory to hold the picture we will take with our camera :-) */
    private fun createTemporaryFile() {
        val timeStamp = Date()
        val dateFormat = SimpleDateFormat("yyyymmdd_ss", Locale.US)
        fileName = dateFormat.format(timeStamp)

        Log.d("TAG_X", "ImageName: $fileName")

        val directory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        file = File.createTempFile(
            fileName,
            ".jpg",
            directory
        )

        filePath = file.absolutePath
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upload_imageview.setOnClickListener {
            val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            createTemporaryFile()
            val imagePath = FileProvider.getUriForFile(
                requireContext(),
                "com.coolcats.foodsharecc.provider",
                file
            )
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath)
            startActivityForResult(camIntent, 707)
        }

        upload_button.setOnClickListener {
            if (this::image.isInitialized) {
                val outputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                val storageReference =
                    FirebaseStorage.getInstance().reference.child("uploads")
                        .child("${FirebaseAuth.getInstance().currentUser?.uid}")
                        .child(fileName)

                val uploadTask = storageReference.putBytes(outputStream.toByteArray())
                uploadTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        storageReference.downloadUrl.addOnCompleteListener {
                            if (task.isSuccessful) {
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
            FirebaseAuth.getInstance().currentUser?.uid.toString(),
            key,
            imageUrl,
            feed_caption.text.toString().trim()
        )
        ref.child(key).setValue(post)
        Toast.makeText(requireContext(), "Success!!", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 707) {

            val bitmap = BitmapFactory.decodeFile(filePath) //data?.extras?.get("data") as Bitmap?

            bitmap?.let {
                Glide.with(this)
                    .applyDefaultRequestOptions(RequestOptions().centerCrop())
                    .load(it).into(upload_imageview)
                image = it
            }
        }
    }
}