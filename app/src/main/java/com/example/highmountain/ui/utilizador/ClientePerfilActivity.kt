package com.example.highmountain.ui.utilizador

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
import androidx.navigation.fragment.findNavController
import com.example.highmountain.R
import com.example.highmountain.databinding.ActivityClientePerfilBinding
import com.example.highmountain.ui.models.newUsers
import com.example.highmountain.ui.notifications.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.android.synthetic.main.activity_cliente_perfil.*
import kotlinx.android.synthetic.main.activity_info_cliente.*
import java.io.File
import java.io.IOException

class ClientePerfilActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivityClientePerfilBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_perfil)

        binding = ActivityClientePerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()


        val arraySpinner = listOf<String>("Masculino","Feminino","Não Binário")
        val arrayAdapter = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySpinner)
        spinnersexperfil.adapter = arrayAdapter


        val arraySpinnerSaude = listOf<String>("A+","A-","B+","B-","AB+-","AB-","O+","O-")
        val arrayAdapterSaude = ArrayAdapter(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySpinnerSaude)
        spinnertiposangueperfil.adapter = arrayAdapterSaude

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = Firebase.firestore
        db.collection("newUsers").document(uid)
            .addSnapshotListener { value, error ->

                val user = value?.let {
                    newUsers.fromDoc(it)
                }

                binding.editTextFirstNamePerfilCliente.setText(user?.FirstName)
                binding.editTextLastNamePerfilCliente.setText(user?.LastName)
                binding.editTextPhonePerfilCliente.setText(user?.numeroTelemovel)
                binding.editTextDataNascimentoPerfilCliente.setText(user?.dataNascimento)
                binding.editTextAlergiasProfile.setText(user?.alergias)
                binding.editTextDoencasProfile.setText(user?.doencas)


                var position : String = user?.sexualidade.toString()
                var spinnerPosition = arrayAdapter.getPosition(position)
                spinnersexperfil.setSelection(spinnerPosition)




                var positionSangue: String = user?.tipodeSangue.toString()
                var spinnerPositonSangue = arrayAdapterSaude.getPosition(positionSangue)
                spinnertiposangueperfil.setSelection(spinnerPositonSangue)



                user?.photoURL?.let {
                    val storage = Firebase.storage
                    val storageRef = storage.reference
                    var islandRef = storageRef.child("newUserPhotos/${it}")

                    val ONE_MEGABYTE: Long = 10024*1024
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val inputStream = it.inputStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageViewPerfilCliente.setImageBitmap(bitmap)

                    }
                }




            }

        binding.imageViewPerfilCliente.setOnClickListener{
            dispatchTakePictureIntent()
        }

        binding.buttonSavePerfilCliente.setOnClickListener {
            newUsers.newUserField(spinnersexperfil.selectedItem.toString(), "sexualidade")
            newUsers.newUserField(spinnertiposangueperfil.selectedItem.toString(), "tipodeSangue")
            newUsers.newUserField(binding.editTextDoencasProfile.text.toString(), "doencas")
            newUsers.newUserField(binding.editTextAlergiasProfile.text.toString(), "alergias")
            newUsers.newUserField(binding.editTextFirstNamePerfilCliente.text.toString(), "FirstName")
            newUsers.newUserField(binding.editTextLastNamePerfilCliente.text.toString(), "LastName")
            newUsers.newUserField(binding.editTextDataNascimentoPerfilCliente.text.toString(), "dataNascimento")
            newUsers.newUserField(binding.editTextPhonePerfilCliente.text.toString(),"numeroTelemovel")

            Toast.makeText(this, "Dados Alterados com sucesso!", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProfileFragment.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            ///val imageBitmap = data?.extras?.get("data") as Bitmap
            BitmapFactory.decodeFile(currentPhotoPath).apply {
                binding.imageViewPerfilCliente.setImageBitmap(this)
                uploadFile {
                    if (it != null) {
                        //  Administrador.postField(it, "photoFilename")
                        newUsers.newUserField(it,"photoURL")
                    }
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(
                this.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
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


    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1001
        const val TAG = "ProfileCliente"
    }
}