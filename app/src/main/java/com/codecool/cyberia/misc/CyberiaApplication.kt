package com.codecool.cyberia.misc

import android.app.Application
import org.koin.android.ext.android.startKoin

class CyberiaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules = listOf(appModule))
    }
}