package puvi.breeds.ui.internal

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.squareup.picasso.Transformation

object GrayScaleTransformation : Transformation {
    override fun key() = "GrayScaleTransformation"

    override fun transform(source: Bitmap): Bitmap {
        val width: Int = source.width
        val height: Int = source.height

        val bitmap: Bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            val saturation = ColorMatrix().apply {
                setSaturation(0f)
            }
            colorFilter = ColorMatrixColorFilter(saturation)
        }

        canvas.drawBitmap(source, 0f, 0f, paint)
        source.recycle()

        return bitmap
    }
}
