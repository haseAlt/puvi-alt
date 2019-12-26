package puvi.breeds.data

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import puvi.breeds.Breed
import puvi.breeds.BreedsRepository
import puvi.breeds.FullBreedId
import puvi.breeds.data.internal.BreedsResponse
import puvi.breeds.data.internal.ImagesStore
import puvi.breeds.data.internal.toDomain
import puvi.breeds.data.internal.toImagesStore
import puvi.breeds.data.internal.toResponse
import java.net.URL

class SharedPrefBreedsRepository(
    private val context: Context
) : BreedsRepository {

    companion object {
        private const val DB_NAME = "breeds-repo"

        private const val KEY_BREEDS = "breeds"
        private const val KEY_IMAGES = "images"

        private val imagesLock = Any()
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)
    }

    private val json by lazy {
        val config = JsonConfiguration(strictMode = false)
        Json(config)
    }

    override fun observeBreedsList(): Observable<Set<Breed>> =
        sharedPref.observeChanges()
            .startWith(Unit)
            .map {
                val rawJson = sharedPref.getString(KEY_BREEDS, null)
                if (rawJson == null) {
                    emptySet()
                } else {
                    val response = json.parse(BreedsResponse.serializer(), rawJson)
                    response.toDomain()
                }
            }
            .onErrorReturnItem(emptySet())

    override fun saveBreedsList(breeds: Set<Breed>): Completable =
        Completable.fromCallable {
            val rawJson = json.stringify(BreedsResponse.serializer(), breeds.toResponse())
            sharedPref.edit().putString(
                KEY_BREEDS,
                rawJson
            ).commit()
        }

    override fun observeImages(): Observable<Map<FullBreedId, Set<URL>>> =
        sharedPref.observeChanges()
            .startWith(Unit)
            .map {
                val rawJson = sharedPref.getString(KEY_IMAGES, null)

                when (rawJson) {
                    null -> emptyMap()
                    else -> {
                        val store = json.parse(ImagesStore.serializer(), rawJson)
                        store.toDomain()
                    }
                }
            }
            .onErrorReturnItem(emptyMap())

    override fun insertImagesForBreed(id: FullBreedId, imageURLs: Set<URL>): Completable =
        Completable.fromCallable {
            synchronized(imagesLock) {
                val rawJson = sharedPref.getString(KEY_IMAGES, null)

                val map = when (rawJson) {
                    null -> emptyMap()
                    else -> {
                        val store = json.parse(ImagesStore.serializer(), rawJson)
                        store.toDomain()
                    }
                }
                val oldImages = map[id] ?: emptySet()

                val newMap = map + (id to oldImages + imageURLs)
                val newStore = newMap.toImagesStore()
                val newRawJson = json.stringify(ImagesStore.serializer(), newStore)

                sharedPref.edit().putString(KEY_IMAGES, newRawJson).commit()
            }
        }

    override fun clearAll(): Completable = Completable.fromCallable {
        sharedPref.edit().clear().commit()
    }

    private fun SharedPreferences.observeChanges() =
        Observable.create<Unit> { emitter ->
            val listener = SharedPreferences
                .OnSharedPreferenceChangeListener { _, _ ->
                    emitter.onNext(Unit)
                }
            emitter.setCancellable { unregisterOnSharedPreferenceChangeListener(listener) }
            registerOnSharedPreferenceChangeListener(listener)
        }
}
