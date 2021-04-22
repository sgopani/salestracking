package com.example.salestracking

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.salestracking.attendance.Tracking
import com.example.salestracking.attendance.locationCallback
import com.example.salestracking.databse.model.TrackingLocation
import com.example.salestracking.repository.FireStoreViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>
class TrackingService : LifecycleService() {
    var isFirstRun = true
    var serviceKilled = false
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var prefManager: PrefManager
    private lateinit var user:FirebaseUser
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var curNotificationBuilder: NotificationCompat.Builder
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
        viewModel=FireStoreViewModel()
        user= FirebaseAuth.getInstance().currentUser!!
        prefManager=PrefManager(this.applicationContext)
    }


    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if(TrackingUtility.hasLocationPermissions(this)) {
                status.value= SalesApiStatus.LOADING
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient
                        .requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            }

            else{
                status.value=SalesApiStatus.ERROR
            }
        }
        else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun killService() {
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
        val trackingLocation=TrackingLocation(isCheckedIn = false)
        viewModel.addTrackingLocation(trackingLocation)
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!) {
                result.locations.let { locations ->
                    for(location in locations) {
                        addPathPoint(location)
                        Log.d(TAG,"NEW LOCATION: ${location.latitude}, ${location.longitude}")

                        if(locations.isEmpty()){
                            status.value=SalesApiStatus.ERROR
                        }
                        else{
                            status.value=SalesApiStatus.DONE
                            val trackingLocation=TrackingLocation(
                                prefManager.getFullName().toString(),
                                user.uid,System.currentTimeMillis(),location.latitude,location.longitude,true)
                            viewModel.addTrackingLocation(trackingLocation)
                        }
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
            Log.d(TAG,"${pathPoints.value}")
        }
    }
    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Log.d(TAG,"Resuming service...")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.d(TAG,"Paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Log.d(TAG,"Stopped service")
                    killService()
                }
                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun pauseService() {
        isTracking.postValue(false)
        //isTimerEnabled = false
    }
    private fun startForegroundService() {
        addEmptyPolyline()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.drawable.tracking_employee)
                .setContentTitle("Sales Employee Tracking")
                .setContentText("Location service enabled")
                .setContentIntent(getMainActivityPendingIntent())
        startForeground(NOTIFICATION_ID, notificationBuilder.build())

    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).also {
                it.action = ACTION_SHOW_TRACKING_FRAGMENT
            },
            FLAG_UPDATE_CURRENT
    )


    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    IMPORTANCE_LOW
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }
    companion object {
        private const val  TAG = "Tracking"
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val status= MutableLiveData<SalesApiStatus>()
    }
}