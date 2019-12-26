package puvi

import android.app.Application
import puvi.breeds.ui.di.BreedDetailsScope
import puvi.breeds.ui.di.BreedsListScope
import puvi.di.AppScope
import puvi.di.AppScopeDep
import puvi.di.AppScopeImpl
import puvi.navigation.AppNavigator
import puvi.ui.navigation.Navigator

class PuviApp : Application() {
    override fun onCreate() {
        super.onCreate()

        buildNavigator()
        initDi()
    }

    private fun buildNavigator() {
        Navigator.instance = AppNavigator
    }

    private fun initDi() {
        AppScope.instance = AppScopeImpl(object : AppScopeDep {
            override fun application(): PuviApp = this@PuviApp
        })

        BreedsListScope.factory = { AppScope().breedsListScope() }
        BreedDetailsScope.factory = { AppScope().breedDetailsScope(it) }
    }
}
