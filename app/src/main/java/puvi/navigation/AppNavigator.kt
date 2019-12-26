package puvi.navigation

import androidx.navigation.NavController
import dev.hasegawa.puvi.R
import puvi.breeds.FullBreedId
import puvi.breeds.ui.BreedsListFragmentDirections
import puvi.ui.navigation.Navigator
import puvi.ui.utils.navigateFromThisScreenOnly

object AppNavigator : Navigator {
    override fun goToBreedDetail(navController: NavController, id: FullBreedId) {
        navController.navigateFromThisScreenOnly(R.id.breedsListFragment) {
            BreedsListFragmentDirections
                .actionBreedsListFragmentToBreedDetailsFragment(
                    breedId = id.parentId(),
                    subId = id.subId?.v
                )
                .let(this::navigate)
        }
    }
}
