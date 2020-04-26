package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.R
import aglibs.loading.skeleton.util.AnimationUtils
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View


@Suppress("unused")
abstract class SkeletonDrawer(private val view: View) : ValueAnimator.AnimatorUpdateListener {

    private var valueAnimator: ValueAnimator
    private var animationDuration: Int = Duration.MEDIUM.millis()

    private var currentAnimationProgress: Float = 0F
    private var initWithLoading: Boolean = true
    private var enableDevelopPreview: Boolean = true
    private var skeletonColor: Int = Color.rgb(230, 230, 230)
    private var skeletonEffectStrokeWidth: Float = 30F
    private var skeletonEffectBlurWidth: Float = 50F
    private var skeletonEffectLightenFactor: Float = 0.2F
    protected var splitSkeletonTextByLines: Boolean = true
    protected var clipToText: Boolean = true

    var skeletonCornerRadius: Float = 10F
    var disableAnimation: Boolean = false

    private var skeletonPaint: Paint = Paint(ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.FILL
    }

    private var skeletonEffectPaint: Paint = Paint(ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.STROKE
        it.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    protected val skeletonPaths: ArrayList<Path> = arrayListOf()
    protected val skeletonRects: ArrayList<Rect> = arrayListOf()
    private val effectPath: Path = Path()

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

            splitSkeletonTextByLines = typedArray.getBoolean(
                R.styleable.SkeletonView_splitSkeletonTextByLines,
                splitSkeletonTextByLines
            )

            clipToText = typedArray.getBoolean(
                R.styleable.SkeletonView_clipToText,
                clipToText
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
        if (!isLoading()) {
            valueAnimator.start()
            view.invalidate()
        }
    }

    open fun stopLoading() {
        if (isLoading()) {
            valueAnimator.cancel()
            valueAnimator.end()
            view.invalidate()
        }
    }

    override fun onAnimationUpdate(animator: ValueAnimator) {
        currentAnimationProgress = animator.animatedValue as Float
        createSkeletonEffect()
        view.invalidate()
    }

    abstract fun createSkeleton()

    fun getSkeletonRects(): List<Rect> {
        createSkeleton()
        return skeletonRects
    }

    private fun createSkeletonEffect() {
        val viewWidth = view.width
        val viewHeight = view.height

        val effectX = viewWidth * currentAnimationProgress
        effectPath.reset()
        effectPath.moveTo(effectX, 0F)
        effectPath.lineTo(effectX, viewHeight.toFloat())
    }

    fun draw(canvas: Canvas?): Boolean {
        canvas?.let {
            if (!view.isInEditMode) {
                if (isLoading()) {
                    skeletonPaths.forEach { path ->
                        canvas.drawPath(path, skeletonPaint)
                        if (!disableAnimation) {
                            canvas.drawPath(effectPath, skeletonEffectPaint)
                        }
                    }
                    return true
                }
            } else {
                if (initWithLoading && enableDevelopPreview) {
                    createSkeleton()
                    skeletonPaths.forEach { path ->
                        canvas.drawPath(path, skeletonPaint)
                    }
                    return true
                }
            }
        }

        return false
    }


}
