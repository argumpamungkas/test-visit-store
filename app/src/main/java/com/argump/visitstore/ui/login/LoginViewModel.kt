package com.argump.visitstore.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.argump.visitstore.model.ResponseStore
import com.argump.visitstore.util.Constant
import com.argump.visitstore.persistence.SharedPreferences
import com.argump.visitstore.persistence.Repository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.dsl.module

val moduleLoginViewModel = module {
    factory { LoginViewModel(get(), get()) }
}

class LoginViewModel(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val loginSession = SharedPreferences(application)

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _responseStore = MutableLiveData<ResponseStore>()

    init {
        sessionLogin()
    }

    fun login(username: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.fetchLogin(username, password)
                _responseStore.value = response
                if (response.status == "success") {
                    _message.value = "Login berhasil"
                    loginSession.putLoginSession(Constant.IS_LOGIN, true)
                    _isLogin.value = loginSession.getLoginSession(Constant.IS_LOGIN)

                    val gson = Gson()
                    val storeList = gson.toJson(response.stores)
                    loginSession.putStore(Constant.STORE, storeList)
                    _loading.value = false
                } else {
                    _message.value = response.message
                    _loading.value = false
                }
            } catch (e: Exception) {
                _message.value = e.message
                _loading.value = false
            }
        }
    }

    private fun sessionLogin() {
        _isLogin.value = loginSession.getLoginSession(Constant.IS_LOGIN)
    }
}