package com.insbaixcamp.game.ui.notifications

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.insbaixcamp.game.R
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView

class AdapterRanking(private val listaProfiles: MutableList<User>, context: Context?, game: Int) :
    RecyclerView.Adapter<AdapterRanking.ViewHolder>() {
    var holder: ViewHolder? = null
    var context: Context? = null
    var game: Int? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNombre: TextView
        var tvPuntos: TextView
        var tvPosicion: TextView
        var ivImage4: ImageView

        // TextView tvName;
        init {
            tvNombre = itemView.findViewById(R.id.tvNombre)
            tvPuntos = itemView.findViewById(R.id.tvPuntos)
            tvPosicion = itemView.findViewById(R.id.tvPosicion)
            ivImage4 = itemView.findViewById(R.id.ivImage4)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.holder = holder
        val (username, profileImage, _, wordlePoints,wordSearchPoints) = listaProfiles[position]
        holder.tvNombre.text = username
        if(game==1) holder.tvPuntos.text = wordlePoints
        else holder.tvPuntos.text = wordSearchPoints
        holder.ivImage4.setImageResource(
            context!!.resources
                .getIdentifier(profileImage,"mipmap", context!!.packageName))
        holder.tvPosicion.setText((position+4).toString())
    }

    override fun getItemCount(): Int {
        return listaProfiles.size
    }

    init {
        this.context = context
        this.game = game
    }
}