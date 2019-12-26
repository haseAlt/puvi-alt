package puvi.ui.utils

import androidx.navigation.NavController

fun NavController.navigateFromThisScreenOnly(
    currentId: Int,
    navAction: NavController.() -> Unit
) {
    if (currentDestination?.id == currentId) {
        navAction.invoke(this)
    }
}
