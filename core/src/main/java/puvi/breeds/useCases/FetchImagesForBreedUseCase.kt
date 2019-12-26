package puvi.breeds.useCases

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import puvi.breeds.BreedsRepository
import puvi.breeds.BreedsService
import puvi.breeds.FullBreedId
import puvi.utils.Either
import puvi.utils.Left
import puvi.utils.Right
import java.net.URL
import java.util.concurrent.TimeUnit

class FetchImagesForBreedUseCase(
    private val breedsService: BreedsService,
    private val breedsRepository: BreedsRepository
) {
    operator fun invoke(id: FullBreedId): Observable<Either<Throwable, Set<URL>>> =
        breedsRepository.observeImages()
            .take(1)
            .map { allImages -> allImages[id] ?: emptySet() }
            .concatWith(
                breedsService.fetchImagesForBreed(id)
                    .subscribeOn(Schedulers.io())
                    .flatMap { images ->
                        breedsRepository.insertImagesForBreed(id, images)
                            .andThen(Single.just(images))
                    }
                    .toObservable()
            )
            .map { Right(it) as Either<Throwable, Set<URL>> }
            .onErrorResumeNext { error: Throwable ->
                Observable.just(Left(error))
                    .concatWith(Observable.error(error))
            }
            .retryWhen {
                it.zipWith(Observable.interval(10, TimeUnit.SECONDS))
            }
}
