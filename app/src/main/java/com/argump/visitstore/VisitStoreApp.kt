package com.argump.visitstore

import android.app.Application
import com.argump.visitstore.persistence.moduleRepository
import com.argump.visitstore.source.moduleNetwork
import com.argump.visitstore.ui.login.moduleLoginActivity
import com.argump.visitstore.ui.login.moduleLoginViewModel
import com.argump.visitstore.ui.mainmenu.liststore.menuvisit.moduleMenuVisitActivity
import com.argump.visitstore.ui.mainmenu.liststore.moduleListStoreActivity
import com.argump.visitstore.ui.mainmenu.liststore.moduleListStoreViewModel
import com.argump.visitstore.ui.mainmenu.liststore.verifikasistore.moduleVerifikasiStore
import com.argump.visitstore.ui.mainmenu.liststore.verifikasistore.moduleVerifikasiViewModel
import com.argump.visitstore.ui.mainmenu.moduleMainMenuActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VisitStoreApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VisitStoreApp)
            modules(
                moduleNetwork,
                moduleRepository,
                moduleLoginViewModel,
                moduleLoginActivity,
                moduleMainMenuActivity,
                moduleListStoreViewModel,
                moduleListStoreActivity,
                moduleVerifikasiViewModel,
                moduleVerifikasiStore,
                moduleMenuVisitActivity
            )
        }
    }

}