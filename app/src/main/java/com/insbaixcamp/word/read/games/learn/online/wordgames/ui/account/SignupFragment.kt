package com.insbaixcamp.word.read.games.learn.online.wordgames.ui.account

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.navigation.findNavController
import com.insbaixcamp.word.read.games.learn.online.wordgames.MainActivity
import com.insbaixcamp.word.read.games.learn.online.wordgames.R
import com.insbaixcamp.word.read.games.learn.online.wordgames.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private lateinit var viewModel: SignupViewModel
    private lateinit var _binding: FragmentSignupBinding
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        root = _binding.root
        shakeHand()

        _binding.cvCreateAccount.setOnClickListener {
            root!!.findNavController().navigate(R.id.loginFragment)
        }

        _binding.cvLogin.setOnClickListener {
            //Pedir credenciales de google e iniciar session con ellas
        }

        return root
    }

    private fun shakeHand() {
        val image = _binding.ivHand
        val animatorSet: AnimatorSet = AnimatorSet()
        image.pivotY = 400f
        image.pivotX = 100f
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(image, "rotation", 0f, 10f, 0f, -10f, 0f)
        )
        animatorSet.childAnimations.forEach {
            val animator = it as ObjectAnimator
            animator.repeatCount = 900
        }
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.duration = 3500
        animatorSet.start()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
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