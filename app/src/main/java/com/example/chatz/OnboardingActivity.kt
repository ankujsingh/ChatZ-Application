package com.example.chatz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class OnboardingActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signUpFragment:Fragment = SignUpFragment()
        val signUPText : TextView = findViewById(R.id.SignUpText)

        // to redirect the user to signUp fragment
        signUPText.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.signUpFrame, signUpFragment)
                commit()
            }
        }

        // checking if the user is coming from SignUp activity or directly opening the app
        var from : String = "null"
        from = intent.getStringExtra("key").toString()

//        if(from != "FromSignUpToLogin"){
//            // authenticator initializer
//            Toast.makeText(this, from, Toast.LENGTH_LONG).show()
//            auth = FirebaseAuth.getInstance()
//            onStartLogin()
//        }

        val loginButton : Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            // authenticator initializer
            auth = FirebaseAuth.getInstance()

            val email : String = findViewById<EditText?>(R.id.emailLoginInput).text.toString()
            val password : String = findViewById<EditText?>(R.id.passwordLoginInput).text.toString()

            logIn(email, password)
        }
    }

    // function to successfully authenticate the user once the app is started
    private  fun onStartLogin() {
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI()
    }

    private fun logIn( email:String?, password:String?){
        if (email != null) {
            if (password != null) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            Toast.makeText(this, "Logged In...", Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Log In unsuccessful. Please try again...", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(this, "Please enter correct password...", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Please enter correct password...", Toast.LENGTH_SHORT).show()
        }
    }

    // to open the mainActivity once user has successfully logged in
    fun updateUI(){
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}