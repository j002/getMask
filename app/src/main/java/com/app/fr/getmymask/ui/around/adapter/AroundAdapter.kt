package com.app.fr.getmymask.ui.around.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.fr.getmymask.R
import com.app.fr.getmymask.api.models.ResponseMasks
import kotlinx.android.synthetic.main.rv_mask_around.view.*

class AroundAdapter(var items: List<ResponseMasks>, val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {
    private var listener: AroundAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.rv_mask_around, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_postal.text = items[position].quantity.toString()
        holder.btn_itinerary.setOnClickListener {
            listener!!.onShowItineraryPerfomed(
                items[position].latitude.toString(),
                items[position].longitude.toString()
            )
        }

    }

    fun setList(list: List<ResponseMasks>) {
        items = list
        notifyDataSetChanged()
    }

    fun setListener(listener: AroundAdapterListener) {

        this.listener = listener
    }

    interface AroundAdapterListener {
        fun onShowItineraryPerfomed(latitude: String, longitude: String)
    }
}

class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val txt_postal = view.tv_number_mask!!
    val btn_itinerary = view.btn_show_itineraire!!
}