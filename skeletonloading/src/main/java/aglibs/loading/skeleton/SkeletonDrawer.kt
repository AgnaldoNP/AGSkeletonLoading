package aglibs.loading.skeleton

import android.animation.ValueAnimator
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SkeletonDrawer(private val view: View) : ValueAnimator.AnimatorUpdateListener {

    private lateinit var valueAnimator: ValueAnimator

    internal var skeletonPath: Path
    internal var skeletonPaint: Paint
    internal var skeletonEffectPath: Path
    internal var skeletonEffectPaint: Paint

    companion object {
        const val DEFAULT_DURATION_MILLIS = 1200
        const val ROUND_PIXELS = 10F
        const val STROKE_WIDTH = 30F
        const val BLUR_WIDTH = 50F
        var BASE_COLOR = Color.rgb(230, 230, 230)
    }

    init {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        valueAnimator = AnimationUtils.getValueAnimator(updateListener = this)

        skeletonPath = Path()
        skeletonEffectPath = Path()

        skeletonPaint = Paint().apply {
            style = Paint.Style.FILL
            color = BASE_COLOR
        }

        skeletonEffectPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            color = AnimationUtils.lightenColor(BASE_COLOR, 0.2F)
            maskFilter = BlurMaskFilter(BLUR_WIDTH, BlurMaskFilter.Blur.NORMAL);
        }
    }

    internal fun getStyles(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let {

        }
    }

    fun isLoading() = valueAnimator.isRunning

    fun startLoading() {
        valueAnimator.start()
    }

    fun stopLoading() {
        valueAnimator.cancel()
        valueAnimator.end()
        view.invalidate()
    }

    override fun onAnimationUpdate(animator: ValueAnimator) {
        val animationProgress = animator.animatedValue as Float
        createSkeleton(animationProgress)
        view.invalidate()
    }

    internal fun createSkeleton(progress: Float) {
        skeletonPath.reset()
        skeletonPath.addRoundRect(
            0F, 0F, view.width.toFloat(), view.height.toFloat(),
            ROUND_PIXELS, ROUND_PIXELS, Path.Direction.CCW
        )

        val x = view.width * progress
        skeletonEffectPath.reset()
        skeletonEffectPath.moveTo(x, 0f)
        skeletonEffectPath.lineTo(x, view.height.toFloat())
    }

    fun draw(canvas: Canvas) {
        canvas.drawPath(skeletonPath, skeletonPaint);
        canvas.drawPath(skeletonEffectPath, skeletonEffectPaint);
    }

}
