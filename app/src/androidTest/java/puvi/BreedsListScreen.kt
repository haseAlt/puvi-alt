package puvi

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import dev.hasegawa.puvi.R
import org.hamcrest.Matcher
import puvi.breeds.FullBreedId

object BreedsListScreen : Screen<BreedsListScreen>() {
    class DogItem(parent: Matcher<View>) : KRecyclerItem<DogItem>(parent) {
        val title = KTextView(parent) { withId(R.id.dogBreedTv) }
        val subTitle = KTextView(parent) { withId(R.id.dogSubBreedTv) }
    }

    val rv = KRecyclerView({
        withId(R.id.breedsListRv)
    }, itemTypeBuilder = {
        itemType(::DogItem)
    })

    fun isBreedAtPositionCorrect(position: Int, id: FullBreedId) {
        rv {
            childAt<DogItem>(position) {
                title.hasText(id.parentId())
                id.subId?.let {
                    subTitle.hasText(it())
                }
            }
        }
    }

    fun clickOnItem(position: Int) {
        rv {
            childAt<DogItem>(position) {
                click()
            }
        }
    }
}
