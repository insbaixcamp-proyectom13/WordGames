package com.insbaixcamp.word.read.games.learn.online.wordgames.ui.account

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.insbaixcamp.word.read.games.learn.online.wordgames.MainActivity
import com.insbaixcamp.word.read.games.learn.online.wordgames.R
import com.insbaixcamp.word.read.games.learn.online.wordgames.databinding.FragmentLoginBinding
import com.insbaixcamp.word.read.games.learn.online.wordgames.firebase.data.User
import com.insbaixcamp.word.read.games.learn.online.wordgames.firebase.data.WordSearchProfile
import com.insbaixcamp.word.read.games.learn.online.wordgames.firebase.data.WordleProfile

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var _binding: FragmentLoginBinding
    private lateinit var root: View
    private lateinit var profile: String
    private val RC_SIGN_IN = 1
    private lateinit var username: String
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        root = _binding.root
        profile = ""
        database = Firebase.database.reference

        var ivMale = _binding.ivMale
        var ivFemale = _binding.ivFemale
        var ivGoogleSync = _binding.ivCreateGoogle
        var ivCreateAnonymous = _binding.ivCreateAnonymous
        var ivReturnSignup = _binding.ivReturnSignup
        var usernameEdiText = _binding.textInputLayout.editText

        ivFemale.setOnClickListener {
            ivMale.setImageResource(R.mipmap.perfil_male_empty)
            ivFemale.setImageResource(R.mipmap.profile_female)
            profile = "profile_female"
        }

        ivMale.setOnClickListener {
            ivFemale.setImageResource(R.mipmap.profile_female_empty)
            ivMale.setImageResource(R.mipmap.perfil_male)
            profile = "perfil_male"
        }

        ivGoogleSync.setOnClickListener {
            //Guardar datos en firebase he inicar session con las credenciales pedidas
            username = usernameEdiText!!.text.toString().trim()
            if (checkProfile(username)){
                createLinkedUser(username)
            }
        }

        ivCreateAnonymous.setOnClickListener {
            //Guardar datos en shared preferences y firebase con el UID de usuario anonimo
            username = usernameEdiText!!.text.toString().trim()
            if (checkProfile(username)){
                createAnonymousUser(username)
            }
        }

        ivReturnSignup.setOnClickListener {
            root!!.findNavController().navigate(R.id.signupFragment)
        }

        return root
    }

    private fun createLinkedUser(trim: String) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext().applicationContext, gso)
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private fun checkProfile(trim: String): Boolean {
        return when {
            trim.length !in 5..20 -> {
                Toast.makeText(context, "Check profile data! Minimum of 5 characters for username!", Toast.LENGTH_LONG)
                    .show()
                false
            }
            profile == "" -> {
                Toast.makeText(context, "Select your image profile!", Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun createAnonymousUser(user: String) {
        Firebase.auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("TAG", "signInAnonymously:success")
                val userFirebase = Firebase.auth.currentUser
                val user = User(user, profile)

                createUser(userFirebase!!.uid, user)
//                saveLocalUser()

            }
        }
    }

    private fun createUser(uid: String, user: User) {

        var s = activity as MainActivity
        s.updateUI(user)
        database.child("users").child(uid).setValue(user).addOnSuccessListener {
            val wordleProf = WordleProfile()
            database.child("wordle").child("profiles").child(uid).setValue(wordleProf).addOnSuccessListener {
                val sopaLetras = WordSearchProfile()
                database.child("wordsearch").child("profiles").child(uid).setValue(sopaLetras).addOnSuccessListener {

                    //Añadiriamos el perfil tambien a la tabla de scrabble o juegos creados
                    root!!.findNavController().navigate(R.id.navigation_test)
                }
            }
        }
            .addOnFailureListener {
                Log.i("error", "error firebase")
            }

    }

    private fun linkWithCredential(credential: AuthCredential) {
        //
        Firebase.auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("TAG", "signInAnonymously:success")
                val userFirebase = Firebase.auth.currentUser
                val user = User(username, profile)


//                saveLocalUser()


                Firebase.auth.currentUser!!.linkWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "linkWithCredential:success")
                            createUser(userFirebase!!.uid, user)

                        }else {
                            Firebase.auth.currentUser!!.delete()
                            Toast.makeText(
                                context,
                                "Credentials already in use!",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.w("TAG", "linkWithCredential:failure", task.exception)
                        }
                    }
            }
        }

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

                linkWithCredential(credential)

            } catch (e : ApiException) {
                Log.d("TAG", e.stackTraceToString())
            }

        }
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