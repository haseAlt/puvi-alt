package puvi.breeds.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import puvi.breeds.Breed
import puvi.breeds.BreedsRepository
import puvi.breeds.FullBreedId
import java.net.URL

class InMemoryBreedsRepository : BreedsRepository {
    private val breedsSubj = PublishSubject.create<Unit>()
    private val imagesSubj = PublishSubject.create<Unit>()

    val breeds = mutableSetOf<Breed>()
    val images = mutableMapOf<FullBreedId, Set<URL>>()

    override fun observeBreedsList(): Observable<Set<Breed>> =
        breedsSubj
            .startWith(Unit)
            .map { breeds.toSet() }

    override fun saveBreedsList(breeds: Set<Breed>): Completable =
        Completable.fromCallable {
            this.breeds.clear()
            this.breeds.addAll(breeds)
            breedsSubj.onNext(Unit)
        }

    override fun observeImages(): Observable<Map<FullBreedId, Set<URL>>> =
        imagesSubj
            .startWith(Unit)
            .map { images.toMap() }

    override fun insertImagesForBreed(id: FullBreedId, imageURLs: Set<URL>): Completable =
        Completable.fromCallable {
            val oldSet = images.getOrElse(id) { emptySet() }
            this.images += id to (oldSet + imageURLs)
            imagesSubj.onNext(Unit)
        }

    override fun clearAll(): Completable = Completable.fromCallable {
        breeds.clear()
        images.clear()
        breedsSubj.onNext(Unit)
        imagesSubj.onNext(Unit)
    }
}
