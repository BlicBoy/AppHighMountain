package com.example.highmountain.ui.notifications

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.highmountain.LoginActivity
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentProfileBinding
import com.example.highmountain.ui.LoadingDialog
import com.example.highmountain.ui.PREF_CODE
import com.example.highmountain.ui.models.newUsers
import com.example.highmountain.ui.role_user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.android.synthetic.main.activity_cliente_perfil.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.IOException

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val auth = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //loading
        val loading = LoadingDialog(requireActivity())
        loading.startLoading()
        Handler().postDelayed({
            if(loading.isOnline(requireContext())){
                loading.isDismiss()
            }else{
                loading.isDismiss()
                Toast.makeText(requireContext(),"Sem ligação a internet", Toast.LENGTH_SHORT).show()
            }
        }, 5000) //possivel alterar tempo

        val arraySpinner = listOf<String>("Masculino","Feminino","Não Binário")
        val arrayAdapter = ArrayAdapter<String>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySpinner)
        spinnersexadmin.adapter = arrayAdapter

        binding.imageViewProfileAdmin.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.buttonCodeAdmin.setOnClickListener {
           findNavController().navigate(R.id.action_navigation_profile_to_codeFragment)
        }

        binding.buttonLogout.setOnClickListener {
            deletePreferences()
            Firebase.auth.signOut()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }

        binding.buttonAlterarPasswordAdmin.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_alterarPasswordFragment)
        }

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = Firebase.firestore
        db.collection("newUsers").document(uid)
            .addSnapshotListener { value, error ->

                val user = value?.let {
                    newUsers.fromDoc(it)
                }

                binding.editTextAdminFirstName.setText(user?.FirstName)
                binding.editTextAdminLastName.setText(user?.LastName)
                binding.editTextAdminPhone.setText(user?.numeroTelemovel)
                binding.editTextAdminDate.setText(user?.dataNascimento)


                var position : String = user?.sexualidade.toString()
                var spinnerPosition = arrayAdapter.getPosition(position)
                spinnersexadmin.setSelection(spinnerPosition)
                user?.photoURL?.let {
                    val storage = Firebase.storage
                    val storageRef = storage.reference
                    var islandRef = storageRef.child("newUserPhotos/${it}")

                    val ONE_MEGABYTE: Long = 10024*1024
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val inputStream = it.inputStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageViewProfileAdmin.setImageBitmap(bitmap)

                    }
                }

            }



        binding.buttonSalveProfile.setOnClickListener{
            newUsers.newUserField(spinnersexadmin.selectedItem.toString(), "sexualidade")
            newUsers.newUserField(binding.editTextAdminFirstName.text.toString(), "FirstName")
            newUsers.newUserField(binding.editTextAdminLastName.text.toString(), "LastName")
            newUsers.newUserField(binding.editTextAdminDate.text.toString(), "dataNascimento")
            newUsers.newUserField(binding.editTextAdminPhone.text.toString(), "numeroTelemovel")
        }
    }

    private fun deletePreferences() {
        requireContext().PREF_CODE = "apagar"
        requireContext().role_user = "apagar"

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ProfileFragment.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            ///val imageBitmap = data?.extras?.get("data") as Bitmap
            BitmapFactory.decodeFile(currentPhotoPath).apply {
                binding.imageViewProfileAdmin.setImageBitmap(this)
                uploadFile {
                    if (it != null) {
                      //  Administrador.postField(it, "photoFilename")
                        newUsers.newUserField(it,"photoURL")
                    }
                }
            }
        }else{
            findNavController().popBackStack()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(
                requireActivity().packageManager)?.also {
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
                        requireContext(),
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
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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
        const val TAG = "ProfileFragment"
    }
}