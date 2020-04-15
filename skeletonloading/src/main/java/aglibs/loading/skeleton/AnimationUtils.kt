package aglibs.loading.skeleton

import android.animation.ValueAnimator
import android.graphics.Color
import android.view.animation.AccelerateDecelerateInterpolator

class AnimationUtils {

    companion object {

        fun getValueAnimator(
            reverse: Boolean = false,
            repeatCount: Int = ValueAnimator.INFINITE,
            repeatMode: Int = ValueAnimator.RESTART,
            duration: Int = SkeletonTextView.DEFAULT_DURATION_MILLIS,
            updateListener: ValueAnimator.AnimatorUpdateListener? = null
        ): ValueAnimator {
            val valueAnimator =
                if (reverse) ValueAnimator.ofFloat(1f, 0f) else ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.repeatCount = repeatCount
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.repeatMode = repeatMode
            valueAnimator.duration = duration.toLong()
            valueAnimator.addUpdateListener(updateListener)
            return valueAnimator
        }

        private fun colorToHSL(color: Int, hsl: FloatArray = FloatArray(3)): FloatArray {
            val r = Color.red(color) / 255f
            val g = Color.green(color) / 255f
            val b = Color.blue(color) / 255f

            val max = Math.max(r, Math.max(g, b))
            val min = Math.min(r, Math.min(g, b))
            hsl[2] = (max + min) / 2

            if (max == min) {
                hsl[1] = 0f
                hsl[0] = hsl[1]

            } else {
                val d = max - min

                hsl[1] = if (hsl[2] > 0.5f) d / (2f - max - min) else d / (max + min)
                when (max) {
                    r -> hsl[0] = (g - b) / d + (if (g < b) 6 else 0)
                    g -> hsl[0] = (b - r) / d + 2
                    b -> hsl[0] = (r - g) / d + 4
                }
                hsl[0] /= 6f
            }
            return hsl
        }

        private fun hslToColor(hsl: FloatArray): Int {
            val r: Float
            val g: Float
            val b: Float

            val h = hsl[0]
            val s = hsl[1]
            val l = hsl[2]

            if (s == 0f) {
                b = l
                g = b
                r = g
            } else {
                val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
                val p = 2 * l - q
                r = hue2rgb(p, q, h + 1f / 3)
                g = hue2rgb(p, q, h)
                b = hue2rgb(p, q, h - 1f / 3)
            }

            return Color.rgb((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())
        }

        private fun hue2rgb(p: Float, q: Float, t: Float): Float {
            var valueT = t
            if (valueT < 0) valueT += 1f
            if (valueT > 1) valueT -= 1f
            if (valueT < 1f / 6) return p + (q - p) * 6f * valueT
            if (valueT < 1f / 2) return q
            return if (valueT < 2f / 3) p + (q - p) * (2f / 3 - valueT) * 6f else p
        }


        /**
         * @param color color to be lightened
         * @param value range from 0.0 to 1.0
         *              where 0.0 represents no change and 1 turns the color
         *              white due to lightness to maximum.The maximum value
         *              could depends on the value for lightness from the original color.
         *              For strong colors like #F00, #0F0 and  #00F the effective maximum
         *              value is 0.5
         *
         * @return new color after be lightened
         */
        fun lightenColor(
            color: Int,
            value: Float
        ): Int {
            val hsl = colorToHSL(color)
            hsl[2] += value
            hsl[2] = 0f.coerceAtLeast(hsl[2].coerceAtMost(1f))
            return hslToColor(hsl)
        }

        /**
         * @param color color to be darkened
         * @param value range from 0.0 to 1
         *              where 0.0 represents no change and 1 turns the color
         *              black due to lightness to minimum. The maximum value
         *              could depends on the value for lightness from the original color.
         *              For strong colors like #F00, #0F0 and  #00F the effective maximum
         *              value is 0.5
         *
         * @return new color after be darkened
         */
        fun darkenColor(
            color: Int,
            value: Float
        ): Int {
            val hsl = colorToHSL(color)
            hsl[2] -= value
            hsl[2] = 0f.coerceAtLeast(hsl[2].coerceAtMost(1f))
            return hslToColor(hsl)
        }

    }

}
