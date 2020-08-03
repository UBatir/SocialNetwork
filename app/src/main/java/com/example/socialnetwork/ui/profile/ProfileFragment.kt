package com.example.socialnetwork.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialnetwork.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment:Fragment(R.layout.fragment_profile) {
    private val db= FirebaseFirestore.getInstance()
    private val mAuth=FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showData()
        saveButton.setOnClickListener {
            loading.visibility=View.VISIBLE
            val map:MutableMap<String,Any?> = mutableMapOf()
            map["username"]=etUsername.text.toString()
            map["email"]=etEmail.text.toString()
            map["phone"]=etPhone.text.toString()
            map["info"]=etInfo.text.toString()
            db.collection("users").document(mAuth.currentUser?.uid!!).set(map)
                .addOnSuccessListener {
                    loading.visibility=View.GONE
                    Toast.makeText(requireContext(),"Your profile data has been changed successfully",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e->
                    loading.visibility=View.GONE
                    Toast.makeText(requireContext(),e.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun showData(){
        loading.visibility=View.GONE
        db.collection("users").document(mAuth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                etUsername.setText(it.get("username").toString())
                etEmail.setText(it.get("email").toString())
                etPhone.setText(it.get("phone").toString())
                etInfo.setText(it.get("info").toString())
            }
    }
}