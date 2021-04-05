
package com.example.salestracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.employee.dashboard.EmployeeDashboard
import com.example.salestracking.login.LoginActivity
import com.example.salestracking.repository.FireStoreRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (user != null)
        {

            Log.i("User Token ", user.uid)
            val firebaseRepository = FireStoreRepository()
            val prefManager=PrefManager(this.applicationContext)
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
                    //Toast.makeText(this, "{ ${COMPANYUID} }", Toast.LENGTH_SHORT).show()
//            name.setText(prefManager.getFullName())
//            address.setText(prefManager.getAddress())
//            phoneNo.setText(prefManager.getPhoneNo())
//            email.setText(prefManager.getEmail())
//                name.setText(userInfo?.name)
//                address.setText(userInfo?.Address)
//                phoneNo.setText(userInfo?.phoneNo)
//                email.setText(userInfo?.emailId)
                    //Toast.makeText(this.requireContext(),"${userInfo?.phoneNo}",Toast.LENGTH_SHORT).show()
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

        val navController=this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        //val navController=Navigation.findNavController(this,R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this,navController)
        NavigationUI.setupWithNavController(bottomNavigationView,
            navController)
        navigateToTrackingFragmentIfNeeded(intent)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController=Navigation.findNavController(this,R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController,null)
    }

    override fun onStart() {
        super.onStart()
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