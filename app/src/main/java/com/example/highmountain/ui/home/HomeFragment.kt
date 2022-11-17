package com.example.highmountain.ui.home

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.databinding.FragmentHomeBinding
import com.example.highmountain.databinding.RowclientBinding
import com.example.highmountain.ui.LoadingDialog
import com.example.highmountain.ui.models.Clientes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var clientes = arrayListOf<Clientes>()
    var adapter = ClientesAdapter()

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
        val loading = LoadingDialog(requireActivity())
        loading.startLoading()
        Handler().postDelayed({
            if(loading.isOnline(requireContext())){
                loading.isDismiss()
                adapter.notifyDataSetChanged()
            }else{
                loading.isDismiss()
                Toast.makeText(requireContext(),"Sem ligação a internet", Toast.LENGTH_SHORT).show()
                clientes.clear()
                adapter.notifyDataSetChanged()
            }
                              }, 5000) //possivel alterar tempo
        val db = Firebase.firestore
        db.collection("clientes")
            .addSnapshotListener{value , error ->
                clientes.clear()
                for (doc in value?.documents!!){
                    clientes.add(Clientes.fromDoc(doc))
                }
                adapter.notifyDataSetChanged()
            }
        binding.recyclerViewClientes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewClientes.adapter = adapter
        binding.recyclerViewClientes.itemAnimator = DefaultItemAnimator()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ClientesAdapter : RecyclerView.Adapter<ClientesAdapter.ViewHolder>(){

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

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var itemCliente = clientes[position]
            holder.apply {
                val storage = Firebase.storage
                var storageRef = storage.reference
                var islandRef = storageRef.child("adminPhotos/${itemCliente.urlPhoto}")

                val ONE_MEGABYTE: Long = 10024 * 1024
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                            val inputStream = it.inputStream()
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            imageViewPhoto.setImageBitmap(bitmap)
                        }.addOnFailureListener {
                            // Handle any errors
                            Log.d(TAG, it.toString())
                        }


                        textviewNome.text = itemCliente.nomeCliente
                        textviewEmail.text = itemCliente.emailCliente
                    }
                }

        override fun getItemCount(): Int {
            return clientes.size
        }

    }
    companion object{
        const val TAG = "HomeFragment"
    }


}