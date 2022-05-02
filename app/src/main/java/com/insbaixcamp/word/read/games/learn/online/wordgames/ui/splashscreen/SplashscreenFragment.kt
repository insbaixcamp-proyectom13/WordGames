package com.insbaixcamp.word.read.games.learn.online.wordgames.ui.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.insbaixcamp.word.read.games.learn.online.wordgames.MainActivity
import com.insbaixcamp.word.read.games.learn.online.wordgames.R
import com.insbaixcamp.word.read.games.learn.online.wordgames.databinding.FragmentSplashscreenBinding
import com.insbaixcamp.word.read.games.learn.online.wordgames.ui.account.LoginFragment
import com.insbaixcamp.word.read.games.learn.online.wordgames.ui.account.SignupFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashscreenFragment : Fragment() {

    private var _binding: FragmentSplashscreenBinding? = null
    private var root: View? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashscreenBinding.inflate(inflater, container, false)
        root = binding.root
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        rollDice()
        activity!!.lifecycleScope.launch {
            delay(5000)
            root!!.findNavController().navigate(R.id.signupFragment)
        }
        return root!!
    }

    private fun rollDice() {
        val image2 = binding.imageView2
        val image = binding.imageView

        val animatorSet: AnimatorSet = AnimatorSet()
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(image, "scaleX", 1f, 0.8f, 1f),
            ObjectAnimator.ofFloat(image, "scaleY", 1f, 0.8f, 1f)
        )

        animatorSet.interpolator = LinearInterpolator()
        animatorSet.duration = 5000

        animatorSet.childAnimations.forEach {
            val animator = it as ObjectAnimator
            animator.repeatCount = ObjectAnimator.INFINITE
        }
        animatorSet.start()

        val animatorSet2: AnimatorSet = AnimatorSet()
        animatorSet2.interpolator = LinearInterpolator()
        animatorSet2.playTogether(
            ObjectAnimator.ofFloat(image2, "translationX", 0f, 400f, -400f, 0f),
            ObjectAnimator.ofFloat(image2, "rotation", 0f, 100f, -100f, 0f)
        )

        animatorSet2.duration = 2500

        animatorSet2.childAnimations.forEach {
            val animator = it as ObjectAnimator
            animator.repeatCount = ObjectAnimator.INFINITE
        }
        animatorSet2.start()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        var s = activity as MainActivity
        s.hideBottomBar()
        s.floatButtonHide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }
}