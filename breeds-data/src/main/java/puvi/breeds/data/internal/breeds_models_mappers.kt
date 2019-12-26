package puvi.breeds.data.internal

import puvi.NoConnectionException
import puvi.breeds.Breed
import puvi.breeds.BreedId
import puvi.breeds.FullBreedId
import puvi.breeds.SubBreed
import puvi.breeds.SubBreedId
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

internal fun BreedsResponse.toDomain() =
    message
        .entries
        .map { (breed, subBreeds) ->
            val breedId = BreedId(breed)
            val subs = subBreeds
                .map {
                    SubBreed(
                        id = SubBreedId(it),
                        parentId = breedId
                    )
                }
                .let {
                    if (it.isEmpty()) null
                    else it
                }
            Breed(
                id = breedId,
                subBreeds = subs
            )
        }.toSet()

internal fun Set<Breed>.toResponse() =
    map { breed ->
        val key = breed.id()

        val value = when (breed.subBreeds) {
            null -> emptyList()
            else -> breed.subBreeds!!.map { it.id() }
        }

        key to value
    }.toMap()
        .let { message ->
            BreedsResponse(message, status = "success")
        }

internal fun ImagesStore.toDomain(): Map<FullBreedId, Set<URL>> =
    map
        .entries
        .mapNotNull { (key, value) ->
            val newKey = ImagesStore.parseKey(key)
            when (newKey) {
                null -> null
                else -> newKey to value.map(::URL).toSet()
            }
        }
        .toMap()

internal fun Map<FullBreedId, Set<URL>>.toImagesStore() =
    ImagesStore(
        map = entries
            .map { (id, urls) ->
                ImagesStore.key(id) to urls.map(URL::toString).toSet()
            }
            .toMap()
    )

internal fun Throwable.toDomain() =
    when (this) {
        is SocketTimeoutException,
        is TimeoutException,
        is SocketException,
        is UnknownHostException -> NoConnectionException(this)
        is HttpException -> puvi.HttpException(code(), this)
        else -> this
    }
