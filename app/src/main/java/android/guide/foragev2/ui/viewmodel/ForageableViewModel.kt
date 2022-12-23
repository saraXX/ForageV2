package android.guide.foragev2.ui.viewmodel

import android.guide.foragev2.data.ForageableDao
import android.guide.foragev2.model.Forageable
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForageableViewModel(private val forageableDao: ForageableDao) : ViewModel() {

    val foragableList: LiveData<List<Forageable>> = forageableDao.getForageables().asLiveData()

    fun getForageableById(id: Long): LiveData<Forageable> {
        return forageableDao.getForageable(id).asLiveData()
    }

    fun addForageable(
        name: String,
        address: String,
        inSeason: Boolean,
        note: String
    ) {
        val forageable = Forageable(
            name = name,
            address = address,
            inSeason = inSeason,
            notes = note
        )
        viewModelScope.launch {
            forageableDao.insert(forageable)
        }
    }


    fun updateForageable(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        note: String
    ) {
        val forageable = Forageable(
            id = id,
            name = name,
            address = address,
            inSeason = inSeason,
            notes = note
        )
        viewModelScope.launch {
            forageableDao.update(forageable)
        }
    }

    fun deleteForageable(forageable: Forageable) {
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.delete(forageable)
        }
    }

    fun isValidEntry(name: String, address: String): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }
}



class ForageableViewModelFactory(private val forageableDao: ForageableDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForageableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForageableViewModel(forageableDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}