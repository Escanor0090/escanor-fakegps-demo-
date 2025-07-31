package com.example.escanorfakegpsdemo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var fakeLocation: LatLng? = null
    private var fakeGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val btnToggle = findViewById<Button>(R.id.btnToggleFakeGPS)
        btnToggle.setOnClickListener {
            if (fakeGPSEnabled) {
                disableFakeGPS()
            } else {
                enableFakeGPS()
            }
        }

        checkLocationPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener { latLng ->
            fakeLocation = latLng
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Fake Location"))
            Toast.makeText(this, "Fake location selected: $latLng", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enableFakeGPS() {
        if (fakeLocation == null) {
            Toast.makeText(this, "Select a location on the map first", Toast.LENGTH_SHORT).show()
            return
        }
        fakeGPSEnabled = true
        Toast.makeText(this, "Fake GPS Enabled at $fakeLocation", Toast.LENGTH_SHORT).show()
        // TODO: Implement real fake GPS injection here
    }

    private fun disableFakeGPS() {
        fakeGPSEnabled = false
        Toast.makeText(this, "Fake GPS Disabled", Toast.LENGTH_SHORT).show()
        // TODO: Stop fake GPS
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }
    }
}
