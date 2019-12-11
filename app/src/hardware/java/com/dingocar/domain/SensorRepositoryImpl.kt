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

    private var sensitivity: Float = 2f

    private var channel = buildNewChannel()

    companion object {
        //The accelerometer's sensor data has a delta against earth's gravity - 9.80m/s. So
        //to normalise the readings, this is a multiplication factor of 1/9.80
        const val NORMALISATION_FACTOR = 0.102
    }


    init {
        setSensitivity(1f)
    }

    private fun buildNewChannel(): Channel<SensorModel> {

        return Channel<SensorModel>(Channel.CONFLATED)
    }


    override fun getTilt(): Channel<SensorModel> {

        channel.close()
        channel = buildNewChannel()

        val sensorManager =
            applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val accelerometerListener = object: SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //ToDo handle calbiration
            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER ?: false) {
                    //event!!.values[1] * NORMALISATION_FACTOR
                }
            }

        }

        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        sensorManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //ToDo handle calbiration
            }

            override fun onSensorChanged(event: SensorEvent?) {
                sendEvent(channel, event)
            }


        }, sensor, SensorManager.SENSOR_DELAY_UI);

        return channel
    }

    private fun sendEvent(channel: Channel<SensorModel>, event: SensorEvent?) {
        CoroutineScope(Dispatchers.IO)
            .launch {
                event
                    ?.let { SensorModel(it.values[1] * sensitivity, it.values[0] * sensitivity) }
                    ?.let {
                        if (!channel.isClosedForSend) {
                            channel.send(it)
                        }
                    }
            }
    }

    override fun setSensitivity(sensitivity: Float) {
        if (sensitivity > 0 && sensitivity <= 1) {
            this.sensitivity = sensitivity * 2f // because hw input only goes between -0.5 and 0.5
        }
    }

}
