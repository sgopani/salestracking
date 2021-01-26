package com.example.salesadmin.notification

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.salesadmin.CheckNetClass
import com.example.salesadmin.FirebaseServerID
import com.example.salesadmin.R
import com.example.salesadmin.model.Notification
import com.example.salesadmin.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject

class SendNotification : Fragment() {
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=$FirebaseServerID"
    private val contentType = "application/json"
    private lateinit var rootView:View
    private lateinit var submit:Button
    private lateinit var text:EditText
    private lateinit var message:EditText
    private var valid:Boolean=true
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var notificationFireStore:Notification
    private lateinit var user:FirebaseAuth
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_topic")
        rootView=inflater.inflate(R.layout.send_notification, container, false)
        text=rootView.findViewById(R.id.msg)
        message=rootView.findViewById(R.id.tv_title)
        submit=rootView.findViewById(R.id.submit)
        progressBar=rootView.findViewById(R.id.progress_bar)
        user= FirebaseAuth.getInstance()
        submit.setOnClickListener {
            checkField(text)
            checkField(message)
            if (CheckNetClass.checknetwork(this.requireContext())) {
                if (valid) {
                    val time = System.currentTimeMillis()
                    progressBar.visibility=View.VISIBLE
                    viewModel= FireStoreViewModel()
                    notificationFireStore=Notification(message.text.toString(),text.text.toString(),time=time)
                    val topic = "/topics/${user.currentUser?.uid}" //topic has to match what the receiver subscribed to
                    val notification = JSONObject()
                    val notifcationBody = JSONObject()
                    try {
                        notifcationBody.put("title", message.text)
                        notifcationBody.put("message", text.text)
                        notification.put("to", topic)
                        notification.put("data", notifcationBody)
                        Log.e("TAG", "try")
                    } catch (e: JSONException) {
                        Log.e("TAG", "onCreate: " + e.message)
                    }

                    sendNotification(notification)
                }
            }
            else{
                Toast.makeText(this.context,"Please check your internet connection!",Toast.LENGTH_LONG).show()
            }
        }
        // Inflate the layout for this fragment
        return  rootView
    }
    private fun checkField(textField: EditText): Boolean {
        when {
            textField.text.toString().isEmpty() -> {
                textField.error = "Field cannot be empty"
                valid = false
            }
            else->{
                valid=true
            }
        }
        return valid
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
                text.setText("")
                message.setText("")
                Toast.makeText(this.context,"Notification sent Successfully",Toast.LENGTH_LONG).show()
                viewModel.addNotification(notificationFireStore)
                progressBar.visibility=View.GONE
            },
            Response.ErrorListener {
                Toast.makeText(this.context, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
                Toast.makeText(this.context,"Something went Wrong",Toast.LENGTH_LONG).show()
                progressBar.visibility=View.GONE
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}