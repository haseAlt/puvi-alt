package puvi.breeds.ui.internal

import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.Lifecycle
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_breed_detail_image.*
import kotlinx.android.synthetic.main.item_breed_detail_title.*
import kotlinx.android.synthetic.main.item_breed_detail_title.view.*
import kotlinx.android.synthetic.main.item_breed_list_dog.view.*
import puvi.breeds.ui.R
import java.net.URL

internal class BreedsListDogItem(
    private val data: BreedDataUi,
    private val uiLifecycle: Lifecycle,
    private val onClick: () -> Unit
) : com.xwray.groupie.Item<BreedItemVH>() {

    override fun bind(viewHolder: BreedItemVH, position: Int): Unit = with(viewHolder.itemView) {
        viewHolder.alwaysVisibleTitle = data.alwaysVisibleTitle
        dogBreedTv.text = data.breedName
        dogSubBreedTv.text = data.subBreedName

        if (data.imageUrl != null) {
            Picasso
                .get()
                .load(data.imageUrl.toString())
                .fit()
                .centerCrop()
                .transform(GrayScaleTransformation)
                .placeholder(R.color.BreedsListEmptyPlaceholder)
                .into(dogIv)
        } else {
            dogIv.setImageDrawable(null)
        }

        dogContainer.setOnClickListener { onClick() }
    }

    override fun getLayout(): Int = R.layout.item_breed_list_dog

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean =
        (other as? BreedsListDogItem)?.data?.id == data.id

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>): Boolean =
        (other as? BreedsListDogItem)?.data == data

    override fun createViewHolder(itemView: View): BreedItemVH = BreedItemVH(itemView, uiLifecycle)
}

internal data class BreedTitleItem(
    private val breedName: String,
    private val subBreedName: String?
) : Item(10) {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) = viewHolder.run {
        breedDetailTitleTv.text = breedName
        breedDetailSubTitleTv.text = subBreedName
    }

    override fun getLayout(): Int = R.layout.item_breed_detail_title

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>): Boolean =
        (other as? BreedTitleItem)?.let {
            it.breedName == breedName && it.subBreedName == subBreedName
        } ?: false

    override fun onViewAttachedToWindow(viewHolder: GroupieViewHolder) {
        val view = viewHolder.itemView.breedDetailTitleContainer
        view.translationY = -300f
        view.alpha = 0f
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setInterpolator(DecelerateInterpolator())
            .duration = 1000
    }
}

internal data class BreedFullScreenImageItem(
    private val imageUrl: URL
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) = viewHolder.run {
        Picasso
            .get()
            .load(imageUrl.toString())
            .transform(GrayScaleTransformation)
            .into(breedDetailIv)
    }

    override fun getLayout(): Int = R.layout.item_breed_detail_image

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean =
        (other as? BreedFullScreenImageItem)?.imageUrl == imageUrl

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>): Boolean = isSameAs(other)
}
