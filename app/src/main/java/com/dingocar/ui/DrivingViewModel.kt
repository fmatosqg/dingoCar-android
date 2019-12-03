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

                        tiltX.postValue(
                            format.format("x", it.x)
                        )

                        tiltY.postValue(
                            format.format("y", it.y)
                        )

                        delay(500)
                    }
            }
    }

}