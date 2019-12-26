package puvi.breeds.useCases

import puvi.breeds.Breed
import puvi.breeds.BreedId
import puvi.breeds.SubBreed
import puvi.breeds.SubBreedId

internal object Mother {
    val breedsList = setOf(
        Breed(
            id = BreedId("pug"),
            subBreeds = null
        ),
        Breed(
            id = BreedId("sheppard"),
            subBreeds = listOf(
                SubBreed(
                    parentId = BreedId("sheppard"),
                    id = SubBreedId("american")
                ),
                SubBreed(
                    parentId = BreedId("sheppard"),
                    id = SubBreedId("british")
                )
            )
        )
    )

    val cachedBreeds = breedsList.take(1).toSet()
}
