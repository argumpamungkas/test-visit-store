package com.argump.visitstore.ui.mainmenu.liststore.menuvisit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.argump.visitstore.databinding.ActivityMenuVisitBinding
import com.argump.visitstore.ui.mainmenu.liststore.verifikasistore.VerifikasiStore
import org.koin.dsl.module

val moduleMenuVisitActivity = module {
    factory { MenuVisitActivity() }
}

class MenuVisitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuVisitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuVisitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelesai.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, VerifikasiStore::class.java))
        finish()
    }
}