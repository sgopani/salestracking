package com.example.salestracking.attendance

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.salestracking.*
import com.example.salestracking.Polyline
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class Tracking : Fragment(),OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private lateinit var mapView: MapView
    private lateinit var rootView: View
    private var map: GoogleMap? = null
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private lateinit var startButton: Button
    private lateinit var progressBar:ProgressBar
    private lateinit var stopButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.tracking, container, false)
        mapView = rootView.findViewById(R.id.tracking_mapview)
        progressBar=rootView.findViewById(R.id.progress_bar)
        stopButton=rootView.findViewById(R.id.btn_stop)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startButton = rootView.findViewById(R.id.btn_start)
        mapView.onCreate(savedInstanceState)
        //requestPermissions()
        progressBar.visibility = View.VISIBLE
        isLocationEnabled()

        startButton.setOnClickListener {
            //sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            startTracking()
        }
        stopButton.setOnClickListener {
            showCancelTrackingDialog()
        }

        Log.d(TAG, "$pathPoints")
    }
    private fun startTracking(){
        if(isInternetOn(this.requireActivity())){
            if (requestPermissions()) {
                if (isLocationEnabled()) {
                    toggleRun()
                } else {
                    Toast.makeText(context, "Please turn on your Location", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        else{
            Toast.makeText(this.requireContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showCancelTrackingDialog() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure you want to want to check out")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
                stopRun()
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }
    private fun checkStatus(status: SalesApiStatus) {
        when (status) {
            SalesApiStatus.LOADING -> {
                //progressBar.visibility=View.VISIBLE
                Toast.makeText(this.context, "Loading", Toast.LENGTH_SHORT).show()
            }
            SalesApiStatus.ERROR -> {
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.DONE -> {
                progressBar.visibility = View.GONE
                //Toast.makeText(this.context, "Done", Toast.LENGTH_SHORT).show()
            }
            SalesApiStatus.EMPTY -> {
                progressBar.visibility = View.GONE

                //Toast.makeText(this.context, "Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isLocationEnabled():Boolean{

        val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(context)
                    .setMessage(R.string.verify)
                    .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        context?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
                    .setNegativeButton(R.string.no_thanks, DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        val action = TrackingDirections.actionTrackingToEmployeeDashboard()
                        findNavController().navigate(action)
                    })
                    .show()
            return false
        }
        return true
    }
    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        val action=TrackingDirections.actionTrackingToEmployeeDashboard()
        findNavController().navigate(action)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //subscribeToObservers()
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })
        TrackingService.status.observe(viewLifecycleOwner, Observer {
            Log.d(TAG,"$it")
            checkStatus(it)
        })
        mapView.getMapAsync(this)
    }

    private fun subscribeToObservers() {
//        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
//            updateTracking(it)
//        })
//
//        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
//            pathPoints = it
//            addLatestPolyline()
//            moveCameraToUser()
//        })
    }

    private fun toggleRun() {
        if(isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking) {
            startButton.visibility = View.VISIBLE
            stopButton.visibility=View.INVISIBLE
        } else {
               stopButton.visibility=View.VISIBLE
                startButton.visibility=View.INVISIBLE
//            btnToggleRun.text = "Stop"
//            btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            pathPoints.last().last(),
                            MAP_ZOOM
                    )
            )

        }

    }

    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .add(preLastLatLng)
                    .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        Log.d(Companion.TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        //stopLocationUpdates()
        mapView.onStop()
        Log.d(Companion.TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        Log.d(Companion.TAG, "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
        Log.d(Companion.TAG, "onSaveInstanceState")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
        Log.d(Companion.TAG, " onLowMemory()")
    }

    companion object {
        private const val  TAG = "Tracking"
    }
    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }

    override fun onMapReady(p0: GoogleMap?) {
        map=p0
        addAllPolylines()
        moveCameraToUser()
//        if(pathPoints.isNotEmpty()){
//            p0?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.tracking_employee))
//                    .position(pathPoints.last().last()).title("Your Current location"))
//        }
    }
    private fun requestPermissions():Boolean {
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return true
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
        return false
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}