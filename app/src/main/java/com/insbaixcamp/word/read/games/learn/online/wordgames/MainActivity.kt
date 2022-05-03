package com.insbaixcamp.word.read.games.learn.online.wordgames

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insbaixcamp.word.read.games.learn.online.wordgames.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView

        binding.container.systemUiVisibility =  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        var bar = supportActionBar as ActionBar
        bar.displayOptions = 0
//        bar.elevation = 0f
        bar.setDisplayShowTitleEnabled(false)
        bar.setBackgroundDrawable(ColorDrawable(getColor(R.color.citrine_white)))
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        var infla = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = infla.inflate(R.layout.user_appbar, null) as View
        supportActionBar!!.customView = view

//        val customView = layoutInflater.inflate(R.layout.user_appbar, binding.root)
        var parent = view.parent as androidx.appcompat.widget.Toolbar
//        parent.setPadding(0,0,0,0)
        parent.setContentInsetsAbsolute(0, 0)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_notifications, R.id.signupFragment, R.id.navigation_test, R.id.loginFragment, R.id.splashscreenFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    public fun hideBottomBar() {
        navView.visibility = View.GONE
    }

    override fun onBackPressed() {

    }

    fun floatButtonUnselect(){
        var fab = binding.fab
        var drawable = fab.drawable
        DrawableCompat.setTint(drawable, getColor(R.color.pharlap))
    }

    fun floatButtonHide(){
        var fab = binding.fab
        fab.visibility = View.GONE
    }

    fun floatButtonClicked(v: View){

        var s = navView.menu
        s.performIdentifierAction(R.id.navigation_test, 0)
        var fab = binding.fab
        var drawable = fab.drawable
        DrawableCompat.setTint(drawable, getColor(R.color.wine_berry))
        fab.visibility = View.VISIBLE
    }

    fun spinFloatButton(){
        val image2 = binding.fab

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

    fun showBottomBar() {
        navView.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    fun updateUI(user: FirebaseUser?){


    }
}