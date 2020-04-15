package aglibs.loading.skeleton

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class SkeletonTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), AnimatorUpdateListener {

    private lateinit var valueAnimator: ValueAnimator
    private lateinit var skeletonPath: Path
    private lateinit var skeletonPaint: Paint
    private lateinit var skeletonEffectPath: Path
    private lateinit var skeletonEffectPaint: Paint

    companion object {
        const val DEFAULT_DURATION_MILLIS = 1500
        const val ROUND_PIXELS = 10F
        const val STROKE_WIDTH = 30F
        var BASE_COLOR = Color.rgb(100, 100, 100)
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        getStyles(attrs, defStyleAttr)

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
            maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL);
        }
    }


    private fun getStyles(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let {

        }
    }

    fun isLoading() = valueAnimator.isRunning ?: false

    fun startLoading() {
        valueAnimator.start()
    }

    fun clearLoading() {
        valueAnimator.cancel()
        valueAnimator.end()
    }

    private fun isAnimationRunning() = valueAnimator.isRunning

    override fun onAnimationUpdate(animator: ValueAnimator) {
        val animationProgress = animator.animatedValue as Float
        createSkeleton(animationProgress)

        //remove after
        BASE_COLOR = Color.rgb(235, 235, 235)
        skeletonEffectPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            color = AnimationUtils.lightenColor(BASE_COLOR, 0.4F)
            maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)
        }
        skeletonPaint = Paint().apply {
            style = Paint.Style.FILL
            color = BASE_COLOR
        }

        invalidate()
    }

    private fun createSkeleton(progress: Float) {
        skeletonPath.reset()
        skeletonPath.addRoundRect(
            0F, 0F, width.toFloat(), height.toFloat(),
            ROUND_PIXELS, ROUND_PIXELS, Path.Direction.CCW
        )

        val x = width * progress
        skeletonEffectPath.reset()
        skeletonEffectPath.moveTo(x, 0f)
        skeletonEffectPath.lineTo(x, height.toFloat())

    }

    override fun onDraw(canvas: Canvas?) {
        if (isAnimationRunning() && canvas != null) {
            canvas.drawPath(skeletonPath, skeletonPaint);
            canvas.drawPath(skeletonEffectPath, skeletonEffectPaint);
        } else {
            super.onDraw(canvas)
        }
    }

}