package puvi.breeds

import io.reactivex.Completable
import io.reactivex.Observable
import java.net.URL

interface BreedsRepository {
    fun observeBreedsList(): Observable<Set<Breed>>
    fun saveBreedsList(breeds: Set<Breed>): Completable

    fun observeImages(): Observable<Map<FullBreedId, Set<URL>>>
    fun insertImagesForBreed(id: FullBreedId, imageURLs: Set<URL>): Completable

    fun clearAll(): Completable
}
