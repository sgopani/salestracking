package com.example.salesadmin.tracking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.*
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackingEmployeeListAdapter
    private  var employeeLocation:MutableList<TrackingLocation> = arrayListOf()
    private lateinit var noEmployeeTracking:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun init(){
        viewModel= FireStoreViewModel()
        mapView=rootView.findViewById(R.id.mapView)
        recyclerView=rootView.findViewById(R.id.rv_employee_tracking)
        noEmployeeTracking=rootView.findViewById(R.id.no_employee_tracking)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.employee_tracking, container, false)
        // Inflate the layout for this fragment\
        init()
        return rootView
    }
    private fun configureTrackingList(){
        adapter= TrackingEmployeeListAdapter(employeeLocation)
        recyclerView.adapter=adapter
        //progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        //itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun checkStatus(status: SalesApiStatus) {
        when (status) {
            SalesApiStatus.LOADING -> {
                //progressBar.visibility=View.VISIBLE
            }
            SalesApiStatus.ERROR -> {
                if (isInternetOn(this.requireContext())) {
                    Toast.makeText(this.context, "Connected to internet", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.newsList2)
                } else {
                    Toast.makeText(
                        this.context,
                        "Please Check Your Internet Connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //progressBar.visibility = View.GONE
            }
            SalesApiStatus.DONE -> {
                noEmployeeTracking.visibility = View.INVISIBLE
                //progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY -> {
                noEmployeeTracking.visibility = View.VISIBLE
                //progressBar.visibility = View.GONE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getEmployeeLocation()
        //mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        configureTrackingList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.employeeTrackingList.observe(viewLifecycleOwner, Observer {trackingLocation->
            if(trackingLocation.isNotEmpty()){
                Log.d(TAG,"$trackingLocation")
                employeeLocation=trackingLocation
                mapView.getMapAsync(this)
                adapter.trackingEmployeeList=employeeLocation
                adapter.notifyDataSetChanged()
                //mapView.onCreate(savedInstanceState)
            }
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkStatus(status)
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