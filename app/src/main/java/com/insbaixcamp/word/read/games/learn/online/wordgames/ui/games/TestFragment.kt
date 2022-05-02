package com.insbaixcamp.word.read.games.learn.online.wordgames.ui.games

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.insbaixcamp.word.read.games.learn.online.wordgames.MainActivity
import com.insbaixcamp.word.read.games.learn.online.wordgames.databinding.TestFragmentBinding

class TestFragment : Fragment() {

    private lateinit var binding: TestFragmentBinding
    private lateinit var root: View

    companion object {
        fun newInstance() = TestFragment()
    }

    private lateinit var viewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TestFragmentBinding.inflate(inflater, container, false)
        root = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        var s = activity as MainActivity
        s.showBottomBar()
        s.floatButtonClicked(root)
    }

    override fun onStop() {
        super.onStop()
        var s = activity as MainActivity
        s.floatButtonUnselect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}