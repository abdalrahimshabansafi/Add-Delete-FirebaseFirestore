package com.example.addinformation

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.addinformation.databinding.ActivityAddInformationBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddInformation : AppCompatActivity() {
    private var binding: ActivityAddInformationBinding? = null
    private val fireStoreDatabase = FirebaseFirestore.getInstance()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddInformationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        progressDialog = ProgressDialog(this@AddInformation)

        // Create a new user with a name , number and address
        binding!!.saveInfoPerson.setOnClickListener {
            if (check()) {
                val name = binding!!.addNamePerson.text.toString()
                val number = binding!!.addPhonePerson.text.toString()
                val address = binding!!.addPersonAddress.text.toString()
                progressDialog.setTitle("Add Process")
                progressDialog.setMessage("Persons Added Successfully! ")
                progressDialog.show()
                saveFireStore(name,number,address)
            }
        }
    }
        // Add a new document with a generated ID
    private fun saveFireStore(name: String, number: String, address: String) {
            val person: MutableMap<String,Any> = HashMap()
            person["name"] = name
            person["number"] = number
            person["address"] = address

            fireStoreDatabase.collection("persons")
                .add(person)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    binding!!.addNamePerson.text!!.clear()
                    binding!!.addPhonePerson.text!!.clear()
                    binding!!.addPersonAddress.text!!.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Error Adding Persons",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
    private fun check():Boolean{
        if (binding!!.addNamePerson.text.toString() == ""){
            binding!!.textInputLayout.error = "You cannot leave Person Name Empty!"
            return false
        }
        if (binding!!.addPhonePerson.text.toString() == ""){
            binding!!.textInputLayout2.error = "You cannot leave Person Phone Empty!"
            return false
        }
        if (binding!!.addPersonAddress.text.toString() == ""){
            binding!!.textInputLayout3.error = "You cannot leave Person Address Empty!"
            return false
        }
        return true
    }
}