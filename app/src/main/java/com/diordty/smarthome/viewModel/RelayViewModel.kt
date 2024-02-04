package com.diordty.smarthome.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diordty.smarthome.Repository.RelayRepository
import com.diordty.smarthome.models.Relay

class RelayViewModel : ViewModel() {

    private val repository : RelayRepository
    private val _allRelays = MutableLiveData<List<Relay>>()
    val allRelays : LiveData<List<Relay>> = _allRelays
    init {
        repository = RelayRepository().getInstance()
        repository.loadRelays(_allRelays)
    }
}
