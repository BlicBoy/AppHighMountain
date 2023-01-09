package com.example.highmountain.ui.notifications

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.highmountain.R
import com.example.highmountain.databinding.FragmentAlterarPasswordBinding
import com.example.highmountain.ui.utilizador.ClienteActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AlterarPasswordFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    lateinit var credential: AuthCredential
    private var _binding: FragmentAlterarPasswordBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlterarPasswordBinding.inflate(inflater, container, false)
        return (binding.root)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth

        binding.buttonGuardarNewPassword.setOnClickListener {
            var password = binding.editTextTextEditPasswordAdmin.text
            var passwordRepeat = binding.editTextTextEditRepeatPasswordAdmin.text
            var oldPassword = binding.editTextTextPasswordOldAdmin.text

            if(password.isNullOrBlank() || passwordRepeat.isNullOrBlank() || oldPassword.isNullOrBlank()){
                Toast.makeText(requireContext(), "As passwords estão vazias!", Toast.LENGTH_SHORT).show()
            }else{
                credential = EmailAuthProvider.getCredential(auth.currentUser?.email.toString(), oldPassword.toString())
                if(password.toString() != passwordRepeat.toString()){
                    Toast.makeText(requireContext(), "As passwords não são iguais!", Toast.LENGTH_SHORT).show()
                }else{
                    auth.currentUser?.reauthenticate(credential)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                auth.currentUser?.updatePassword(password.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(requireContext(), "Password Editada com sucesso", Toast.LENGTH_LONG).show()
                                            findNavController().popBackStack()
                                        }else{
                                            Toast.makeText(requireContext(), "Ocurreu um erro", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }else{
                                Toast.makeText(requireContext(), "Password Antiga Errada", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }


}