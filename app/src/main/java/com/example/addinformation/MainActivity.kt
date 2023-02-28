package com.example.addinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.addinformation.databinding.ActivityAddInformationBinding
import com.example.addinformation.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var addInfoPerson: Button
    private lateinit var personArrayList: ArrayList<Persons>
    private  var displayArrayList = mutableListOf<Any>()

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding!!.swipeRefresh.setOnRefreshListener {
            displayArrayList.clear()
            displayArrayList.addAll(personArrayList)
            binding!!.swipeRefresh.isRefreshing = false
            readFireStoreData()
        }

        binding!!.delete.setOnClickListener {
            val intent = Intent(this, DeleteActivity::class.java)
            startActivity(intent)
        }
        addInfoPerson = findViewById(R.id.addInfoPerson)
        addInfoPerson.setOnClickListener{
            val intent = Intent(this, AddInformation::class.java)
            startActivity(intent)
        }
    }
    fun readFireStoreData() {
        binding!!.rRecycler.layoutManager = LinearLayoutManager(this)
        personArrayList = arrayListOf()
        db = FirebaseFirestore.getInstance()
        db.collection("persons").get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val person: Persons? = data.toObject(Persons::class.java)
                        if (person != null) {
                            personArrayList.add(person)
                        }
                    }
                    binding!!.rRecycler.adapter = PersonsAdapter(personArrayList)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        readFireStoreData()
    }

}
