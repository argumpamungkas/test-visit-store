package com.argump.visitstore.ui.mainmenu.liststore.verifikasistore

import androidx.lifecycle.ViewModel
import com.argump.visitstore.R
import org.koin.dsl.module
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

val moduleVerifikasiViewModel = module {
    factory { VerifikasiStoreViewModel() }
}

class VerifikasiStoreViewModel : ViewModel() {

    fun distance(
        currentLat: Double,
        currentLong: Double,
        targetLat: Double,
        targetLong: Double
    ): Double {
        val earthRadius = 6371.0
        val dLat = currentLat - targetLat
        val dLong = currentLong - targetLong
        val a = sin(dLat / 2.0) * sin(dLat / 2.0) +
                cos(targetLat) * cos(currentLat) * sin(dLong / 2.0) * sin(dLong / 2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = earthRadius * c
        return distance * 1000.0
    }

}