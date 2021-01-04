package com.example.udacity_project_two.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.udacity_project_two.Asteroid
import com.example.udacity_project_two.R
import com.example.udacity_project_two.databinding.ListItemNeoBinding

class RecyclerAdapter(private val context: Context, private val listener: (Asteroid) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var asteroidList = listOf<Asteroid>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemBinding: ListItemNeoBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.list_item_neo,
                parent,
                false
            )

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = asteroidList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = asteroidList[position]
        holder.itemBinding.asteroid = asteroid
        holder.itemView.setOnClickListener { listener(asteroid) }
    }

    fun setAsteroidList(newList: List<Asteroid>) {
        asteroidList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: ListItemNeoBinding) : RecyclerView.ViewHolder(view.root) {
        val itemBinding: ListItemNeoBinding = view
    }
}