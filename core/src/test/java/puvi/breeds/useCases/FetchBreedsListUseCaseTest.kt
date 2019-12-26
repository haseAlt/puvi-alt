package puvi.breeds.useCases

import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.spekframework.spek2.Spek
import puvi.NoConnectionException
import puvi.breeds.Breed
import puvi.breeds.BreedsRepository
import puvi.breeds.BreedsService
import puvi.utils.Left
import puvi.utils.Right

object FetchBreedsListUseCaseTest : Spek({
    val repository = mockk<BreedsRepository>()
    val service = mockk<BreedsService>()

    val uc = FetchBreedsListUseCase(service, repository)

    group("given there's no cache and service returns OK") {
        beforeEachTest {
            clearMocks(repository, service)
            every { repository.observeBreedsList() } returns Observable.just(emptySet<Breed>())
            every { repository.saveBreedsList(any()) } returns Completable.complete()
            every { service.fetchBreedsList() } returns Single.just(Mother.breedsList)
        }

        test("it should return a list of breeds") {
            uc().blockingFirst() shouldBe Right(Mother.breedsList)
        }

        test("it should save the list from the cloud") {
            uc().blockingFirst() shouldBe Right(Mother.breedsList)

            verify {
                service.fetchBreedsList()
                repository.saveBreedsList(Mother.breedsList)
            }
        }
    }

    group("given there's cache for the breed list and service is OK") {
        beforeEachTest {
            clearMocks(repository, service)
            every { repository.observeBreedsList() } returns Observable.just(Mother.cachedBreeds)
            every { repository.saveBreedsList(any()) } returns Completable.complete()
            every { service.fetchBreedsList() } returns Single.just(Mother.breedsList)
        }

        test("it should return a list of breeds from cache") {
            uc().blockingFirst() shouldBe Right(Mother.cachedBreeds)
        }

        test("it should still save the list from the cloud") {
            uc().buffer(2).blockingFirst() shouldBe listOf(
                Right(Mother.cachedBreeds),
                Right(Mother.breedsList)
            )

            verify {
                service.fetchBreedsList()
                repository.saveBreedsList(Mother.breedsList)
            }
        }
    }

    group("given the service is not OK") {
        beforeEachTest {
            clearMocks(repository, service)
            every { repository.observeBreedsList() } returns Observable.just(Mother.cachedBreeds)
            every { repository.saveBreedsList(any()) } returns Completable.complete()
            every { service.fetchBreedsList() } returns Single.error(NoConnectionException())
        }

        test("it should return a list of breeds from cache") {
            uc().blockingFirst() shouldBe Right(Mother.cachedBreeds)
        }

        test("it should return an error after a list from the cache") {
            val result = uc().buffer(2).blockingFirst()
            result.first() shouldBe Right(Mother.cachedBreeds)

            (result.drop(1).first() as Left<Throwable>)
                .value.shouldBeInstanceOf<NoConnectionException>()
        }
    }
})
