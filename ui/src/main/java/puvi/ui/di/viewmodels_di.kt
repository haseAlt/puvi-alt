package puvi.ui.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ViewModelInjectorDelegate<VM : ViewModel>(
    private val fragment: Fragment,
    private val activityScoped: Boolean,
    private val viewModelKlass: KClass<VM>,
    private val factory: () -> VM
) {
    private var savedValue: Any? = null

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): VM {
        if (savedValue == null) {
            savedValue = createValue()
        }
        return savedValue as VM
    }

    private fun createValue(): Any? {
        return try {
            val vmFactory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return factory() as T
                }
            }
            val provider = when (activityScoped) {
                true -> ViewModelProvider(fragment.activity!!, vmFactory)
                false -> ViewModelProvider(fragment, vmFactory)
            }
            provider[viewModelKlass.java]
        } catch (t: Throwable) {
            throw IllegalStateException(
                "ViewModel [${viewModelKlass.java.simpleName}] not found in DI system. " +
                        "Did you remember to add it to the ViewModelsModule?",
                t
            )
        }
    }
}

class ViewModelActivityInjectorDelegate<VM : ViewModel>(
    private val activity: FragmentActivity,
    private val viewModelKlass: KClass<VM>,
    private val factory: () -> VM
) {
    private var savedValue: Any? = null

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): VM {
        if (savedValue == null) {
            savedValue = createValue()
        }
        return savedValue as VM
    }

    private fun createValue(): Any? {
        val vmFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return factory() as T
            }
        }
        return try {
            val provider = ViewModelProvider(activity, vmFactory)
            provider[viewModelKlass.java]
        } catch (t: Throwable) {
            throw IllegalStateException(
                "ViewModel [${viewModelKlass.java.simpleName}] not found in DI system. " +
                        "Did you remember to add it to the ViewModelsModule?",
                t
            )
        }
    }
}

inline fun <reified T : ViewModel> Fragment.viewModelInjector(
    activityScoped: Boolean = false,
    noinline factory: () -> T
): ViewModelInjectorDelegate<T> =
    ViewModelInjectorDelegate(this, activityScoped, T::class, factory)

inline fun <reified T : ViewModel> FragmentActivity.viewModelInjector(
    noinline factory: () -> T
): ViewModelActivityInjectorDelegate<T> =
    ViewModelActivityInjectorDelegate(this, T::class, factory)
