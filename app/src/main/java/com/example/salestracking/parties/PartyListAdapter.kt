package com.example.salestracking.parties

import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salestracking.ItemClickListener
import com.example.salestracking.R
import com.example.salestracking.databse.model.Party
import com.example.salestracking.requestCode
import java.util.*

class PartyListAdapter ( var partyList: List<Party>,var PartyItemClickListeners: ItemClickListener)
    : RecyclerView.Adapter<PartyListAdapter.PartyItem>() {
    //    private var productList= mutableListOf<Products>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyItem {
        return PartyItem.createViewHolder(parent)
    }

    private fun getItem(position: Int):Party {
        return partyList[position]
    }

    override fun onBindViewHolder(holder: PartyItem, position: Int) {
        val parties=getItem(position)
        holder.bind(parties)
        holder.itemView.setOnClickListener{
            if(requestCode==1){
                PartyItemClickListeners.onPartyItemClick(parties)
            }
            else{
                PartyItemClickListeners.onOrderPartyClick(parties)
            }

        }
        val generator: ColorGenerator = ColorGenerator.MATERIAL
        val color: Int = generator.randomColor
        val drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                .buildRound(parties.name[0].toString().toUpperCase(Locale.ROOT), color)
        holder.partyImage.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${partyList.size}")
        return partyList.size
    }
    class PartyItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvName=itemView.findViewById<TextView>(R.id.tv_party_name)
        val tvPhoneNo=itemView.findViewById<TextView>(R.id.tv_party_phoneno)
        val partyImage=itemView.findViewById<ImageView>(R.id.party_imageview)
        val tvAddress=itemView.findViewById<TextView>(R.id.tv_part_address)
        companion object{
            fun createViewHolder(parent: ViewGroup): PartyItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.parties_list_item, parent, false)
                return PartyItem (view)
            }
        }
        fun bind(party: Party) {
            val partyName=party.name
            val phone_no=party.phoneNo
            Linkify.addLinks(tvPhoneNo, Linkify.ALL)
            val address=party.address
            tvName.text=partyName
            tvPhoneNo.text=phone_no
            tvAddress.text=address

        }
    }
    fun updateList(list: MutableList<Party>){
        partyList=list
        notifyDataSetChanged()
    }
}


