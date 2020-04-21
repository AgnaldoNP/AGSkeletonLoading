package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.util.AnimationUtils
import aglibs.loading.skeleton.R
import android.animation.ValueAnimator
import android.graphics.*
import android.util.AttributeSet
import android.view.View

@Suppress("unused")
class SkeletonViewDrawer(private val view: View) : SkeletonDrawer(view){

    private var skeletonPath: Path = Path()
    private var skeletonPaint: Paint = Paint().also {
        it.style = Paint.Style.FILL
    }

    private var skeletonEffectPath: Path = Path()
    private var skeletonEffectPaint: Paint = Paint().also {
        it.style = Paint.Style.STROKE
    }

    override fun applyStyles() {
        super.applyStyles()
        skeletonPaint.color = skeletonColor

        skeletonEffectPaint.apply {
            strokeWidth = skeletonEffectStrokeWidth
            color =
                AnimationUtils.lightenColor(
                    skeletonColor,
                    skeletonEffectLightenFactor
                )
            maskFilter = BlurMaskFilter(skeletonEffectBlurWidth, BlurMaskFilter.Blur.NORMAL)
        }
    }

    override fun createSkeleton() {
        val viewWidth = view.width
        val viewHeight = view.height

        skeletonPath.reset()
        skeletonPath.addRoundRect(
            0F, 0F, viewWidth.toFloat(), viewHeight.toFloat(),
            skeletonCornerRadius, skeletonCornerRadius, Path.Direction.CCW
        )
    }

    override fun createSkeletonEffect() {
        val viewWidth = view.width
        val viewHeight = view.height

        val x = viewWidth * currentAnimationProgress
        skeletonEffectPath.reset()
        skeletonEffectPath.moveTo(x, 0f)
        skeletonEffectPath.lineTo(x, viewHeight.toFloat())
    }

    override fun draw(canvas: Canvas?): Boolean {
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
