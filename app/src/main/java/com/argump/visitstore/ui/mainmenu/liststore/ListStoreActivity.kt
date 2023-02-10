package com.argump.visitstore.ui.mainmenu.liststore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.argump.visitstore.R
import com.argump.visitstore.adapter.ListStoreAdapter
import com.argump.visitstore.databinding.ActivityListStoreBinding
import com.argump.visitstore.util.Constant
import com.argump.visitstore.model.DataStore
import com.argump.visitstore.util.GetLocation
import com.argump.visitstore.ui.mainmenu.liststore.verifikasistore.VerifikasiStore
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module


val moduleListStoreActivity = module {
    factory { ListStoreActivity() }
}

class ListStoreActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityListStoreBinding
    private val viewModel: ListStoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onStart() {
        super.onStart()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val adapterListStore =
            ListStoreAdapter(viewModel.getListStore(), object : ListStoreAdapter.OnAdapterListener {
                override fun onClick(store: DataStore, position: Int) {
                    startActivity(
                        Intent(applicationContext, VerifikasiStore::class.java)
                            .putExtra("detail_store", store)
                            .putExtra("index", position)
                    )
                }
            })
        binding.rvListStore.adapter = adapterListStore
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        showMarkerStore()
        val permissionLocation = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissionLocation, Constant.MY_REQUEST_GPS)
        } else {
            showCurrentMarker()
        }

    }

    private fun showMarkerStore() {
        val listToko = viewModel.getListStore()
        for (dataToko in listToko) {
            val latitude = dataToko.latitude.toDouble()
            val longitude = dataToko.longitude.toDouble()

            val storeLocation = LatLng(latitude, longitude)
            mMap.addMarker(
                MarkerOptions()
                    .position(storeLocation)
                    .title(dataToko.store_name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }

    }

    private fun showCurrentMarker() {
        val getCurrentLocation = GetLocation(applicationContext)
        val location = getCurrentLocation.getLoc()
        if (location != null) {

            val latitude = location.latitude
            val longitude = location.longitude

            val myLoc = LatLng(latitude, longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15f))
            mMap.isMyLocationEnabled = true

            val circleOptions = CircleOptions()
            circleOptions.center(myLoc)
            circleOptions.radius(100.0)
            circleOptions.strokeColor(Color.BLUE)
            circleOptions.fillColor(0x30A2E0F7)
            circleOptions.strokeWidth(1f)
            mMap.addCircle(circleOptions)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.MY_REQUEST_GPS && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showCurrentMarker()
        } else {
            Snackbar.make(
                binding.listStoreActivity,
                "Location permission was denied",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}