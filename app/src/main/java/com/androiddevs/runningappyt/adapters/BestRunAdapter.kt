package com.androiddevs.runningappyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.TrackingUtility
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class BestRunAdapter:RecyclerView.Adapter<BestRunAdapter.BestRunViewHolder>() {

    inner class BestRunViewHolder(itemview:View):RecyclerView.ViewHolder(itemview)

    private val differCallBack= object : DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRunViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_run,parent,false)
        return BestRunViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestRunViewHolder, position: Int) {
        val run =differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(ivRunImage)

            val calender= Calendar.getInstance().apply {
                timeInMillis=run.timestamp
            }
            val dateFormat= SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text=dateFormat.format(calender.time)


            val avgSpeed="${run.avgSpeedInKMH}km/h"
            tvAvgSpeed.text=avgSpeed

            val distanceInKm="${run.distanceInMetres/1000f}km"
            tvDistance.text=distanceInKm

            val runTime= TrackingUtility.getFormattedStopwatchTime(run.timeInMillis)
            tvTime.text=runTime

            val caloriesBurnt="${run.caloriesBurnt}kcal"
            tvCalories.text=caloriesBurnt
        }
    }
}