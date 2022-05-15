package com.insbaixcamp.game.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.insbaixcamp.game.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
// ...


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = FirebaseDatabase.getInstance().getReference("users")

        database.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                collectPoints(snapshot.getValue() as Map<String?, Any?>)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("INFO", "Failed to read value.", error.toException())
            }
        })

        return root
    }

    private fun collectPoints(users: Map<String?, Any?>) {
        val points: ArrayList<Long?> = ArrayList()

        //iterate through each user, ignoring their UID
        for ((_, value) in users) {

            //Get user map
            val singleUser = value as Map<*, *>
            //Get phone field and append to list
            points.add(singleUser["totalPoints"] as Long?)
            Log.i("INFO", "Value is: " + points)
        }

    }

}

