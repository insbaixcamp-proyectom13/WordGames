package com.insbaixcamp.word.read.games.learn.online.wordgames.ui.account

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.credentials.IdentityProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.insbaixcamp.game.R
import com.insbaixcamp.game.databinding.FragmentSignupBinding
import com.insbaixcamp.word.read.games.learn.online.wordgames.MainActivity
import com.insbaixcamp.word.read.games.learn.online.wordgames.firebase.data.User

class SignupFragment : Fragment() {
    private lateinit var viewModel: SignupViewModel
    private lateinit var _binding: FragmentSignupBinding
    private lateinit var root: View
    private val RC_SIGN_IN = 1

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

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.id.defa))

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString())
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(requireContext().applicationContext, gso)
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN);
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG", account.email.toString())


                //Credenciales para vincular cuenta
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        checkIfNewUser(Firebase.auth.currentUser)
                    }
                }

            } catch (e : ApiException) {
                Log.d("TAG", e.stackTraceToString())
            }

        }
    }

    private fun checkIfNewUser(currentUser: FirebaseUser?) {
        Firebase.database.reference.child("users").child(currentUser!!.uid).get()
            .addOnCompleteListener {

                if (it.isSuccessful){
                    if (!it.result.exists()){
                        Toast.makeText(context, "Unknown credentials!", Toast.LENGTH_LONG).show()
                        Firebase.auth.currentUser!!.delete()
                    } else {
                        Firebase.database.reference.child("users").child(Firebase.auth.currentUser!!.uid)
                            .get().addOnSuccessListener { userResult ->
                                val user = userResult.getValue(User::class.java)
                                (activity as MainActivity).updateUI(user)
                                (activity as MainActivity).floatButtonClicked(_binding.root);
                            }

                    }

                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Unknown credentials!", Toast.LENGTH_LONG).show()
                Firebase.auth.currentUser!!.delete()
            }
    }
}