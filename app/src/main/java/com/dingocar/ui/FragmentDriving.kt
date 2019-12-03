package com.dingocar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dingocar.databinding.FragmentDrivingBinding
import com.dingocar.domain.ISensorRepository
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Fabio de Matos
 * @since 03/12/2019.
 */
class FragmentDriving : Fragment() {

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
                it.root
            }

    }

}