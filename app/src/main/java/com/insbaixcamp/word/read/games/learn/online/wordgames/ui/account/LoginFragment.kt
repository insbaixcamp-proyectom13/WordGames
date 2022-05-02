package com.insbaixcamp.word.read.games.learn.online.wordgames.ui.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.insbaixcamp.word.read.games.learn.online.wordgames.MainActivity
import com.insbaixcamp.word.read.games.learn.online.wordgames.R
import com.insbaixcamp.word.read.games.learn.online.wordgames.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var _binding: FragmentLoginBinding
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        root = _binding.root

        var ivMale = _binding.ivMale
        var ivFemale = _binding.ivFemale
        var ivGoogleSync = _binding.ivCreateGoogle
        var ivCreateAnonymous = _binding.ivCreateAnonymous
        var ivReturnSignup = _binding.ivReturnSignup

        ivFemale.setOnClickListener {
            ivMale.setImageResource(R.mipmap.perfil_male_empty)
            ivFemale.setImageResource(R.mipmap.profile_female)
        }

        ivMale.setOnClickListener {
            ivFemale.setImageResource(R.mipmap.profile_female_empty)
            ivMale.setImageResource(R.mipmap.perfil_male)
        }

        ivGoogleSync.setOnClickListener {
            //Guardar datos en firebase he inicar session con las credenciales pedidas
        }

        ivCreateAnonymous.setOnClickListener {
            //Guardar datos en shared preferences y firebase con el UID de usuario anonimo
            root!!.findNavController().navigate(R.id.navigation_test)
        }

        ivReturnSignup.setOnClickListener {
            root!!.findNavController().navigate(R.id.signupFragment)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        var s = activity as MainActivity
        s.floatButtonHide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }
}