package com.insbaixcamp.game

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insbaixcamp.game.databinding.ActivityMainBinding
import com.insbaixcamp.game.utilities.Diccionario
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser ? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        Diccionario.inicialitzar(resources, getShared(), packageName)

        //Adding custom top appBar layout
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        var infla = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = infla.inflate(R.layout.custom_appbar, null) as View
        supportActionBar!!.customView = view

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Customizing bottom navigaton bar
        val navView: BottomNavigationView = binding.navView
        val menuView = binding.navView.getChildAt(0) as BottomNavigationMenuView
        val iconView = menuView.getChildAt(0).findViewById<View>(R.id.navigation_home) as View
        iconView.setLeftTopRightBottom(-14, 0, 0, 0)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_wordle
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        setLanguage("ru")
    }

    private fun setLanguage(upLanguage: String) {
        val config = resources.configuration
        val locale = Locale(upLanguage)
        Locale.setDefault(locale)

        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    //    private fun loadDictionary(lang:String) {
//        switchLanguage(lang)
//        dictionary = mutableListOf()
//        val input = activity!!.resources.openRawResource(activity!!.resources.getIdentifier("$lang.txt", "raw", packageName))
//        val reader = input.bufferedReader()
//        var line : String? = ""
//        var parts : List<String>
//
//        line = reader.readLine()
//        while (line != null) {
//            if (line.contains("/")){
//                parts = line.split("/")
//                dictionary!!.add(parts.elementAt(0))
//            } else {
//                dictionary!!.add(line)
//            }
//            Log.i("paraula", dictionary!![dictionary!!.size-1]);
//            line = reader.readLine()
//        }
//
//    }

    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser ?: signInAnonymously()
    }

    fun getShared(): SharedPreferences{
        return this.getSharedPreferences(
            getString(R.string.shared_preferences_file),
            Context.MODE_PRIVATE)
    }

    private fun signInAnonymously() : FirebaseUser? {
        auth.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInAnonymously:success")
                currentUser = auth.currentUser!!
//                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInAnonymously:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
//                updateUI(null)
                return@addOnCompleteListener null!!
            }
        }

        return currentUser
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    private fun updateUI(user: FirebaseUser?) {
        TODO("Not yet implemented")
    }

}