package puvi.breeds.data.internal

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

internal interface Api {
    @GET("breeds/list/all")
    fun getAllBreeds(): Single<BreedsResponse>

    @GET("breed/{breed}/images")
    fun getImagesForBreed(@Path("breed") breed: String): Single<ImagesResponse>

    @GET("breed/{breed}/{subbreed}/images")
    fun getImagesForSubBreed(
        @Path("breed") breed: String,
        @Path("subbreed") subBreed: String
    ): Single<ImagesResponse>
}
