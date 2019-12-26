package puvi.breeds

import io.reactivex.Single
import java.net.URL

interface BreedsService {
    fun fetchBreedsList(): Single<Set<Breed>>

    fun fetchImagesForBreed(id: FullBreedId): Single<Set<URL>>
}
