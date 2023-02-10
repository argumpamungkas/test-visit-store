package com.argump.visitstore.ui.mainmenu

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.argump.visitstore.R
import com.argump.visitstore.databinding.ActivityMainMenuBinding
import com.argump.visitstore.databinding.CardKunjunganBinding
import com.argump.visitstore.util.Constant
import com.argump.visitstore.persistence.SharedPreferences
import com.argump.visitstore.ui.login.LoginActivity
import com.argump.visitstore.ui.mainmenu.liststore.ListStoreActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.dsl.module

val moduleMainMenuActivity = module {
    factory { MainMenuActivity() }
}

class MainMenuActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var bindingCard: CardKunjunganBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        bindingCard = binding.cardKunjungan
        setContentView(binding.root)
        noStatusBar()
        sharedPref = SharedPreferences(this)

        binding.btnTargetInstall.setOnClickListener(this)
        binding.btnDashboard.setOnClickListener(this)
        binding.btnTransmissionHistory.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        binding.btnLogout.setOnClickListener(this)
        binding.btnKunjungan.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_kunjungan -> {
                startActivity(Intent(this, ListStoreActivity::class.java))
            }
            R.id.btn_target_install -> {
                showMessage("Target Install")
            }
            R.id.btn_dashboard -> {
                showMessage("Dashboard")
            }
            R.id.btn_transmission_history -> {
                showMessage("Transmission History")
            }
            R.id.btn_logout -> {
                sharedPref.putLoginSession(Constant.IS_LOGIN, false)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    fun showMessage(msg: String) {
        Snackbar.make(binding.mainMenuActivity, msg, Snackbar.LENGTH_SHORT).show()
    }

    fun noStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
}