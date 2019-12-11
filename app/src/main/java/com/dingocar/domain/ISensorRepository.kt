package com.dingocar.domain

import kotlinx.coroutines.channels.Channel

/**
 * @author Fabio de Matos
 * @since 03/12/2019.
 */
interface ISensorRepository {

    /**
     * Gets normalized value of phone inclination from -1 to 1
     * If phone is not moving and perfectly flat it should return <0,0>
     *
     * @return Pair<X tilt, Y tilt>
     */
    fun getTilt(): Channel<SensorModel>

    /**
     * Sets how sensitive it is. The lower the number the more you have to rotate the phone
     * to get the same output.
     *
     * @param sensitivity must be between 0 and 1
     */
    fun setSensitivity(sensitivity: Float)
}