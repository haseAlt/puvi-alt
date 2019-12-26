package puvi.ui.navigation

import androidx.navigation.NavController
import puvi.breeds.FullBreedId

interface Navigator {
    companion object {
        var instance: Navigator? = null

        operator fun invoke() = instance!!
    }

    fun goToBreedDetail(navController: NavController, id: FullBreedId)
}
