package com.example.highmountain.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentPercursoAddBinding


class PercursoAddFragment : Fragment() {

    private var _binding : FragmentPercursoAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPercursoAddBinding.inflate(inflater,  container, false)
        return binding.root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}