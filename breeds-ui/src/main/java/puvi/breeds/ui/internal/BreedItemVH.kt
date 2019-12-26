package puvi.breeds.ui.internal

import android.app.Activity
import android.graphics.Point
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_breed_list_dog.view.*
import puvi.ui.utils.context
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class BreedItemVH(
    view: View,
    lifecycle: Lifecycle
) : GroupieViewHolder(view) {
    var alwaysVisibleTitle: Boolean by Delegates.observable(false) { _, _, newValue ->
        textVisibility(newValue)
    }

    private val locArr = IntArray(2)
    private val point = Point()
    private var tickDisposable: Disposable? = null

    init {
        textVisibility(false)

        (context as Activity)
            .windowManager
            .defaultDisplay
            .getSize(point)

        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                tickDisposable = Observable.interval(48, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { tick() }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                textVisibility(false)
                tickDisposable?.dispose()
                tickDisposable = null
            }
        })
    }

    private fun textVisibility(visible: Boolean) {
        val visibility = when (visible) {
            true -> View.VISIBLE
            false -> View.INVISIBLE
        }
        with(itemView) {
            dogBreedTv.visibility = visibility
            dogSubBreedTv.visibility = visibility
        }
    }

    private fun tick() = itemView.run {
        println("ticking")
        val range = 0.2f
        val rangeALow = 0.1f
        val rangeAHigh = rangeALow + range
        val rangeBLow = rangeAHigh + 0.1f
        val rangeBHigh = rangeBLow + range

        fun setAlpha(a: Float) {
            dogSubBreedTv.alpha = a
            dogBreedTv.alpha = a
        }

        if (alwaysVisibleTitle) {
            setAlpha(1f)
            return
        }

        dogContainer.getLocationOnScreen(locArr)
        val along = locArr[1].toFloat() / point.y.toFloat()

        val alpha = when (along) {
            in -Float.MAX_VALUE..rangeALow -> 0f
            in rangeBHigh..Float.MAX_VALUE -> 0f
            in rangeALow..rangeAHigh -> (along - rangeALow) / range
            in rangeAHigh..rangeBLow -> 1f
            in rangeBLow..rangeBHigh -> (1f - (along - rangeBLow) / range)
            else -> 1f
        }.coerceIn(0f, 1f)

        setAlpha(alpha)
        textVisibility(true)
    }
}
