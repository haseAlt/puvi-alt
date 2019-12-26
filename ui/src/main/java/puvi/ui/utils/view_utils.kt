package puvi.ui.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

val RecyclerView.ViewHolder.context: Context
    get() = itemView.context

fun Fragment.showNoConnectionSnackbar() =
    view?.let { view ->
        Snackbar.make(view, "No connection", Snackbar.LENGTH_SHORT)
            .show()
    }

fun Fragment.showUnknownErrorSnackbar() =
    view?.let { view ->
        Snackbar.make(view, "Ops, something went wrong, try again later", Snackbar.LENGTH_SHORT)
            .show()
    }
