package com.argump.visitstore.ui.mainmenu.liststore.verifikasistore

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.argump.visitstore.R
import com.argump.visitstore.databinding.ActivityVerifikasiStoreBinding
import com.argump.visitstore.databinding.DetailStoreBinding
import com.argump.visitstore.model.DataStore
import com.argump.visitstore.persistence.SharedPreferences
import com.argump.visitstore.ui.mainmenu.liststore.menuvisit.MenuVisitActivity
import com.argump.visitstore.util.Constant
import com.argump.visitstore.util.GetLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import java.io.ByteArrayOutputStream

val moduleVerifikasiStore = module {
    factory { VerifikasiStore() }
}

class VerifikasiStore : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityVerifikasiStoreBinding
    private lateinit var bindingDetailStore: DetailStoreBinding
    private val viewModel: VerifikasiStoreViewModel by viewModel()
    private lateinit var sharedPref: SharedPreferences

    private val store_detail by lazy {
        intent.getSerializableExtra("detail_store") as DataStore
    }

    private var takePhoto = false
    private var currentLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifikasiStoreBinding.inflate(layoutInflater)
        bindingDetailStore = binding.detailStore
        setContentView(binding.root)

        binding.btnBack.setOnClickListener(this)
        binding.btnNoVisit.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        binding.ibCamera.setOnClickListener(this)
        binding.ibTagLocation.setOnClickListener(this)
        binding.btnVisit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
            R.id.ib_up_arrow -> {
                showMessage("Up Arrow")
            }
            R.id.ib_camera -> {
                cameraPermission()
            }
            R.id.ib_tag_location -> {
                getTagLocation()
            }
            R.id.btn_no_visit -> {
                onBackPressed()
            }
            R.id.btn_visit -> {
                if (validate()) {
                    if (calculateDistance()) {
                        if (isVisitDone()) {
                            startActivity(Intent(this, MenuVisitActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(CAMERA), Constant.MY_REQUEST_CAMERA)
            } else {
                openCamera()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Constant.MY_ACTION_CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.MY_REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            showMessage("Camera permission was denied")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.MY_ACTION_CAMERA) {
            val pictureBitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            binding.ivPicture.setImageBitmap(pictureBitmap)
            takePhoto = true
        }
    }

    private fun getTagLocation() {
        val getCurrentLocation = GetLocation(applicationContext)
        val location = getCurrentLocation.getLoc()
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            currentLocation = LatLng(latitude, longitude)
            showMessage("Success tag location")
        }
    }

    private fun validate(): Boolean {
        return when {
            !takePhoto && (currentLocation?.latitude == null && currentLocation?.longitude == null) -> {
                showMessage("You haven't taken a picture and tagged a location")
                false
            }
            !takePhoto -> {
                showMessage("You haven't taken a picture yet")
                false
            }
            currentLocation?.latitude == null && currentLocation?.longitude == null -> {
                showMessage("You haven't marked a location yet")
                false
            }
            else -> true
        }
    }

    private fun calculateDistance(): Boolean {
        val currentLat = currentLocation?.latitude
        val currentLong = currentLocation?.longitude
        val targetLat = store_detail.latitude.toDouble()
        val targetLong = store_detail.longitude.toDouble()
        val calculate = viewModel.distance(
            currentLat = currentLat!!,
            currentLong = currentLong!!,
            targetLat = targetLat,
            targetLong = targetLong
        )
        if (calculate > 100.0) {
            showMessage("jarak terlalu jauh, jarak saat ini ${calculate.toInt()} meter dari toko")
            return false
        }
        return true
    }

    private fun isVisitDone(): Boolean {
        val index = intent.getIntExtra("index", 0)
        sharedPref = SharedPreferences(this)

        val getListStore = sharedPref.getStore(Constant.STORE)
        val typeStoreList = object : TypeToken<ArrayList<DataStore>>() {}.type
        val dataStoreList = Gson().fromJson<ArrayList<DataStore>>(getListStore, typeStoreList)

        dataStoreList[index].isVisit = true

        val dataJsonStore = Gson().toJson(dataStoreList)
        sharedPref.putStore(Constant.STORE, dataJsonStore)
        return true
    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.activityVerify, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}