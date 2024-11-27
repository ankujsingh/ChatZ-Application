package com.example.chatz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // authenticator initializing
        auth = Firebase.auth

        val chatFragment = ChatFragment()
        val accountFragment = AccountFragment()

        setCurrentFragment(chatFragment)

        val bottomNavigationView:BottomNavigationView = findViewById(R.id.bottom_nav_bar)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.chats -> setCurrentFragment(chatFragment)
                R.id.account -> setCurrentFragment(accountFragment)
            }
            true
        }

        val topAppBar : MaterialToolbar = findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.logout -> logOut()
            }
            true
        }
    }

    private fun logOut(): Boolean {
        auth = FirebaseAuth.getInstance()
        Firebase.auth.signOut()
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Log Out successful", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }
}