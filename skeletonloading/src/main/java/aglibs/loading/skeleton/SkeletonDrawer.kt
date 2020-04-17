package aglibs.loading.skeleton

import android.animation.ValueAnimator
import android.graphics.*
import android.util.AttributeSet
import android.view.View

@Suppress("unused")
class SkeletonDrawer(private val view: View) : ValueAnimator.AnimatorUpdateListener {

    companion object {
        private const val ROUND_PIXELS = 10F
        private const val STROKE_WIDTH = 30F
        private const val BLUR_WIDTH = 50F
        private const val LIGHTNEN_FACTOR = 0.2F
        private val BASE_COLOR = Color.rgb(230, 230, 230)
    }

    private var valueAnimator: ValueAnimator

    private var skeletonPath: Path
    private var skeletonPaint: Paint
    private var skeletonEffectPath: Path
    private var skeletonEffectPaint: Paint

    private var currentAnimationProgress: Float = 0F

    private var initWithLoading: Boolean = true
    private var enableDevelopPreview: Boolean = true
    private var animationDuration: Int = Duration.MEDIUM.millis()
    private var skeletonColor: Int = BASE_COLOR
    private var skeletonEffectStrokeWidth: Float = STROKE_WIDTH
    private var skeletonEffectBlurWidth: Float = BLUR_WIDTH
    private var skeletonEffectLightenFactor: Float = LIGHTNEN_FACTOR
    private var skeletonCornerRadius: Float = ROUND_PIXELS

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

        valueAnimator = AnimationUtils.getValueAnimator(
            updateListener = this
        )

        skeletonPath = Path()
        skeletonEffectPath = Path()

        skeletonPaint = Paint().also {
            it.style = Paint.Style.FILL
        }

        skeletonEffectPaint = Paint().also {
            it.style = Paint.Style.STROKE
        }

        view.post {
            if (initWithLoading) {
                startLoading()
            }
        }
    }

    private fun applyStyles() {
        skeletonPaint.color = skeletonColor

        skeletonEffectPaint.apply {
            strokeWidth = skeletonEffectStrokeWidth
            color = AnimationUtils.lightenColor(skeletonColor, skeletonEffectLightenFactor)
            maskFilter = BlurMaskFilter(skeletonEffectBlurWidth, BlurMaskFilter.Blur.NORMAL)
        }

        valueAnimator.duration = animationDuration.toLong()
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
        currentAnimationProgress = animator.animatedValue as Float
        createSkeleton()
        view.invalidate()
    }

    private fun createSkeleton() {
        val viewWidth = view.width
        val viewHeight = view.height

        skeletonPath.reset()
        skeletonPath.addRoundRect(
            0F, 0F, viewWidth.toFloat(), viewHeight.toFloat(),
            skeletonCornerRadius, skeletonCornerRadius, Path.Direction.CCW
        )

        val x = viewWidth * currentAnimationProgress
        skeletonEffectPath.reset()
        skeletonEffectPath.moveTo(x, 0f)
        skeletonEffectPath.lineTo(x, viewHeight.toFloat())
    }

    fun draw(canvas: Canvas?): Boolean {
        canvas?.let {
            if (!view.isInEditMode) {
                if (isLoading()) {
                    canvas.drawPath(skeletonPath, skeletonPaint)
                    canvas.drawPath(skeletonEffectPath, skeletonEffectPaint)
                    return true
                }
            } else {
                if (initWithLoading && enableDevelopPreview) {
                    createSkeleton()
                    canvas.drawPath(skeletonPath, skeletonPaint)
                    return true
                }
            }
        }

        return false
    }

}
