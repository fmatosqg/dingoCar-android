package com.dingocar.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Fabio de Matos
 * @since 03/12/2019.
 */
class DrivingViewModel : ViewModel() {

    val tiltX = MutableLiveData<String>()
    val tiltY = MutableLiveData<String>()

    init {
        kickoff()
    }

    private fun kickoff() {

        viewModelScope
            .launch {
                while (true) {

                    delay(1_000)

                    tiltX.postValue(1.0f.toString())
                    tiltY.postValue(1.0f.toString())
                }
            }
    }

}