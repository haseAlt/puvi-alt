package puvi.di

import android.content.Context
import puvi.PuviApp
import puvi.breeds.BreedsRepository
import puvi.breeds.BreedsService
import puvi.breeds.FullBreedId
import puvi.breeds.data.RetrofitBreedsService
import puvi.breeds.data.SharedPrefBreedsRepository
import puvi.breeds.ui.di.BreedDetailsScope
import puvi.breeds.ui.di.BreedsListScope

@motif.Scope
interface AppScope : motif.Creatable<AppScopeDep> {
    companion object {
        var instance: AppScope? = null

        operator fun invoke() = instance!!
    }

    fun breedsListScope(): BreedsListScope
    fun breedDetailsScope(id: FullBreedId): BreedDetailsScope

    @motif.Objects
    abstract class Objects {
        abstract fun context(b: PuviApp): Context

        fun retrofitBreedsService() = RetrofitBreedsService()

        @motif.Expose
        abstract fun breedsService(b: RetrofitBreedsService): BreedsService

        abstract fun sharedPrefBreedsRepository(): SharedPrefBreedsRepository

        @motif.Expose
        abstract fun breedsRepository(b: SharedPrefBreedsRepository): BreedsRepository
    }
}

interface AppScopeDep {
    fun application(): PuviApp
}
