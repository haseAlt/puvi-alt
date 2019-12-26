package puvi.breeds

import java.net.URL

inline class BreedId(val v: String) {
    operator fun invoke() = v
}

inline class SubBreedId(val v: String) {
    operator fun invoke() = v
}

data class FullBreedId(val parentId: BreedId, val subId: SubBreedId?)

data class Breed(
    val id: BreedId,
    val subBreeds: List<SubBreed>?
) {
    val fullId = FullBreedId(id, null)
}

data class SubBreed(val parentId: BreedId, val id: SubBreedId) {
    val fullId = FullBreedId(parentId, id)
}

sealed class BreedImage(val id: FullBreedId, open val imageURL: URL?) {
    data class Breed(
        val parentId: BreedId,
        override val imageURL: URL?
    ) : BreedImage(FullBreedId(parentId, null), imageURL)

    data class SubBreed(
        val parentId: BreedId,
        val subId: SubBreedId,
        override val imageURL: URL?
    ) : BreedImage(FullBreedId(parentId, subId), imageURL)
}
