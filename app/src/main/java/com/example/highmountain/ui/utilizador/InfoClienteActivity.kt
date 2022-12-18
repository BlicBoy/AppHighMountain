package com.example.highmountain.ui.utilizador

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityInfoClienteBinding
import com.example.highmountain.ui.models.newUsers
import com.example.highmountain.ui.notifications.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.android.synthetic.main.activity_info_cliente.*
import java.io.File
import java.io.IOException

class InfoClienteActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityInfoClienteBinding


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_cliente)
        binding = ActivityInfoClienteBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val arraySpinner = listOf<String>("Masculino","Feminino","Não Binário")
         val arrayAdapter = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySpinner)

        spinnersex.adapter = arrayAdapter

        binding.imageViewInfoCliente.setOnClickListener{
            dispatchTakePictureIntent()
        }

        binding.buttonSaveInfoCliente.setOnClickListener {
            uploadFile { filename ->
                 filename?.let{
                    newUsers(
                        Firebase.auth.currentUser?.uid.toString(),
                        binding.editTextFirstNameInfoCliente.text.toString(),
                        binding.editTextLastNameInfoCliente.text.toString(),
                        binding.editTextPhoneInfoCliente.text.toString(),
                       it,
                        binding.editTextDataNascimentoInfoCliente.text.toString(),
                        spinnersex.selectedItem.toString(),
                        "Cliente"
                    ).sendNewUser { error ->
                        error?.let {
                            Toast.makeText(this,"Ocurreu algum erro!",Toast.LENGTH_LONG).show()
                        }?: kotlin.run {
                            Toast.makeText(this,"Guardado com sucesso!",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,InfoClienteSaudeActivity::class.java))
                        }
                    }
                }?: kotlin.run { Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(
                this.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.highmountain.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, ProfileFragment.REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val userId: String = FirebaseAuth.getInstance().currentUser!!.uid
        val storageDir: File = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${userId}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun uploadFile(callback: (String?)->Unit) {
        val storage = Firebase.storage
        var storageRef = storage.reference
        val file = Uri.fromFile(File(currentPhotoPath))
        var metadata = storageMetadata {
            contentType = "image/jpg"
        }

        val uploadTask = storageRef.child("newUserPhotos/${file.lastPathSegment}")
            .putFile(file, metadata)

        uploadTask.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
            Log.d("Upload", "Upload is $progress% done")
        }.addOnPausedListener {
            Log.d("Upload", "Upload is paused")
        }.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("Upload", it.toString())
            callback(null)
        }.addOnSuccessListener {
            // Handle successful uploads on complete
            Log.d("Upload", it.uploadSessionUri.toString())
            callback(file.lastPathSegment)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProfileFragment.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            ///val imageBitmap = data?.extras?.get("data") as Bitmap
            BitmapFactory.decodeFile(currentPhotoPath).apply {
                binding.imageViewInfoCliente.setImageBitmap(this)
                uploadFile {
                    if (it != null) {
                        newUsers.newUserField(it, "photoURL")
                    }
                }
            }
        }
    }


    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1001
        const val TAG = "NewUserActivity"
    }

}