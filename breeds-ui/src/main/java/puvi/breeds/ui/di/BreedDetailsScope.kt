package puvi.breeds.ui.di

import puvi.breeds.FullBreedId
import puvi.breeds.ui.BreedDetailsViewModel
import puvi.breeds.useCases.FetchImagesForBreedUseCase

@motif.Scope
interface BreedDetailsScope {
    companion object {
        var factory: ((FullBreedId) -> BreedDetailsScope)? = null

        private var instance: BreedDetailsScope? = null

        fun build(id: FullBreedId) {
            instance = factory!!(id)
        }

        operator fun invoke() = instance!!
    }

    fun viewModel(): BreedDetailsViewModel

    @motif.Objects
    abstract class Objects {
        abstract fun viewModel(): BreedDetailsViewModel

        abstract fun fetchImagesForBreedUseCase(): FetchImagesForBreedUseCase
    }
}
