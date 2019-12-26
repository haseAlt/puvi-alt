package puvi.breeds.ui.di

import puvi.breeds.ui.BreedsListViewModel
import puvi.breeds.useCases.FetchAllBreedImagesUseCase
import puvi.breeds.useCases.FetchBreedsListUseCase

@motif.Scope
interface BreedsListScope {
    companion object {
        var factory: (() -> BreedsListScope)? = null

        private var instance: BreedsListScope? = null

        fun build() {
            instance = factory!!()
        }

        operator fun invoke() = instance!!
    }

    fun viewModel(): BreedsListViewModel

    @motif.Objects
    abstract class Objects {
        abstract fun viewModel(): BreedsListViewModel

        abstract fun fetchAllBreedImagesUseCase(): FetchAllBreedImagesUseCase
        abstract fun fetchBreedsListUseCase(): FetchBreedsListUseCase
    }
}
