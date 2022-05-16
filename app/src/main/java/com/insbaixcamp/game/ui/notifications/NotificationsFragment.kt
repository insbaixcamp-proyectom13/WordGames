package com.insbaixcamp.game.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.insbaixcamp.game.databinding.FragmentNotificationsBinding
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    var root : View? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        root = binding.root
        var bRankingWordle = (root as ConstraintLayout).bRankingWordle
        var bRankingWrodSerch = (root as ConstraintLayout).bRankingWrodSerch

        cargarDatos(1)
        bRankingWordle.setOnClickListener {
            cargarDatos(1)
            }
        bRankingWrodSerch.setOnClickListener {
            cargarDatos(2)
        }

        return root!!
    }

    fun cargarDatos(game:Int){
        var listProfile = mutableListOf<User>()
        var db =  FirebaseDatabase.getInstance()
        var tvName1 = root!!.tvName1
        var tvName2 = root!!.tvName2
        var tvName3 = root!!.tvName3
        var tvPoints1 = root!!.tvPoints1
        var tvPoints2 = root!!.tvPoints2
        var tvPoints3 = root!!.tvPoints3
        var ivImage1 = root!!.ivImage1
        var ivImage2 = root!!.ivImage2
        var ivImage3 = root!!.ivImage3

        db.getReference("users").get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                for(user in task.result.children){
                    task.result.value
                    var usuario: User? = user.getValue(User::class.java)
                    listProfile.add(usuario!!)
                }
                if(game == 1) {
                    listProfile.sortByDescending { it.wordlePoints!!.toInt() }
                    tvPoints1.setText(listProfile.get(0).wordlePoints)
                    tvPoints2.setText(listProfile.get(1).wordlePoints)
                    tvPoints3.setText(listProfile.get(2).wordlePoints)
                }
                else{
                    listProfile.sortByDescending { it.wordSearchPoints!!.toInt() }
                    tvPoints1.setText(listProfile.get(0).wordSearchPoints)
                    tvPoints2.setText(listProfile.get(1).wordSearchPoints)
                    tvPoints3.setText(listProfile.get(2).wordSearchPoints)
                }
                tvName1.setText(listProfile.get(0).username)
                tvName2.setText(listProfile.get(1).username)
                tvName3.setText(listProfile.get(2).username)
                ivImage1.setImageResource(requireContext().resources
                        .getIdentifier(listProfile.get(0).profileImage,"mipmap", requireContext().packageName))
                ivImage2.setImageResource(requireContext().resources
                    .getIdentifier(listProfile.get(1).profileImage,"mipmap", requireContext().packageName))
                ivImage3.setImageResource(requireContext().resources
                    .getIdentifier(listProfile.get(2).profileImage,"mipmap", requireContext().packageName))

                listProfile.removeAt(0)
                listProfile.removeAt(0)
                listProfile.removeAt(0)
                val rvRanking = root?.rvRanking
                var adapterRanking = AdapterRanking(listProfile,context,game)
                rvRanking?.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                rvRanking?.adapter = adapterRanking
            }
        }
    }
}


