package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.util.AnimationUtils
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import android.view.ViewGroup

@Suppress("unused")
class SkeletonViewGroupDrawer(private val view: View) : SkeletonDrawer(view) {

    private var skeletonPath: Path = Path()
    private var skeletonPaint: Paint = Paint().also {
        it.style = Paint.Style.FILL
    }

    private var skeletonEffectPath: Path = Path()
    private var skeletonEffectPaint: Paint = Paint().also {
        it.style = Paint.Style.STROKE
    }

    private val visibilityViewsMap = hashMapOf<View, Int>()

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

    override fun startLoading() {
        getAllVisibilityNonViewGroupViews(view, visibilityViewsMap)
        visibilityViewsMap.forEach { it.key.visibility = View.GONE }
        super.startLoading()
    }

    override fun stopLoading() {
        super.stopLoading()
        visibilityViewsMap.forEach { it.key.visibility = it.value }
    }

    private fun getAllVisibilityNonViewGroupViews(v: View, viewsMap: HashMap<View, Int>) {
        v.takeIf { v is ViewGroup }?.let {
            for (i in 0 until (v as ViewGroup).childCount) {
                getAllVisibilityNonViewGroupViews(v.getChildAt(i), viewsMap)
            }
        } ?: run { viewsMap.put(v, v.visibility) }
    }

    override fun createSkeleton() {
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
