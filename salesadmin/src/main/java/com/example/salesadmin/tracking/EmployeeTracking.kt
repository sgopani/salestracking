package com.example.salesadmin.tracking

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.salesadmin.R
import com.example.salesadmin.model.TrackingLocation
import com.example.salesadmin.repository.FireStoreViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class EmployeeTracking : Fragment(),OnMapReadyCallback {
    private lateinit var rootView: View
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var mapView: MapView
    private  var employeeLocation:MutableList<TrackingLocation> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun init(){
        viewModel= FireStoreViewModel()
        mapView=rootView.findViewById(R.id.mapView)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.employee_tracking, container, false)
        // Inflate the layout for this fragment
        init()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getEmployeeLocation()
        //mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.employeeTrackingList.observe(viewLifecycleOwner, Observer {trackingLocation->
            if(trackingLocation.isNotEmpty()){
                Log.d(TAG,"$trackingLocation")
                employeeLocation=trackingLocation
                mapView.getMapAsync(this)
                //mapView.onCreate(savedInstanceState)
            }

        })

    }

    companion object {
        private const val  TAG = "employeeTrackingList"
        private const val DEFAULT_ZOOM = 15
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if(employeeLocation.isNotEmpty()){
            googleMap?.clear()
            employeeLocation.forEach { location ->
                googleMap?.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude)).title(location.employeeName))
                googleMap?.moveCamera(
                    CameraUpdateFactory
                    .newLatLngZoom(LatLng(location.latitude, location.longitude), Companion.DEFAULT_ZOOM.toFloat()))
            }
        }
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