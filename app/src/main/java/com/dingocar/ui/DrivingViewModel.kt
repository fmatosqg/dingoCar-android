package com.dingocar.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dingocar.domain.ISensorRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Fabio de Matos
 * @since 03/12/2019.
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
class DrivingViewModel(private val sensorRepository: ISensorRepository) : ViewModel() {


    val mutableSwitchThrottle = MutableLiveData<Boolean>()
    val mutableThrottleValue = MutableLiveData<Int>() // from 0 to 100


    val mutableSwitchSteer = MutableLiveData<Boolean>()


    private val format = "%s = %+2.2f"
    val tiltX = MutableLiveData<String>()
    val tiltY = MutableLiveData<String>()

    init {
        kickoff()
    }

    private fun kickoff() {

        viewModelScope
            .launch {

                sensorRepository.getTilt()
                    .consumeEach {

                        if (mutableSwitchSteer.value == false) {
                            tiltX.postValue(
                                format.format("x", it.x)
                            )
                        } else {
                            tiltX.postValue("auto steer")
                        }

                        if (mutableSwitchThrottle.value == false) {
                            tiltY.postValue(
                                format.format("y", it.y)
                            )
                        } else {
                            tiltY.postValue("fixed throttle %+2d".format(mutableThrottleValue.value))
                        }

                        delay(500)
                    }
            }
    }

}