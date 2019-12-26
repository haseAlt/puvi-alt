package puvi.breeds.useCases

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import puvi.breeds.Breed
import puvi.breeds.BreedImage
import puvi.breeds.BreedsRepository
import puvi.breeds.BreedsService
import puvi.utils.Either
import puvi.utils.Left
import puvi.utils.Right
import puvi.utils.Some
import puvi.utils.toNullable
import puvi.utils.toOptional
import java.util.concurrent.TimeUnit

class FetchAllBreedImagesUseCase(
    private val fetchBreedsListUseCase: FetchBreedsListUseCase,
    private val breedsService: BreedsService,
    private val breedsRepository: BreedsRepository
) {
    operator fun invoke(): Observable<Either<Throwable, List<BreedImage>>> =
        fetchBreedsListUseCase()
            .throttleLatest(10, TimeUnit.SECONDS, true)
            .zipWith(Observable.interval(1, TimeUnit.SECONDS)) { a, _ -> a }
            .switchMap { either ->
                when (either) {
                    is Left -> Observable.error(either.value)
                    is Right -> {
                        val breeds = either.value
                        getImagesFromRepo(breeds)
                            .publish { breedImagesObs ->
                                breedImagesObs
                                    .mergeWith(autoFetchMissingBreedImages(breedImagesObs))
                            }
                            .map { Right(it) as Either<Throwable, List<BreedImage>> }
                            .onErrorReturn { Left(it) }
                    }
                }
            }
            .onErrorResumeNext { error: Throwable ->
                Observable.just(Left(error))
                    .concatWith(Observable.error(error))
            }
            .retryWhen { obs -> obs.zipWith(Observable.interval(10, TimeUnit.SECONDS)) }

    private fun getImagesFromRepo(breeds: Set<Breed>): Observable<List<BreedImage>> =
        breedsRepository.observeImages()
            .subscribeOn(Schedulers.io())
            .map { images ->
                breeds.map { breed ->
                    val mainBreedSet = images[breed.fullId]
                    val mainBreedImage = BreedImage.Breed(
                        parentId = breed.id,
                        imageURL = mainBreedSet?.firstOrNull()
                    )

                    val subBreedImages = breed.subBreeds?.map { subBreed ->
                        val subBreedSet = images[subBreed.fullId]
                        BreedImage.SubBreed(
                            parentId = breed.id,
                            subId = subBreed.id,
                            imageURL = subBreedSet?.firstOrNull()
                        )
                    } ?: emptyList()

                    listOf(mainBreedImage) + subBreedImages
                }.flatten()
            }

    private fun autoFetchMissingBreedImages(
        breedImagesObs: Observable<List<BreedImage>>
    ): Observable<List<BreedImage>> =
        breedImagesObs
            .delay(100, TimeUnit.MILLISECONDS)
            .map { breedImages ->
                breedImages.firstOrNull { it.imageURL == null }
                    ?.id
                    .toOptional()
            }
            .throttleLatest(100, TimeUnit.MILLISECONDS, true)
            .toFlowable(BackpressureStrategy.LATEST)
            .filter { it is Some }
            .map { it.toNullable()!! }
            .concatMap { breedId ->
                breedsService.fetchImagesForBreed(breedId)
                    .subscribeOn(Schedulers.io())
                    .timeout(30, TimeUnit.SECONDS)
                    .retryWhen { flow -> flow.zipWith(Flowable.interval(10, TimeUnit.SECONDS)) }
                    .flatMapCompletable { images ->
                        breedsRepository.insertImagesForBreed(breedId, images)
                            .subscribeOn(Schedulers.io())
                    }
                    .toFlowable<List<BreedImage>>()
            }
            .toObservable()
}
