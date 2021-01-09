package com.example.salesadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.salesadmin.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
        {
            Toast.makeText(this, "{ ${user?.uid} }", Toast.LENGTH_SHORT).show()
            Log.i("User Token ", user.uid)
        }
        else{
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val navController=this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController= Navigation.findNavController(this,R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController,null)
    }

}