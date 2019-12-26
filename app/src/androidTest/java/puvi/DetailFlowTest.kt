package puvi

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import puvi.breeds.BreedsService
import puvi.breeds.data.InMemoryBreedsRepository
import puvi.di.AppScope
import puvi.di.AppScopeDep
import puvi.di.AppScopeImpl

@RunWith(AndroidJUnit4::class)
class DetailFlowTest : TestCase() {
    private val m = DetailFlowTestMother

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Test
    fun fullFlow() = before {
        // Repository is a in-memory stub because it's easier than mocking it
        val repository = InMemoryBreedsRepository().apply {
            breeds.addAll(m.cachedBreeds)
            images.putAll(m.images)
        }
        val service = mockk<BreedsService> {
            every { fetchBreedsList() } returns Single.just(m.breedsList)
            every { fetchImagesForBreed(any()) } returns Single.just(m.imagesSet)
        }

        appScopeSpy {
            every { breedsRepository() } returns repository
            every { breedsService() } returns service
        }

        launchScenario()
    }.after {
    }.run {
        step("Open the screen and load content from cache") {
            BreedsListScreen {
                val list = m.cachedBreeds.toList()
                list.forEachIndexed { idx, breed ->
                    isBreedAtPositionCorrect(idx, breed.fullId)
                }
            }
        }

        Thread.sleep(11000)

        step("Content from cloud will load after 10 seconds") {
            BreedsListScreen {
                val list = m.breedsList.toList()
                list.forEachIndexed { idx, breed ->
                    isBreedAtPositionCorrect(idx, breed.fullId)
                }
            }
        }

        step("Clicking on an item will open the detail screen") {
            BreedsListScreen {
                clickOnItem(m.selectedBreedPosition)
            }
        }

        Thread.sleep(1000)

        step("Detail should show the breed name") {
            BreedDetailsScreen {
                isTitleVisible(m.selectedBreedId)
            }
        }

        step("All the images should be visible") {
            BreedDetailsScreen {
                isImageVisible(1)
            }
        }

        step("Pressing back returns to the list") {
            BreedDetailsScreen {
                pressBack()
            }

            BreedsListScreen {
                val list = m.breedsList.toList()
                list.forEachIndexed { idx, breed ->
                    isBreedAtPositionCorrect(idx, breed.fullId)
                }
            }
        }
    }

    private fun launchScenario() {
        activityRule.launchActivity(null)
        Thread.sleep(1000)
    }

    private fun appScopeSpy(f: AppScopeImpl.() -> Unit) {
        val appScopeReal = AppScopeImpl(object : AppScopeDep {
            override fun application(): PuviApp =
                InstrumentationRegistry.getInstrumentation()
                    .targetContext
                    .applicationContext as PuviApp
        })

        AppScope.instance = spyk(appScopeReal, block = f)
    }
}
