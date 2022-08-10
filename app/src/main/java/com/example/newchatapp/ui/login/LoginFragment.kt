package com.example.newchatapp.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.newchatapp.databinding.FragmentLoginBinding
import com.example.newchatapp.ui.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel =
            ViewModelProvider(this)[LoginViewModel::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val editTextEmail = binding.editTextEmail
        val editTextPassword = binding.editTextPassword
        val textView: TextView = binding.textLogin

        // firebase auth instance
        binding.login.setOnClickListener(View.OnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter email/password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                signIn(email, password)
            }

        })
        binding.register.setOnClickListener(View.OnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter email/password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                signUp(email, password)
            }
        })



        loginViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser
                    Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("Error", task.exception.toString())
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        getUser()
    }

    private fun signOut() {
        firebaseAuth.signOut()
    }

    private fun signUp(email: String, password: String) {
        val user = hashMapOf(
            "email" to email,
            "password" to password
        )
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser
                    Toast.makeText(context, "Register success.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("Error", task.exception.toString())
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Register failed.", Toast.LENGTH_SHORT).show()
                }
            }
        saveUser(user)
    }

    private fun saveUser(user: HashMap<String, String>) {
        db.collection("users").add(user)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "User saved successfully ${documentReference.id}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUser(){
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("User", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("User", "Error getting documents.", exception)
            }
    }
}