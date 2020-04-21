package aglibs.loading.skeleton.drawer

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.view.View

@Suppress("unused")
class SkeletonViewDrawer(private val view: View) : SkeletonDrawer(view) {

    private var skeletonPath: Path = Path()
    private var skeletonRect: Rect = Rect()
    private var skeletonEffectPath: Path = Path()

    override fun createSkeleton() {
        val viewWidth = view.width
        val viewHeight = view.height

        skeletonRect.set(0, 0, viewWidth, viewHeight)

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

    override fun getSkeletonRects(): List<Rect> {
        createSkeleton()
        return listOf(skeletonRect)
    }

    override fun draw(canvas: Canvas?): Boolean {
        canvas?.let {
            if (!view.isInEditMode) {
                if (isLoading()) {
                    canvas.drawPath(skeletonPath, skeletonPaint)
                    if (!disableAnimation) {
                        canvas.drawPath(skeletonEffectPath, skeletonEffectPaint)
                    }
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
