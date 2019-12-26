package puvi.breeds.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import puvi.breeds.FullBreedId
import puvi.breeds.useCases.FetchImagesForBreedUseCase
import puvi.utils.Left
import puvi.utils.Right
import java.net.URL

class BreedDetailsViewModel(
    breedId: FullBreedId,
    fetchImagesForBreedUseCase: FetchImagesForBreedUseCase
) : ViewModel() {
    val imagesLiveData = MutableLiveData<Set<URL>>()
    val errorLiveData = MutableLiveData<Throwable>()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += fetchImagesForBreedUseCase
            .invoke(breedId)
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
