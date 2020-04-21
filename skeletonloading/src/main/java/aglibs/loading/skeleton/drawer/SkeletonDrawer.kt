package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.util.AnimationUtils
import aglibs.loading.skeleton.R
import android.animation.ValueAnimator
import android.graphics.*
import android.util.AttributeSet
import android.view.View

@Suppress("unused")
abstract class SkeletonDrawer(private val view: View) : ValueAnimator.AnimatorUpdateListener {

    companion object {
        private const val ROUND_PIXELS = 10F
        private const val STROKE_WIDTH = 30F
        private const val BLUR_WIDTH = 50F
        private const val LIGHTNEN_FACTOR = 0.2F
        private val BASE_COLOR = Color.rgb(230, 230, 230)
    }

    private var valueAnimator: ValueAnimator
    private var animationDuration: Int = Duration.MEDIUM.millis()

    protected var currentAnimationProgress: Float = 0F
    protected var initWithLoading: Boolean = true
    protected var enableDevelopPreview: Boolean = true
    protected var skeletonColor: Int = BASE_COLOR
    protected var skeletonEffectStrokeWidth: Float = STROKE_WIDTH
    protected var skeletonEffectBlurWidth: Float = BLUR_WIDTH
    protected var skeletonEffectLightenFactor: Float = LIGHTNEN_FACTOR
    var skeletonCornerRadius: Float = ROUND_PIXELS

    var disableAnimation: Boolean = false

    var skeletonPaint: Paint = Paint().also {
        it.style = Paint.Style.FILL
    }

    var skeletonEffectPaint: Paint = Paint().also {
        it.style = Paint.Style.STROKE
    }

    enum class Duration(val duration: Int) {
        SHORT(0) {
            override fun millis() = 800
        },
        MEDIUM(1) {
            override fun millis() = 1300
        },
        LONG(2) {
            override fun millis() = 2000
        };

        abstract fun millis(): Int

        companion object {
            fun get(index: Int): Duration {
                return values()[index]
            }
        }
    }

    internal fun getStyles(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let {

            val typedArray = view.context.obtainStyledAttributes(
                attrs, R.styleable.SkeletonView, defStyle,
                R.style.DefaultSkeletonView
            )

            initWithLoading = typedArray.getBoolean(
                R.styleable.SkeletonView_initWithLoading,
                initWithLoading
            )

            enableDevelopPreview = typedArray.getBoolean(
                R.styleable.SkeletonView_initWithLoading,
                enableDevelopPreview
            )

            animationDuration = Duration.get(
                typedArray.getInteger(
                    R.styleable.SkeletonView_cycleDuration,
                    Duration.MEDIUM.ordinal
                )
            ).millis()

            typedArray.getInteger(
                R.styleable.SkeletonView_customCycleDuration, -1
            ).takeIf { it > 0 }?.run { animationDuration = this }

            skeletonColor = typedArray.getColor(
                R.styleable.SkeletonView_skeletonColor,
                skeletonColor
            )

            skeletonEffectStrokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.SkeletonView_skeletonEffectStrokeWidth,
                skeletonEffectStrokeWidth.toInt()
            ).toFloat()

            skeletonEffectBlurWidth = typedArray.getDimensionPixelSize(
                R.styleable.SkeletonView_skeletonEffectBlurWidth,
                skeletonEffectBlurWidth.toInt()
            ).toFloat()

            skeletonEffectLightenFactor = typedArray.getFloat(
                R.styleable.SkeletonView_skeletonEffectLightenFactor, skeletonEffectLightenFactor
            )

            skeletonCornerRadius = typedArray.getDimensionPixelSize(
                R.styleable.SkeletonView_skeletonCornerRadius,
                skeletonCornerRadius.toInt()
            ).toFloat()

            typedArray.recycle()
        }

        applyStyles()
    }

    init {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        valueAnimator =
            AnimationUtils.getValueAnimator(
                updateListener = this
            )

        view.post {
            if (initWithLoading) {
                startLoading()
            }
        }
    }

    protected open fun applyStyles() {
        valueAnimator.duration = animationDuration.toLong()

        skeletonPaint.color = skeletonColor

        skeletonEffectPaint.apply {
            strokeWidth = skeletonEffectStrokeWidth
            color = AnimationUtils.lightenColor(
                skeletonColor,
                skeletonEffectLightenFactor
            )
            maskFilter = BlurMaskFilter(skeletonEffectBlurWidth, BlurMaskFilter.Blur.NORMAL)
        }
    }

    fun isLoading() = valueAnimator.isRunning

    open fun startLoading() {
        createSkeleton()
        valueAnimator.start()
        view.invalidate()
    }

    open fun stopLoading() {
        valueAnimator.cancel()
        valueAnimator.end()
        view.invalidate()
    }

    override fun onAnimationUpdate(animator: ValueAnimator) {
        currentAnimationProgress = animator.animatedValue as Float
        createSkeletonEffect()
        view.invalidate()
    }

    abstract fun getSkeletonRects(): List<Rect>
    abstract fun createSkeleton()
    abstract fun createSkeletonEffect()
    abstract fun draw(canvas: Canvas?): Boolean

}
