package com.example.salestracking.attendance

import android.Manifest
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.salestracking.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private lateinit var mapView:MapView
private lateinit var rootView: View
private  var latitude:Double=0.0
private  var longitude:Double=0.0
private const val DEFAULT_ZOOM = 15
var gps_enabled = false
var network_enabled = false
private lateinit var tvLocationService:TextView
private lateinit var tvVerifyLocationService:TextView
private lateinit var noThanks:TextView
private lateinit var turnOnLocation:Button
private lateinit var imgLocation:ImageView
private const val  TAG = "CheckInOut"
private lateinit var  locationRequest:LocationRequest
private const val  LOCATION_REQUEST_CODE:Int = 10001

var locationCallback: LocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult == null) {
            return
        }
        for (location in locationResult.locations) {
            latitude=location.latitude
            longitude=location.longitude
            Log.d(TAG, "onLocationResult: $longitude")
        }
    }
}

class CheckInOut : Fragment(),OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
        locationRequest = LocationRequest.create()
        locationRequest.interval = 4000;
        locationRequest.fastestInterval = 2000;
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        Log.d(TAG, "Oncreate Called")
    }
    fun init(){
        tvLocationService= rootView.findViewById(R.id.tv_location_services)
        tvVerifyLocationService= rootView.findViewById(R.id.tv_location_verify)
        noThanks= rootView.findViewById(R.id.tv_no_thanks)
        turnOnLocation= rootView.findViewById(R.id.btn_turn_on_location)
        imgLocation= rootView.findViewById(R.id.location_image)
    }

    private fun checkSettingsAndStartLocationUpdates() {
        val request: LocationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build()
        val client = LocationServices.getSettingsClient(this.requireActivity())
        val locationSettingsResponseTask = client.checkLocationSettings(request)
        locationSettingsResponseTask.addOnSuccessListener {
            //Settings of device are satisfied and we can start location updates
            startLocationUpdates()
            Log.d(TAG, "checkSettingsAndStartLocationUpdates() success")
        }
        locationSettingsResponseTask.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this.requireActivity(), 1001)
                    Log.d(TAG, "checkSettingsAndStartLocationUpdates() try failure")
                } catch (ex: SendIntentException) {
                    ex.printStackTrace()
                    Log.d(TAG, "checkSettingsAndStartLocationUpdates() catch failure")
                }
            }
        }
    }
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper()).addOnSuccessListener {
            //mapView.getMapAsync(this)
        }

        Log.d(TAG, "startLocationUpdates() success")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSettingsAndStartLocationUpdates()
                Log.d(TAG, "onRequestPermissionsResult")
            } else {
                //Permission not granted
            }
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.check_in_out, container, false)
        init()
        //Log.d(TAG, "onStart()")
        mapView= rootView.findViewById(R.id.mapView)
        if(latitude==0.00 && longitude==0.00){
            Log.d(TAG, "onCreateView, value 00")
        }
        else{
            Log.d(TAG, "onCreateView, value not zero")
        }
        turnOnLocation.setOnClickListener {
        }
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        //mapView.getMapAsync(this)
        Log.d(TAG, "onCreateView")
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.mapType=GoogleMap.MAP_TYPE_NORMAL
        googleMap?.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title("Your Current location"))
        googleMap?.moveCamera(CameraUpdateFactory
                .newLatLngZoom(LatLng(latitude, longitude), DEFAULT_ZOOM.toFloat()))

        Log.d(TAG, "onMapReady $latitude $longitude")
    }

    private fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...")
                ActivityCompat.requestPermissions(this.requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
                Log.d(TAG, "askLocationPermission()")
            } else {
                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
                Log.d(TAG, "askLocationPermission()")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this.requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkSettingsAndStartLocationUpdates()
            Log.d(TAG, "onCreateView, checkSettingsAndStartLocationUpdates() ")
        } else {
            askLocationPermission()
            //Log.d(TAG, "onMapReady")
        }
        mapView.onStart()
        //mapView.getMapAsync(this)
        Log.d(TAG, "onStart()")
    }
    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        //stopLocationUpdates()
        mapView.onStop()
        Log.d(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
        Log.d(TAG, " onLowMemory()")
    }
}