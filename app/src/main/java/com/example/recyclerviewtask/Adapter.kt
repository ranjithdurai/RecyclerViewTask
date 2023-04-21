package com.example.recyclerviewtask

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class Adapter(
    private val mylist: List<Responces>,
  private  val context:Context
) :
    RecyclerView.Adapter<Adapter.MyviewHolder>() {

    inner class MyviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val email: TextView = view.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_design, parent, false)
        return MyviewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        val currentitems = mylist[position]
        holder.name.text = currentitems.name.uppercase()
        holder.email.text = currentitems.email.lowercase()
        holder.itemView.setOnClickListener {
            Log.e("RANJITH", "onBindViewHolder:${currentitems.id} ", )
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("id", currentitems.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return this.mylist.size
    }
}

