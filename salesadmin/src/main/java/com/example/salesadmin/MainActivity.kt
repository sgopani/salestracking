package com.example.salesadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salesadmin.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController
    private lateinit var drawable: TextDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        val view=navView.getHeaderView(0)
        val headerName=view.findViewById<TextView>(R.id.nav_header_name)
        val headerImage=view.findViewById<ImageView>(R.id.headerImage)
        val user = FirebaseAuth.getInstance().currentUser
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (user != null)
        {
            val prefManager=PrefManager(this.applicationContext)
            headerName.text=prefManager.getFullName()
            val generator: ColorGenerator = ColorGenerator.MATERIAL
            val color: Int = generator.randomColor
            drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                    .buildRound(user.email!![0].toString().toUpperCase(Locale.ROOT),color)
            headerImage.setImageDrawable(drawable)
            //Toast.makeText(this, "{ ${user?.uid} }", Toast.LENGTH_SHORT).show()
            Log.i("User Token ", user.uid)
        }

        else{
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        navController=this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupWithNavController(navView,navController)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController,drawerLayout)
        //val navController=Navigation.findNavController(this,R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this,navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController= Navigation.findNavController(this,R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }

    }

}