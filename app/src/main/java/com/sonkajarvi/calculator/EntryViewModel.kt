package com.sonkajarvi.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sonkajarvi.calculator.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EntryViewModel : ViewModel() {
    private val dao = MainApplication.database.getDAO()

    fun getAll(): LiveData<List<Entry>> = dao.getAll().asLiveData()

    fun insert(e: Entry) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(e)
        }
    }

    fun delete(e: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(e)
        }
    }
}
