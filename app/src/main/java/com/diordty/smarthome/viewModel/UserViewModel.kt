package com.diordty.smarthome.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diordty.smarthome.Repository.UserRepository

class UserViewModel:ViewModel() {
    private val repository: UserRepository
    private val _repository = MutableLiveData<String>()
    val username : LiveData<String> = _repository
    init {
        repository = UserRepository().getInstance()
        repository.loadUser(_repository)
    }
}