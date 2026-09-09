package com.example.newapp.ui.mapContainer

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.newapp.api.Resource
import com.example.newapp.databinding.FragmentMapContainerDialogBinding
import com.example.newapp.model.DeviceStatus

class MapContainerDialogFragment: DialogFragment() {

    private var _binding: FragmentMapContainerDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapContainerViewModel: MapContainerViewModel

    private val containerListAdapter by lazy { ContainerListAdapter() }

    private var deviceStatus: DeviceStatus? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mapContainerViewModel = ViewModelProvider(this)[MapContainerViewModel::class.java]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            deviceStatus = arguments?.getParcelable("deviceStatus", DeviceStatus::class.java)
        } else {
            deviceStatus = arguments?.getParcelable("deviceStatus")
        }

        _binding = FragmentMapContainerDialogBinding.inflate(
            inflater,
            container,
            false
        )

        mapContainerViewModel.fetchInventory()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

            containerListAdapter.onContainerClickListener =
                object : ContainerListAdapter.OnContainerClickListener {
                    override fun onContainerClicked(containerNo: String) {
                        containerNoText.setText(containerNo)
                    }

                }

            containerList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = containerListAdapter
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }

            dialogBtnSubmit.setOnClickListener {
                mapContainerViewModel.submitInventory(
                    containerNoText.text.toString().trim(),
                    deviceStatus?.location!!
                )
            }

            dialogBtnCancel.setOnClickListener {
                dismiss()
            }

            deviceStatus?.let {
                dialogLocValue.text = it.location
            }

            containerNoText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    dialogBtnSubmit.isEnabled = containerNoText.text.length == 11
                    containerListAdapter.filter.filter(containerNoText.text.toString())
                }
            })
        }

        mapContainerViewModel.getInventoryList().observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Success ->{
                    result.data?.let {
                        if (it.isNotEmpty()){
                            containerListAdapter.updateList(it)
                        }
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        mapContainerViewModel.getSubmitInventoryResponse().observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Success ->{
                    hideProgressBar()
                    dismiss()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        "Something went wrong. Please try again.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.loadingBar.root.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loadingBar.root.visibility = View.VISIBLE
    }
}