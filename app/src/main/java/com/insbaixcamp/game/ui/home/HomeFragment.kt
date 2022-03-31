package com.insbaixcamp.game.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insbaixcamp.game.R
import com.insbaixcamp.game.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val RC_SIGN_IN = 1
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        auth = Firebase.auth

            _binding!!.button.setOnClickListener(View.OnClickListener {
                var lastSignIn = GoogleSignIn.getLastSignedInAccount(requireContext())
                if (lastSignIn != null) {
                    auth.currentUser!!.unlink(GoogleAuthProvider.getCredential(lastSignIn.idToken, null).provider)
//                    auth.currentUser!!.reload()
                } else {
                    signIn()
                }
            })

        return root
    }

    private fun signIn(){
        Log.d("TAG", "Email sent.")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext().applicationContext, gso)
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

                linkWithCredential(auth.currentUser, credential)

            } catch (e : ApiException) {
                Log.d("TAG", e.stackTraceToString())
            }

        }
    }

    private fun linkWithCredential(currentUser: FirebaseUser?, credential: AuthCredential) {
        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Log.d("TAG","linkWithCredential:success")
                else
                    Log.w("TAG","linkWithCredential:failure", task.exception)
            }
    }

}