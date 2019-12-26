package puvi.breeds.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import puvi.breeds.BreedImage
import puvi.breeds.useCases.FetchAllBreedImagesUseCase
import puvi.utils.Left
import puvi.utils.Right

class BreedsListViewModel(
    fetchAllBreedImagesUseCase: FetchAllBreedImagesUseCase
) : ViewModel() {
    val imagesLiveData = MutableLiveData<List<BreedImage>>()
    val errorLiveData = MutableLiveData<Throwable>()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += fetchAllBreedImagesUseCase()
            .subscribe { either ->
                when (either) {
                    is Right -> {
                        imagesLiveData.postValue(either.value)
                        errorLiveData.postValue(null)
                    }
                    is Left -> errorLiveData.postValue(either.value)
                }
            }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
