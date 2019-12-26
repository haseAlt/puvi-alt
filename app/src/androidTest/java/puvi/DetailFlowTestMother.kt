package puvi

import puvi.breeds.Breed
import puvi.breeds.BreedId
import puvi.breeds.SubBreed
import puvi.breeds.SubBreedId
import java.net.URL

internal object DetailFlowTestMother {
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

    val selectedBreedPosition = 1
    val selectedBreedId = breedsList.toList()[selectedBreedPosition].fullId

    val imagesSet = setOf(URL("https://example.com/image.jpg"))

    val images = mapOf(selectedBreedId to imagesSet)
}
