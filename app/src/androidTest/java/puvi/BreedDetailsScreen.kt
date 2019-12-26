package puvi

import android.view.View
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import dev.hasegawa.puvi.R
import org.hamcrest.Matcher
import puvi.breeds.FullBreedId

object BreedDetailsScreen : Screen<BreedDetailsScreen>() {
    class TitleItem(parent: Matcher<View>) : KRecyclerItem<TitleItem>(parent) {
        val title = KTextView(parent) { withId(R.id.breedDetailTitleTv) }
        val subTitle = KTextView(parent) { withId(R.id.breedDetailSubTitleTv) }
    }

    class ImageItem(parent: Matcher<View>) : KRecyclerItem<ImageItem>(parent) {
        val image = KImageView(parent) { withId(R.id.breedDetailIv) }
    }

    val rv = KRecyclerView({
        withParent {
            withId(R.id.breedDetailsViewPager)
        }
    }, itemTypeBuilder = {
        itemType(::TitleItem)
        itemType(::ImageItem)
    })

    fun isTitleVisible(id: FullBreedId) {
        rv {
            firstChild<TitleItem> {
                title.hasText(id.parentId())
                id.subId?.let {
                    subTitle.hasText(it())
                }
            }
        }
    }

    fun isImageVisible(position: Int) {
        rv {
            childAt<ImageItem>(position) {
                image.isVisible()
            }
        }
    }
}
