package com.example.addinformation

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.addinformation.databinding.ActivityAddInformationBinding
import com.example.addinformation.databinding.ActivityDeleteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DeleteActivity : AppCompatActivity() {
    private var binding: ActivityDeleteBinding? = null
    private val db = Firebase.firestore
    private val personCollectionRef = Firebase.firestore.collection("persons")
    lateinit var progressDialog: ProgressDialog
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        progressDialog = ProgressDialog(this@DeleteActivity)

        binding!!.delInfoPerson.setOnClickListener {
            val person = oldPerson()
            deletePerson(person)
            progressDialog.setTitle("Delete Process")
            progressDialog.setMessage("Persons Deleted Successfully! ")
            progressDialog.show()
            binding!!.delNamePerson.text!!.clear()
            binding!!.delPersonAddress.text!!.clear()
            binding!!.delPhonePerson.text!!.clear()

        }
    }
    private fun oldPerson(): Persons{
        val name = binding!!.delNamePerson.text.toString()
        val address = binding!!.delPersonAddress.text.toString()
        val number = binding!!.delPhonePerson.text.toString()
        return Persons(name , address , number)
    }
    private fun deletePerson(person: Persons) = CoroutineScope(Dispatchers.IO).launch {
        val personQuery = personCollectionRef
            .whereEqualTo("name", person.name)
            .whereEqualTo("address", person.address)
            .whereEqualTo("number", person.number)
            .get()
            .await()

        if (personQuery.documents.isNotEmpty()) {
            for (document in personQuery) {
                try {
                    personCollectionRef.document(document.id).delete().await()
                    progressDialog.dismiss()

//                    personCollectionRef.document(document.id).update(mapOf(
//                        "name" to FieldValue.delete()
//                    ))
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@DeleteActivity,
                            e.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(
                    this@DeleteActivity,
                    "No Person Match Data input!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}
