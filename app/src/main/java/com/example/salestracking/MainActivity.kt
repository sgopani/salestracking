
package com.example.salestracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.employee.dashboard.EmployeeDashboard
import com.example.salestracking.login.LoginActivity
import com.example.salestracking.repository.FireStoreRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
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
        val user = FirebaseAuth.getInstance().currentUser
        navView = findViewById(R.id.navView)
        val view=navView.getHeaderView(0)
         val headerName=view.findViewById<TextView>(R.id.nav_header_name)
         val headerImage=view.findViewById<ImageView>(R.id.headerImage)
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (user != null)
        {
            Log.i("User Token ", user.uid)
            val firebaseRepository = FireStoreRepository()
            val prefManager=PrefManager(this.applicationContext)
            val generator: ColorGenerator = ColorGenerator.MATERIAL
            val color: Int = generator.randomColor
            drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                    .buildRound(user.email!![0].toString().toUpperCase(Locale.ROOT), color)
            COMPANYUID=prefManager.getCompanyId().toString()
                    //prefManager.getCompanyId().toString()
            firebaseRepository.getUserInfo().addOnSuccessListener {document->
                if (document.data!=null) {
                    val userInfo = document.toObject(Employee::class.java)
                    Log.d("firebaseRepository", "${document.data}")
                    prefManager.setFullName(userInfo?.name.toString())
                    prefManager.setAddress(userInfo?.Address.toString())
                    prefManager.setEmail(userInfo?.emailId.toString())
                    prefManager.setPhone(userInfo?.phoneNo.toString())
                    prefManager.setCompanyID(userInfo?.companyId.toString())
                    //prefManager.setCompanyID(userInfo?.time.toString())
                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/$COMPANYUID")
                    headerName.text=prefManager.getFullName()
                    headerImage.setImageDrawable(drawable)
                }
            }.addOnFailureListener {
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        navController=this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        NavigationUI.setupWithNavController(navView,navController)
        NavigationUI.setupActionBarWithNavController(this, navController,drawerLayout)
        navigateToTrackingFragmentIfNeeded(intent)
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,drawerLayout)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            myNavHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }

    }