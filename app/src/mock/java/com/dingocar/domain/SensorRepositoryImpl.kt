package com.dingocar.domain

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SensorRepositoryImpl(context: Context) : ISensorRepository {

    private var sensitivity: Float = 1f

    private val delayMs = 100L

    private val dteta: Float = (2.0 * PI * delayMs / 1_000 / 10f).toFloat()

    private var channel = buildChannel()


    init {
        setSensitivity(1f)
    }

    private fun buildChannel(): Channel<SensorModel> {

        return Channel<SensorModel>(Channel.CONFLATED)
    }

    override fun getTilt(): Channel<SensorModel> {

        channel.cancel()
        channel = buildChannel()

        return channel
            .also {
                feedMockedData(it)
            }

    }

    private fun feedMockedData(channel: Channel<SensorModel>) {


        CoroutineScope(Dispatchers.IO)
            .launch {


                var teta = 0f
                while (!channel.isClosedForSend) {

                    if (teta > 2.0 * PI) {
                        teta -= 2f * PI.toFloat()
                    }

                    teta += dteta

                    val model = SensorModel(cos(teta) * sensitivity, sin(teta) * sensitivity)
                    channel.send(model)
                    delay(delayMs) // this is probably an emulator, let's not kill the laptop cpu
                }
            }

    }


    override fun setSensitivity(sensitivity: Float) {

        if (sensitivity > 0 && sensitivity <= 1) {
            this.sensitivity = sensitivity
        }
    }


}