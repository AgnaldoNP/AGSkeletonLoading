package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.util.AnimationUtils
import android.graphics.*
import android.view.View
import android.view.ViewGroup

@Suppress("unused")
class SkeletonViewGroupDrawer(private val view: View) : SkeletonDrawer(view) {

    private var hashMapSkeleton: HashMap<View, Path> = hashMapOf()
    private var skeletonPaint: Paint = Paint().also {
        it.style = Paint.Style.FILL
    }

    private var hashMapSkeletonEffect: HashMap<View, Pair<Path, RectF>> = hashMapOf()
    private var skeletonEffectPaint: Paint = Paint().also {
        it.style = Paint.Style.STROKE
    }

    private val visibilityViewsMap = hashMapOf<View, Int>()

    override fun applyStyles() {
        super.applyStyles()
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

    override fun startLoading() {
        getAllVisibilityNonViewGroupViews(view, visibilityViewsMap)
        visibilityViewsMap.forEach {
            hashMapSkeleton[it.key] = Path()
            hashMapSkeletonEffect[it.key] = Pair(Path(), RectF())
            it.key.visibility = View.GONE
        }

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

    private fun getPathLeft(v: View): Int {
        var left = 0
        var v2 = v

        do {
            if (view != v2) {
                left += v2.left
            }
            v2 = v2.parent as View
        } while (v2 != view.parent)
        return left
    }

    private fun getPathTop(v: View): Int {
        var top = 0
        var v2 = v

        do {
            if (view != v2) {
                top += v2.top
            }
            v2 = v2.parent as View
        } while (v2 != view.parent)
        return top
    }

    override fun createSkeleton() {
        visibilityViewsMap.forEach {
            val viewWidth = it.key.width.toFloat()
            val viewHeight = it.key.height.toFloat()
            val left = getPathLeft(it.key).toFloat()
            val top = getPathTop(it.key).toFloat()
            val right = viewWidth + left
            val bottom = viewHeight + top

            val rect = RectF(left, top, right, bottom)

            hashMapSkeleton[it.key]?.reset()
            hashMapSkeleton[it.key]?.addRoundRect(
                rect, skeletonCornerRadius, skeletonCornerRadius, Path.Direction.CCW
            )

            hashMapSkeletonEffect[it.key]?.let { pair ->
                hashMapSkeletonEffect[it.key] = Pair(pair.first, rect)
            }
        }
    }

    override fun createSkeletonEffect() {
        hashMapSkeletonEffect.forEach {
            val viewWidth = it.key.width
            val viewHeight = it.key.height

            val left = it.value.second.left
            val top = it.value.second.top

            val effectX = left + (viewWidth * currentAnimationProgress)
            it.value.first.reset()
            it.value.first.moveTo(effectX, top)
            it.value.first.lineTo(effectX, top + viewHeight.toFloat())
        }
    }

    override fun draw(canvas: Canvas?): Boolean {
        canvas?.let {
            if (!view.isInEditMode) {
                if (isLoading()) {
                    hashMapSkeleton.forEach {
                        canvas.drawPath(it.value, skeletonPaint)
                    }
                    hashMapSkeletonEffect.forEach {
                        canvas.drawPath(
                            it.value.first,
                            skeletonEffectPaint
                        )
                    }
                    return true
                }
            } else {
                if (initWithLoading && enableDevelopPreview) {
                    createSkeleton()
                    hashMapSkeleton.forEach { canvas.drawPath(it.value, skeletonPaint) }
                    return true
                }
            }
        }

        return false
    }

}
