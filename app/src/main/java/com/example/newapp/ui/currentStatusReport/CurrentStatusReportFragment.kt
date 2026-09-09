package com.example.newapp.ui.currentStatusReport

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.example.newapp.R
import com.example.newapp.api.Resource
import com.example.newapp.databinding.FragmentCurrentStatusReportBinding
import com.example.newapp.model.DeviceStatus
import com.example.newapp.ui.mapContainer.MapContainerDialogFragment
import java.util.*

class CurrentStatusReportFragment: Fragment() {

    private var _binding: FragmentCurrentStatusReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentStatusViewModel: CurrentStatusViewModel

    private var timer: Timer? = null
    private var deviceStatus: DeviceStatus? = null
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        currentStatusViewModel = ViewModelProvider(this)[CurrentStatusViewModel::class.java]

        _binding = FragmentCurrentStatusReportBinding.inflate(
            inflater,
            container,
            false
        )

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.mixkit_alert_alarm)
        mediaPlayer.isLooping = true

        hideLocations()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.root.setOnClickListener {
            deviceStatus?.let {
                val dialogFragment = MapContainerDialogFragment()
                val bundle = Bundle()
                bundle.putParcelable("deviceStatus", it)
                dialogFragment.arguments = bundle
                dialogFragment.show(requireActivity().supportFragmentManager, "Map Container")
            }
        }

        currentStatusViewModel.getDeviceList().observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Success->{
                    result.data?.let {
                        if(it.isNotEmpty()){
                            deviceStatus = it[0]
                            displayStatus(it[0])
                        }
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        result.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun displayStatus(deviceStatus: DeviceStatus) {
        if (!deviceStatus.contNo.isNullOrEmpty()) {
            stopSound()
            val charArray = deviceStatus.contNo.toCharArray()
            binding.apply {
                tvContNo1.text = charArray[0].toString()
                tvContNo2.text = charArray[1].toString()
                tvContNo3.text = charArray[2].toString()
                tvContNo4.text = charArray[3].toString()

                tvContNo5.text = charArray[4].toString()
                tvContNo6.text = charArray[5].toString()
                tvContNo7.text = charArray[6].toString()
                tvContNo8.text = charArray[7].toString()
                tvContNo9.text = charArray[8].toString()
                tvContNo10.text = charArray[9].toString()
                tvContNo11.text = charArray[10].toString()
            }
        } else {
            playSound()
            binding.apply {
                tvContNo1.text = "X"
                tvContNo3.text = "X"
                tvContNo4.text = "X"
                tvContNo2.text = "X"

                tvContNo5.text = "X"
                tvContNo6.text = "X"
                tvContNo7.text = "X"
                tvContNo8.text = "X"
                tvContNo9.text = "X"
                tvContNo10.text = "X"
                tvContNo11.text = "X"
            }
        }


        if (!deviceStatus.location.isNullOrEmpty()) {
            hideLocations()
            val split = deviceStatus.location.split(":")
            if (split.size>1) {
                showLocations()
                binding.apply {
                    tvLoc1.text = split[0]
                    tvLoc2.text = "${split[1]}:${split[2]}:${split[3]}"
                }
            } else {
                binding.apply {
                    tvLoc1.visibility = View.VISIBLE
                    tvLoc1.text = deviceStatus.location
                }
            }
        } else {
            binding.apply {
                tvLoc1.text = "XX-XX"
                tvLoc2.text = "X:XX:X"
            }
        }
    }

    private fun hideLocations(){
        binding.apply {
            tvLoc1.visibility = View.GONE
            tvLoc2.visibility = View.GONE
        }
    }

    private fun showLocations(){
        binding.apply {
            tvLoc1.visibility = View.VISIBLE
            tvLoc2.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        binding.loadingBar.root.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loadingBar.root.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                currentStatusViewModel.fetchStatus()
            }
        }, 0, 1000*5)
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    private fun playSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop()
        }
    }
}