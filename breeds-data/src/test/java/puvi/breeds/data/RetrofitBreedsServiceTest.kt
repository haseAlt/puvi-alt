package puvi.breeds.data

import io.kotlintest.shouldBe
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.spekframework.spek2.Spek
import puvi.NoConnectionException

object RetrofitBreedsServiceTest : Spek({
    group("given service is behaving correctly") {
        val mockWebServer = MockWebServer().apply {
            start()

            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse =
                    when (request.path) {
                        "/api/breeds/list/all" ->
                            MockResponse().setResponseCode(200)
                                .setBody(getJson("breeds_list_response.json"))
                        "/api/breed/african/images" ->
                            MockResponse().setResponseCode(200)
                                .setBody(getJson("african_images_response.json"))
                        else -> error("Unknown path ${request.path}")
                    }
            }
        }

        val baseUrl = mockWebServer.url("api/")
        val service = RetrofitBreedsService(baseUrl.toString())

        afterGroup { mockWebServer.shutdown() }

        test("fetching list of breeds should match expected result") {
            service.fetchBreedsList().blockingGet() shouldBe Mother.breeds
        }

        test("fetching images for breed african should match expected result") {
            service.fetchImagesForBreed(Mother.africanId)
                .blockingGet() shouldBe Mother.africanImages
        }
    }

    group("given user is not connected") {
        val service = RetrofitBreedsService("https://127.0.0.1:4921/")

        test("fetching list should throw NoConnectionException") {
            service.fetchBreedsList().test()
                .assertError { it is NoConnectionException }
        }

        test("fetching images should throw NoConnectionException") {
            service.fetchImagesForBreed(Mother.africanId).test()
                .assertError { it is NoConnectionException }
        }
    }
})
