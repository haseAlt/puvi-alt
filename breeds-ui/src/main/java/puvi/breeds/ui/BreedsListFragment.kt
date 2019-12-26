package puvi.breeds.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_breeds_list.*
import puvi.NoConnectionException
import puvi.breeds.BreedImage
import puvi.breeds.ui.di.BreedsListScope
import puvi.breeds.ui.internal.BreedDataUi
import puvi.breeds.ui.internal.BreedsListDogItem
import puvi.ui.di.viewModelInjector
import puvi.ui.navigation.Navigator
import puvi.ui.utils.showNoConnectionSnackbar
import puvi.ui.utils.showUnknownErrorSnackbar

class BreedsListFragment : Fragment(R.layout.fragment_breeds_list) {
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val viewModel by viewModelInjector {
        BreedsListScope().viewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BreedsListScope.build()

        with(breedsListRv) {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }

        viewModel.imagesLiveData.observe(viewLifecycleOwner, Observer { handleModel(it) })
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { handleError(it) })
    }

    private fun handleModel(model: List<BreedImage>?) {
        if (model == null) return

        fun isItemAtTheBeginningOrEnd(idx: Int) = idx < 2 || idx >= (model.size - 2)

        val items = model.mapIndexed { idx, it ->
            val data = BreedDataUi(
                id = it.id,
                breedName = it.id.parentId(),
                subBreedName = it.id.subId?.invoke(),
                imageUrl = it.imageURL,
                alwaysVisibleTitle = isItemAtTheBeginningOrEnd(idx)
            )
            BreedsListDogItem(data, viewLifecycleOwner.lifecycle) {
                Navigator().goToBreedDetail(findNavController(), data.id)
            }
        }

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
