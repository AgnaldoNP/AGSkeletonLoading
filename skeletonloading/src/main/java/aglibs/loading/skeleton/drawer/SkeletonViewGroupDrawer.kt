package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.getAllLineBounds
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toRect

@Suppress("unused")
class SkeletonViewGroupDrawer(private val view: View) : SkeletonDrawer(view) {

    private val hashMapSkeleton: HashMap<View, ArrayList<Path>> = hashMapOf()
    private val skeletonRects: ArrayList<Rect> = arrayListOf()
    private val effectPath: Path = Path()

    private val visibilityViewsMap = hashMapOf<View, Int>()

    override fun startLoading() {
        getAllVisibilityNonViewGroupViews(view, visibilityViewsMap)
        visibilityViewsMap.forEach {
            hashMapSkeleton[it.key] = arrayListOf()
            it.key.visibility = View.INVISIBLE
        }

        super.startLoading()
    }

    override fun stopLoading() {
        super.stopLoading()
        visibilityViewsMap.forEach {
            it.key.visibility = it.value
            if ((it.key is ISkeletonDrawer)) {
                (it.key as ISkeletonDrawer).stopLoading()
            }
        }
    }

    override fun getSkeletonRects(): List<Rect> = skeletonRects

    private fun getAllVisibilityNonViewGroupViews(v: View, viewsMap: HashMap<View, Int>) {
        v.takeIf { v is ViewGroup }?.let {
            for (i in 0 until (v as ViewGroup).childCount) {
                getAllVisibilityNonViewGroupViews(v.getChildAt(i), viewsMap)
            }
        } ?: run {
            if ((v is ISkeletonDrawer)) {
                (v as ISkeletonDrawer).getSkeletonDrawer().disableAnimation = true
            }
            viewsMap.put(v, v.visibility)
        }
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
        visibilityViewsMap.forEach { map ->
            val visibleView = map.key
            val left = getPathLeft(visibleView).toFloat()
            val top = getPathTop(visibleView).toFloat()

            hashMapSkeleton[visibleView]?.clear()
            skeletonRects.clear()

            when (visibleView) {
                is TextView -> {
                    val allLineBounds = visibleView.getAllLineBounds()
                    allLineBounds.forEach { lineBound ->
                        lineBound.offset(left.toInt(), top.toInt())
                        hashMapSkeleton[visibleView]?.add(Path().also { path ->
                            path.addRoundRect(
                                RectF(lineBound),
                                skeletonCornerRadius,
                                skeletonCornerRadius,
                                Path.Direction.CCW
                            )
                        })
                        skeletonRects.add(lineBound)
                    }
                }
                is ISkeletonDrawer -> {
                    val skeletonDrawer = visibleView.getSkeletonDrawer()
                    val skeletonDrawerRects = skeletonDrawer.getSkeletonRects()
                    val radius = skeletonDrawer.skeletonCornerRadius

                    skeletonDrawerRects.forEach { skeletonRect ->
                        skeletonRect.offset(left.toInt(), top.toInt())
                        hashMapSkeleton[visibleView]?.add(Path().also { path ->
                            path.addRoundRect(
                                RectF(skeletonRect),
                                radius, radius,
                                Path.Direction.CCW
                            )
                        })
                        skeletonRects.add(skeletonRect)
                    }

                }
                else -> {
                    val viewWidth = visibleView.width.toFloat()
                    val viewHeight = visibleView.height.toFloat()
                    val right = viewWidth + left
                    val bottom = viewHeight + top
                    val rect = RectF(left, top, right, bottom)

                    hashMapSkeleton[visibleView]?.add(Path().also { path ->
                        path.addRoundRect(
                            rect,
                            skeletonCornerRadius,
                            skeletonCornerRadius,
                            Path.Direction.CCW
                        )
                        skeletonRects.add(rect.toRect())
                    })
                }
            }
        }
    }

    override fun createSkeletonEffect() {
        val viewWidth = view.width
        val viewHeight = view.height

        val effectX = viewWidth * currentAnimationProgress
        effectPath.reset()
        effectPath.moveTo(effectX, 0F)
        effectPath.lineTo(effectX, viewHeight.toFloat())
    }

    override fun draw(canvas: Canvas?): Boolean {
        canvas?.let {
            if (!view.isInEditMode) {
                if (isLoading()) {
                    hashMapSkeleton.forEach { paths ->
                        paths.value.forEach { path ->
                            canvas.drawPath(path, skeletonPaint)
                        }
                    }

                    if (!disableAnimation) {
                        canvas.drawPath(effectPath, skeletonEffectPaint)
                    }
                    return true
                }
            } else {
                if (initWithLoading && enableDevelopPreview) {
                    createSkeleton()
                    hashMapSkeleton.forEach { paths ->
                        paths.value.forEach { path ->
                            canvas.drawPath(path, skeletonPaint)
                        }
                    }
                    return true
                }
            }
        }

        return false
    }

}
