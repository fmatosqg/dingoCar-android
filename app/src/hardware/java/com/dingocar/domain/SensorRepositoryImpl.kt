package com.dingocar.domain

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class SensorRepositoryImpl(private val applicationContext: Context) : ISensorRepository {

    private var channel = buildNewChannel()

    private fun buildNewChannel(): Channel<SensorModel> {

        return Channel<SensorModel>(Channel.CONFLATED)
    }


    override fun getTilt(): Channel<SensorModel> {

        channel.close()
        channel = buildNewChannel()

        val sensorManager =
            applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)


        sensorManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent?) {
                sendEvent(channel, event)
            }


        }, sensor, 10000);

        return channel
    }


    private fun sendEvent(channel: Channel<SensorModel>, event: SensorEvent?) {


        CoroutineScope(Dispatchers.IO)
            .launch {
                event
                    ?.let { SensorModel(it.values[0], it.values[1]) }
                    ?.let {
                        if (!channel.isClosedForSend) {
                            channel.send(it)
                        }
                    }
            }

    }

    override fun setSensitivity(sensitivity: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}