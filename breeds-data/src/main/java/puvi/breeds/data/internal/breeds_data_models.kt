package puvi.breeds.data.internal

import kotlinx.serialization.Serializable
import puvi.breeds.BreedId
import puvi.breeds.FullBreedId
import puvi.breeds.SubBreedId

@Serializable
internal data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)

@Serializable
internal data class ImagesResponse(
    val message: List<String>,
    val status: String
)

@Serializable
internal data class ImagesStore(
    val map: Map<String, Set<String>>
) {
    companion object {
        fun key(id: FullBreedId) = "${id.parentId()}$${id.subId?.invoke()}"

        fun parseKey(key: String): FullBreedId? {
            val split = key.split("$")
            return try {
                require(split.size == 2)

                val rawBreedId = split[0]
                val rawSubId = split[1]

                val subId = when (rawSubId) {
                    "null" -> null
                    else -> SubBreedId(rawSubId)
                }

                FullBreedId(BreedId(rawBreedId), subId)
            } catch (_: Throwable) {
                null
            }
        }
    }
}