//package com.example.salestracking.attendance
//
//import android.Manifest
//import android.content.IntentSender.SendIntentException
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.salestracking.R
//import com.google.android.gms.common.api.ResolvableApiException
//import com.google.android.gms.location.*
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.MapView
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//
//
//private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//private lateinit var mapView:MapView
//private lateinit var rootView: View
//private  var latitude:Double=0.0
//private  var longitude:Double=0.0
//private const val DEFAULT_ZOOM = 15
//var gps_enabled = false
//var network_enabled = false
//private lateinit var tvLocationService:TextView
//private lateinit var tvVerifyLocationService:TextView
//private lateinit var noThanks:TextView
//private lateinit var turnOnLocation:Button
//private lateinit var imgLocation:ImageView
//private const val  TAG = "CheckInOut"
//private lateinit var  locationRequest:LocationRequest
//private const val  LOCATION_REQUEST_CODE:Int = 10001
//
//var locationCallback: LocationCallback = object : LocationCallback() {
//    override fun onLocationResult(locationResult: LocationResult?) {
//        if (locationResult == null) {
//            return
//        }
//        for (location in locationResult.locations) {
//            Log.d(TAG, "onLocationResult: $location")
//            location.latitude= latitude
//            location.longitude= longitude
//        }
//    }
//}
//
//
//class CheckInOut : Fragment(),OnMapReadyCallback {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
//        locationRequest = LocationRequest.create();
//        locationRequest.interval = 4000;
//        locationRequest.fastestInterval = 2000;
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    }
//    fun init(){
//        tvLocationService= rootView.findViewById(R.id.tv_location_services)
//        tvVerifyLocationService= rootView.findViewById(R.id.tv_location_verify)
//        noThanks= rootView.findViewById(R.id.tv_no_thanks)
//        turnOnLocation= rootView.findViewById(R.id.btn_turn_on_location)
//        imgLocation= rootView.findViewById(R.id.location_image)
//    }
//    private fun checkSettingsAndStartLocationUpdates() {
//        val request: LocationSettingsRequest = LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest).build()
//        val client = LocationServices.getSettingsClient(this.requireActivity())
//        val locationSettingsResponseTask = client.checkLocationSettings(request)
//        locationSettingsResponseTask.addOnSuccessListener { //Settings of device are satisfied and we can start location updates
//            startLocationUpdates()
//        }
//        locationSettingsResponseTask.addOnFailureListener { e ->
//            if (e is ResolvableApiException) {
//                try {
//                    e.startResolutionForResult(this.requireActivity(), 1001)
//                } catch (ex: SendIntentException) {
//                    ex.printStackTrace()
//                }
//            }
//        }
//
//    }
//    private fun startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this.requireActivity(),
//                        Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this.requireActivity(),
//                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//        mapView.getMapAsync(this)
//
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
////                getLastLocation();
//                checkSettingsAndStartLocationUpdates()
//            } else {
//                //Permission not granted
//            }
//        }
//    }
//    //    private fun fetchLocation() {
////        if(checkLocation()){
////            val task = fusedLocationProviderClient.lastLocation
////            if (ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
////                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                            this.requireContext(), android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=
////                    PackageManager.PERMISSION_GRANTED) {
////                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
////                return
////            }
////            task.addOnSuccessListener { location ->
////                if (location != null) {
////                    hideTurnOnLocationView()
////                    showMap()
////                    latitude = location.latitude
////                    longitude = location.longitude
////                    //Toast.makeText(this.context, "$latitude", Toast.LENGTH_LONG).show()
//    //              mapView.getMapAsync(this)
////                }
////                else{
////                    Toast.makeText(this.context, "Unable to fetch your location", Toast.LENGTH_LONG).show()
////                }
////            }
////        }
////        else {
////            showTurnOnLocationView()
////            hideMap()
////            Toast.makeText(this.context, "Turn On Your Location", Toast.LENGTH_LONG).show()
////
////        }
////
////
////
////    }
//
//    //    private fun checkLocation():Boolean{
////        val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
////        try {
////            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
////        } catch (ex: Exception) {
////        }
////        try {
////            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
////        } catch (ex: Exception) {
////        }
////        //Toast.makeText(this.context, "$gps_enabled", Toast.LENGTH_LONG).show()
////        return gps_enabled && network_enabled
////    }
////
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        super.onViewCreated(view, savedInstanceState)
////        //checkLocation()
////        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this.requireActivity())
////        fetchLocation()
//        //mapView.getMapAsync(this)
//        if (ContextCompat.checkSelfPermission(this.requireActivity(),
//                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////            getLastLocation();
//            checkSettingsAndStartLocationUpdates()
//
//        } else {
//            askLocationPermission()
//        }
//
//        mapView.onCreate(savedInstanceState)
//        //mapView.getMapAsync(this)
//        //        turnOnLocation.setOnClickListener{
////            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
////            startActivity(intent)
////        }
//    }
//
//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        rootView=inflater.inflate(R.layout.check_in_out, container, false)
////        init()
//        mapView= rootView.findViewById(R.id.mapView)
//        return rootView
//    }
//    private fun showMap(){
//        mapView.visibility=View.VISIBLE
//    }
//    private fun hideMap(){
//        mapView.visibility=View.INVISIBLE
//    }
//    private fun hideTurnOnLocationView(){
//        tvLocationService.visibility=View.INVISIBLE
//        tvVerifyLocationService.visibility=View.INVISIBLE
//        noThanks.visibility=View.INVISIBLE
//        imgLocation.visibility=View.INVISIBLE
//        turnOnLocation.visibility=View.INVISIBLE
//    }
//
//    private fun showTurnOnLocationView(){
//        tvLocationService.visibility=View.VISIBLE
//        tvVerifyLocationService.visibility=View.VISIBLE
//        noThanks.visibility=View.VISIBLE
//        imgLocation.visibility=View.VISIBLE
//        turnOnLocation.visibility=View.VISIBLE
//    }
//
//    override fun onMapReady(googleMap: GoogleMap?) {
//        //googleMap = map
//        //MapsInitializer.initialize(this.requireContext())
//        //checkLocation()
//        Toast.makeText(this.context, "$latitude", Toast.LENGTH_LONG).show()
//        googleMap?.mapType=GoogleMap.MAP_TYPE_NORMAL
//        googleMap?.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title("Your Current location"))
//        googleMap?.moveCamera(CameraUpdateFactory
//                .newLatLngZoom(LatLng(latitude, longitude), DEFAULT_ZOOM.toFloat()))
//    }
//
//    private fun askLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Log.d(TAG, "askLocationPermission: you should show an alert dialog...")
//                ActivityCompat.requestPermissions(this.requireActivity(),
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
//            } else {
//                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
//            }
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mapView.onStart()
//    }
//    private fun stopLocationUpdates() {
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()
////        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this.requireActivity())
////        checkLocation()
////        fetchLocation()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mapView.onPause()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        //stopLocationUpdates()
//        mapView.onStop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        mapView.onSaveInstanceState(outState)
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }
//}