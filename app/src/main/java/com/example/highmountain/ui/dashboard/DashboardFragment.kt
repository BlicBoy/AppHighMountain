package com.example.highmountain.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentDashboardBinding
import com.example.highmountain.databinding.RowPercursoBinding
import com.example.highmountain.ui.LoadingDialog
import com.example.highmountain.ui.models.Percursos
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.row_percurso.*

class DashboardFragment : Fragment() {



    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonListarPercurso.setOnClickListener {


            //Só irá passar para a outra pagina caso tenho ligação a internet!
            if(LoadingDialog(requireActivity()).isOnline(requireContext())){
                findNavController().navigate(R.id.action_navigation_dashboard_to_listPercursoFragment)
            }else{
                Toast.makeText(requireActivity(), "Não é possível executar esta função sem ligação a internet", Toast.LENGTH_LONG).show()
            }
        }
        binding.imageButtonfazerMedicoes.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_medicoesFragment)

        }
    }



}




