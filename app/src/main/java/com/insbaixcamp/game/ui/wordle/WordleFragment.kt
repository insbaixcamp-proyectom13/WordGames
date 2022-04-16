package com.insbaixcamp.game.ui.wordle

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.insbaixcamp.game.R
import com.insbaixcamp.game.databinding.FragmentNotificationsBinding
import com.insbaixcamp.game.databinding.FragmentWordleBinding
import com.insbaixcamp.game.ui.notifications.NotificationsViewModel

class WordleFragment : Fragment() {
    private var _binding: FragmentWordleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var input = arrayOf(charArrayOf())
    private var word = charArrayOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentWordleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}