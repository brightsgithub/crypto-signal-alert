package com.owusu.cryptosignalalert.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.mappers.PriceTargetDirectionUI
import com.owusu.cryptosignalalert.models.PriceTargetUI

class AlertListAdapter : RecyclerView.Adapter<AlertListAdapter.ViewHolder>() {

    private var mList: List<PriceTargetUI> = emptyList()

    fun setData(data: List<PriceTargetUI>) {
        mList = data
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert_row, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        loadImage(holder.logo, item.image, holder.itemView.context)
        holder.coinName.text = "Coin name: " + item.name
        holder.lastUpdated.text = "Last updated: " + item.lastUpdated
        holder.hasTargetBenHit.text = "Has target been hit: " + item.hasPriceTargetBeenHit.toString()
        holder.currentPrice.text = "Current price: " + item.currentPrice.toString()
        holder.priceDirection.text = "Price direction: " + item.priceTargetDirection.toString()
        setPriceTarget(holder, item)
    }

    private fun setPriceTarget(holder: ViewHolder, item: PriceTargetUI) {
        holder.priceTarget.text = "Price target: " + item.userPriceTarget
        val txtColor = when (item.priceTargetDirection) {
            PriceTargetDirectionUI.ABOVE -> { R.color.btn_green }
            PriceTargetDirectionUI.BELOW -> { R.color.yellow }
            PriceTargetDirectionUI.NOT_SET -> { R.color.white }
        }
        val ctx = holder.itemView.context
        holder.priceTarget.setTextColor(ContextCompat.getColor(ctx, txtColor))
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {


        val logo: ImageView = itemView.findViewById(R.id.logo)
        val coinName: TextView = itemView.findViewById(R.id.coin_name)
        val lastUpdated: TextView = itemView.findViewById(R.id.last_updated)
        val hasTargetBenHit: TextView = itemView.findViewById(R.id.has_target_been_hit)
        val currentPrice: TextView = itemView.findViewById(R.id.current_price)
        val priceDirection: TextView = itemView.findViewById(R.id.price_direction)
        val priceTarget: TextView = itemView.findViewById(R.id.price_target)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun loadImage(view: ImageView, url: String?, context: Context) {
        Glide.with(context)
            .load(url)
            .into(view)
    }
}