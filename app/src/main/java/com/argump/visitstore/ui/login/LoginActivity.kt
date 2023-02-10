package com.argump.visitstore.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.argump.visitstore.BuildConfig
import com.argump.visitstore.R
import com.argump.visitstore.databinding.ActivityLoginBinding
import com.argump.visitstore.util.Constant
import com.argump.visitstore.persistence.SharedPreferences
import com.argump.visitstore.ui.mainmenu.MainMenuActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val moduleLoginActivity = module {
    factory { LoginActivity() }
}

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noStatusBar()

        sharedPref = SharedPreferences(this)

        val keepUsername = sharedPref.getKeepUsername(Constant.KEEP_USERNAME)
        binding.etUsername.setText(keepUsername)

        viewModel.isLogin.observe(this) {
            if (it == true) moveMainMenu()
        }

        viewModel.message.observe(this) {
            showMessage(it)
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.loading.observe(this) {
            binding.pbLoading.visibility = if (it == true) View.VISIBLE else View.GONE
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validate()) {
                viewModel.login(username, password)
                checkKeepUsername(username)
            }
        }

    }

    private fun validate(): Boolean {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        when {
            username.isEmpty() && password.isEmpty() -> {
                binding.etUsername.error = getString(R.string.et_empty)
                binding.etPassword.error = getString(R.string.et_empty)
                return false
            }
            username.isEmpty() -> {
                binding.etUsername.error = "Username is empty"
                return false
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Password is empty"
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun checkKeepUsername(username: String) {
        if (binding.cbKeepUsername.isChecked) {
            sharedPref.putKeepUsername(Constant.KEEP_USERNAME, username)
        } else {
            sharedPref.putKeepUsername(Constant.KEEP_USERNAME, "")
        }
    }

    private fun moveMainMenu() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.activityLogin, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun noStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
}