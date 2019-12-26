package puvi.breeds.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Single
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import puvi.breeds.Breed
import puvi.breeds.BreedsService
import puvi.breeds.FullBreedId
import puvi.breeds.data.internal.Api
import puvi.breeds.data.internal.toDomain
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.net.URL

class RetrofitBreedsService(private val baseUrl: String = DEFAULT_BASEURL) : BreedsService {
    companion object {
        private const val DEFAULT_BASEURL = "https://dog.ceo/api/"
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }
    private val retrofit by lazy {
        val mediaType = "application/json".toMediaType()
        val json = Json(
            JsonConfiguration.Default.copy(
                strictMode = false
            )
        )
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(Api::class.java)
    }

    override fun fetchBreedsList(): Single<Set<Breed>> =
        retrofit.getAllBreeds()
            .map { response -> response.toDomain() }
            .onErrorResumeNext { Single.error(it.toDomain()) }

    override fun fetchImagesForBreed(id: FullBreedId): Single<Set<URL>> =
        when (id.subId) {
            null -> retrofit.getImagesForBreed(id.parentId())
            else -> retrofit.getImagesForSubBreed(id.parentId(), id.subId!!())
        }.map { response ->
            response.message
                .map(::URL)
                .toSet()
        }
            .onErrorResumeNext { Single.error(it.toDomain()) }
}
