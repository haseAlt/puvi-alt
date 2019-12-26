package puvi.breeds.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_breed_details.*
import puvi.NoConnectionException
import puvi.breeds.BreedId
import puvi.breeds.FullBreedId
import puvi.breeds.SubBreedId
import puvi.breeds.ui.di.BreedDetailsScope
import puvi.breeds.ui.internal.BreedFullScreenImageItem
import puvi.breeds.ui.internal.BreedTitleItem
import puvi.ui.di.viewModelInjector
import puvi.ui.utils.showNoConnectionSnackbar
import puvi.ui.utils.showUnknownErrorSnackbar
import java.net.URL

class BreedDetailsFragment : Fragment(R.layout.fragment_breed_details) {
    companion object {
        private const val BKEY_BREED_ID = "breedId"
        private const val BKEY_SUB_ID = "subId"
    }

    private val breedId by lazy {
        with(arguments!!) {
            val breedId = BreedId(getString(BKEY_BREED_ID)!!)
            val subId = getString(BKEY_SUB_ID)?.let(::SubBreedId)
            FullBreedId(breedId, subId)
        }
    }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val viewModel by viewModelInjector {
        BreedDetailsScope().viewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BreedDetailsScope.build(breedId)

        with(breedDetailsViewPager) {
            adapter = groupAdapter
        }

        viewModel.imagesLiveData.observe(viewLifecycleOwner, Observer { handleImages(it) })
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { handleError(it) })

        breedDetailsViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    with(breedDetailsProgressBar) {
                        max = groupAdapter.itemCount
                        progress = breedDetailsViewPager.currentItem + 1
                    }
                }
            })
    }

    private fun handleImages(images: Set<URL>?) {
        if (images == null) return

        val imagesItems = images
            .map { imageUrl -> BreedFullScreenImageItem(imageUrl) }

        val items = listOf(
            BreedTitleItem(
                breedName = breedId.parentId(),
                subBreedName = breedId.subId?.invoke()
            )
        ) + imagesItems

        groupAdapter.update(items)
    }

    private fun handleError(error: Throwable?) {
        if (error == null) return

        when (error) {
            is NoConnectionException -> showNoConnectionSnackbar()
            else -> showUnknownErrorSnackbar()
        }
    }
}
