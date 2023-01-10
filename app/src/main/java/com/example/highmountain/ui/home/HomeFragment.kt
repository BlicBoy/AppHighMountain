package com.example.highmountain.ui.home

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.databinding.FragmentHomeBinding
import com.example.highmountain.databinding.RowclientBinding
import com.example.highmountain.ui.LoadingDialog
import com.example.highmountain.ui.models.newUsers
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var newUsersList = arrayListOf<newUsers>()
    var adapter = newUsersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //loading
      // val loading = LoadingDialog(requireActivity())
      // loading.startLoading()
      // Handler().postDelayed({
      //     if(loading.isOnline(requireContext())){
      //         loading.isDismiss()
      //         adapter.notifyDataSetChanged()
      //     }else{
      //         loading.isDismiss()
      //         Toast.makeText(requireContext(),"Sem ligação a internet", Toast.LENGTH_SHORT).show()
      //         newUsersList.clear()
      //         adapter.notifyDataSetChanged()
      //     }
      //                       }, 5000) //possivel alterar tempo
        val db = Firebase.firestore
        db.collection("newUsers")
            .addSnapshotListener{value , error ->
                newUsersList.clear()
                for (doc in value?.documents!!){
                    if(doc.getString("role") == "Cliente")
                        newUsersList.add(newUsers.fromDoc(doc))
                }
                adapter.notifyDataSetChanged()
            }
        binding.recyclerViewClientes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewClientes.adapter = adapter
        binding.recyclerViewClientes.itemAnimator = DefaultItemAnimator()


        if(newUsersList.size <= 0){
            Toast.makeText(requireContext(),"Nenhum cliente registrado!", Toast.LENGTH_SHORT).show()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class newUsersAdapter : RecyclerView.Adapter<newUsersAdapter.ViewHolder>(){

        inner class ViewHolder(binding: RowclientBinding) : RecyclerView.ViewHolder(binding.root) {
            val textviewNome :TextView = binding.textViewNomeClienteRow
            val textviewEmail : TextView = binding.textViewClienteEmailRow
            val imageViewPhoto : ImageView = binding.imageViewClienteRow
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowclientBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
                )
        }

        @SuppressLint("SuspiciousIndentation")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var itemCliente = newUsersList[position]


            holder.apply {
                val storage = Firebase.storage
                val storageRef = storage.reference
                var islandRef = storageRef.child("newUserPhotos/${itemCliente.photoURL}")

                val ONE_MEGABYTE: Long = 10024*1024
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                    val inputStream = it.inputStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageViewPhoto.setImageBitmap(bitmap)
                }
                        textviewNome.text = itemCliente.FirstName
                        textviewEmail.text = itemCliente.role
                    }
                }

        override fun getItemCount(): Int {
            return newUsersList.size
        }

    }
    companion object{
        const val TAG = "HomeFragment"
    }


}