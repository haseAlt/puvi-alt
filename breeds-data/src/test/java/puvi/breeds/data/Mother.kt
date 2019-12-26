package puvi.breeds.data

import puvi.breeds.Breed
import puvi.breeds.BreedId
import puvi.breeds.SubBreed
import puvi.breeds.SubBreedId
import java.net.URL

internal object Mother {
    val breeds = setOf(
        Breed(
            id = BreedId("african"),
            subBreeds = null
        ),
        Breed(
            id = BreedId("australian"),
            subBreeds = listOf(
                SubBreed(
                    parentId = BreedId("australian"),
                    id = SubBreedId("shepherd")
                )
            )
        )
    )

    val africanId = breeds.first().fullId

    val africanImages = setOf(
        URL("https://images.dog.ceo/breeds/african/n02086910_10147.jpg"),
        URL("https://images.dog.ceo/breeds/african/n02086910_10147.jpg")
    )
}
