package com.insbaixcamp.game.ui.wordle

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import com.insbaixcamp.game.databinding.FragmentWordleBinding
import com.insbaixcamp.game.ui.notifications.NotificationsViewModel

class WordleFragment : Fragment(), OnClickListener {
    private var _binding: FragmentWordleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var input = arrayOf(charArrayOf())
    private var word = charArrayOf()
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentWordleBinding.inflate(inflater, container, false)
        root = binding.root
        var container1 = binding.incKeyboard.linearLayout as ViewGroup
        var container2 = binding.incKeyboard.linearLayout2 as ViewGroup
        var container3 = binding.incKeyboard.linearLayout3 as ViewGroup

        Log.i("butons", container1.childCount.toString())
        Log.i("butons", container2.childCount.toString())
        Log.i("butons", container3.childCount.toString())

        for (i in 0 until container1.childCount){
            var v = container1.getChildAt(i)
            v.setOnClickListener(this)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        var butn = p0 as TextView
        Log.i("click", butn.text.toString())

//        if (p0 == root.findViewById(R.id.inc_keyboard)){
//            var layout = p0 as LinearLayout?
//            if (layout != null) {
//
//            }
//        }
    }

}