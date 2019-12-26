package puvi.breeds.ui.internal

import puvi.breeds.FullBreedId
import java.net.URL

data class BreedDataUi(
    val id: FullBreedId,
    val breedName: String,
    val subBreedName: String?,
    val imageUrl: URL?,
    val alwaysVisibleTitle: Boolean
)
