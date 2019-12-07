package com.dingocar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.dingocar.databinding.FragmentDrivingBinding
import com.dingocar.domain.ISensorRepository
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Fabio de Matos
 * @since 03/12/2019.
 */
class FragmentDriving : Fragment() {

    private lateinit var binding: FragmentDrivingBinding
    private val viewModel: DrivingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentDrivingBinding.inflate(inflater)
            .let {

                it.lifecycleOwner = this
                it.viewModel = viewModel
                binding = it
                it.root
            }
            .also {
                setupViews()
            }

    }

    private fun setupViews() {

        binding.navigateSwitchSteer.setOnClickListener {
            viewModel.mutableSwitchSteer.postValue(binding.navigateSwitchSteer.isChecked)
        }

        binding.navigateSwitchThrottle.setOnClickListener {
            viewModel.mutableSwitchThrottle.postValue(binding.navigateSwitchThrottle.isChecked)
        }

        binding.navigateBarThrottle
            .setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    viewModel.mutableThrottleValue.postValue(seekBar?.progress)

                }

            })
    }


}