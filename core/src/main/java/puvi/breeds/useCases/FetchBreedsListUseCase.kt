package puvi.breeds.useCases

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import puvi.breeds.Breed
import puvi.breeds.BreedsRepository
import puvi.breeds.BreedsService
import puvi.utils.Either
import puvi.utils.Left
import puvi.utils.Right

class FetchBreedsListUseCase(
    private val breedsService: BreedsService,
    private val breedsRepository: BreedsRepository
) {
    operator fun invoke(): Observable<Either<Throwable, Set<Breed>>> =
        observeListLocally()
            .take(1)
            .filter { it.isNotEmpty() }
            .concatWith(fetchListFromTheCloud())
            .concatWith(observeListLocally())
            .map { Right(it) as Either<Throwable, Set<Breed>> }
            .distinctUntilChanged()
            .onErrorReturn { Left(it) }

    private fun observeListLocally() =
        breedsRepository
            .observeBreedsList()
            .subscribeOn(Schedulers.io())

    private fun fetchListFromTheCloud() =
        breedsService
            .fetchBreedsList()
            .subscribeOn(Schedulers.io())
            .flatMap { breeds ->
                breedsRepository.saveBreedsList(breeds)
                    .subscribeOn(Schedulers.io())
                    .andThen(Single.just(breeds))
            }
}
