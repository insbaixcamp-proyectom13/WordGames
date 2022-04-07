package com.insbaixcamp.game.ui.dashboard
import com.insbaixcamp.game.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.insbaixcamp.game.databinding.FragmentDashboardBinding
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tv: TextView = binding.textDashboard
        val imput = resources.openRawResource(R.raw.english)
        val reader = imput.bufferedReader()
        val words = ArrayList<String>()
        var line : String? = ""
        var parts : List<String>
        line = reader.readLine()
        while (line != null) {
            if (line.contains("/")){
                parts = line.split("/")
                words.add(parts.elementAt(0))
            } else {
                words.add(line)
            }
            line = reader.readLine()
        }



        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


