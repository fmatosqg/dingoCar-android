package com.dingocar.common

import android.app.Application
import com.dingocar.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * @author Fabio de Matos
 * @since 03/12/2019.
 */
class DingoApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKoin()

    }

    private fun initKoin() {
        val koinLogger =
            AndroidLogger(Level.DEBUG)

        startKoin {
            koinLogger
            modules(KoinModules.getInstance().getAllModules())
            androidContext(applicationContext)
        }

    }
}