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
    private var autoStart: Boolean = true
    private var enableDevelopPreview: Boolean = true
    private var skeletonColor: Int = Color.rgb(230, 230, 230)
    private var shimmerStrokeWidth: Float = 30F
    private var shimmerBlurWidth: Float = 50F
    private var shimmerLightenFactor: Float = 0.2F
    protected var splitSkeletonTextByLines: Boolean = true
    protected var clipToText: Boolean = true

    protected var skeletonViewHolderItem: Int = -1
    protected var skeletonViewHolderAmount: Int = -1
    protected var skeletonViewHolderTruncate: Boolean = false

    var skeletonCornerRadius: Float = 10F
    var disableAnimation: Boolean = false

    private var skeletonPaint: Paint = Paint(ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.FILL
    }

    private var skeletonEffectPaint: Paint = Paint(ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.STROKE
        it.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    protected val skeletonRects: ArrayList<Rect> = arrayListOf()
    protected val skeletonPath: Path = Path()
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

            autoStart = typedArray.getBoolean(
                R.styleable.SkeletonView_autoStart,
                autoStart
            )

            enableDevelopPreview = typedArray.getBoolean(
                R.styleable.SkeletonView_autoStart,
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
                    R.styleable.SkeletonView_skeletonAnimationDuration,
                    Duration.MEDIUM.ordinal
                )
            ).millis()

            typedArray.getInteger(
                R.styleable.SkeletonView_customDuration, -1
            ).takeIf { it > 0 }?.run { animationDuration = this }

            skeletonColor = typedArray.getColor(
                R.styleable.SkeletonView_skeletonColor,
                skeletonColor
            )

            shimmerStrokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.SkeletonView_shimmerStrokeWidth,
                shimmerStrokeWidth.toInt()
            ).toFloat()

            shimmerBlurWidth = typedArray.getDimensionPixelSize(
                R.styleable.SkeletonView_shimmerBlurWidth,
                shimmerBlurWidth.toInt()
            ).toFloat()

            shimmerLightenFactor = typedArray.getFloat(
                R.styleable.SkeletonView_shimmerLightenFactor, shimmerLightenFactor
            )

            skeletonCornerRadius = typedArray.getDimensionPixelSize(
                R.styleable.SkeletonView_skeletonCornerRadius,
                skeletonCornerRadius.toInt()
            ).toFloat()

            skeletonViewHolderItem = typedArray.getResourceId(
                R.styleable.SkeletonView_skeletonViewHolderItem,
                skeletonViewHolderItem
            )
            skeletonViewHolderAmount = typedArray.getInteger(
                R.styleable.SkeletonView_skeletonViewHolderAmount,
                skeletonViewHolderAmount
            )
            skeletonViewHolderTruncate = typedArray.getBoolean(
                R.styleable.SkeletonView_skeletonViewHolderTruncate,
                skeletonViewHolderTruncate
            )

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
            if (autoStart) {
                startLoading()
            }
        }
    }

    protected open fun applyStyles() {
        valueAnimator.duration = animationDuration.toLong()

        skeletonPaint.color = skeletonColor

        skeletonEffectPaint.apply {
            strokeWidth = shimmerStrokeWidth
            color = AnimationUtils.lightenColor(
                skeletonColor,
                shimmerLightenFactor
            )
            maskFilter = BlurMaskFilter(shimmerBlurWidth, BlurMaskFilter.Blur.NORMAL)
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

    protected open fun createSkeletonEffect() {
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
                    canvas.drawPath(skeletonPath, skeletonPaint)
                    if (!disableAnimation) {
                        canvas.drawPath(effectPath, skeletonEffectPaint)
                    }
                    return true
                }
            } else {
                if (autoStart && enableDevelopPreview) {
                    createSkeleton()
                    canvas.drawPath(skeletonPath, skeletonPaint)
                    return true
                }
            }
        }

        return false
    }


}
