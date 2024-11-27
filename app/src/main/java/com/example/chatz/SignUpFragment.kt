package com.example.chatz

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_sign_up, container, false)
        val alreadySignIn : TextView = view.findViewById(R.id.alreadySignIn)
        alreadySignIn.setOnClickListener {
            val intent = Intent(this.context, OnboardingActivity :: class.java)
            startActivity(intent)
        }

        val signUp : Button = view.findViewById(R.id.signUpButton)

        signUp.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val name : String = view.findViewById<EditText?>(R.id.nameInput).text.toString()
            var email : String = view.findViewById<EditText>(R.id.emailInput).text.toString()

            println(email)
            val password : String = view.findViewById<EditText>(R.id.passwordInput).text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // trimming the email as we can't use special character like (., #, @ etc)
                        email = email.substring(0, email.length-10)

                        // calling the function to add the user in the database
                        addUserToDatabase(name, email)

                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this.context, "Sign Up successful. Please Log In... " + email, Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        updateUI()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this.context,"Sign Up unsuccessful. Please try again..."+ email, Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return view
    }

    // function to add the user in the database
    private fun addUserToDatabase(name : String, email : String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.child(email).setValue(User(name, email))
    }

    // function to update the UI
    private fun updateUI() {
        val intent = Intent(context, OnboardingActivity::class.java)
        intent.putExtra("key", "FromSignUpToLogin")
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}