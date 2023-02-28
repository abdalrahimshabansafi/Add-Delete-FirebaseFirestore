package com.example.addinformation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonsAdapter(val personsList:ArrayList<Persons>):RecyclerView.Adapter<PersonsAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val pName: TextView = itemView.findViewById(R.id.namePerson)
        val pAddress: TextView = itemView.findViewById(R.id.locationPerson)
        val pNumber: TextView = itemView.findViewById(R.id.numberPerson)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonsAdapter.MyViewHolder {
        val  itemView = LayoutInflater.from(parent.context).inflate(R.layout.one_person, parent ,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PersonsAdapter.MyViewHolder, position: Int) {
        holder.pName.text = personsList[position].name
        holder.pAddress.text = personsList[position].address
        holder.pNumber.text = personsList[position].number
    }

    override fun getItemCount(): Int {
        return personsList.size
    }
}