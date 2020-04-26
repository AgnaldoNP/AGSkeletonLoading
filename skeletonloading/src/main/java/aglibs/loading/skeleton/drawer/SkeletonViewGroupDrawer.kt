package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.util.getAllLineBounds
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.graphics.toRect

@Suppress("unused")
class SkeletonViewGroupDrawer(private val viewGroup: View) : SkeletonDrawer(viewGroup) {

    private val visibilityViewsMap = hashMapOf<View, Int>()

    override fun startLoading() {
        getAllVisibilityNonViewGroupViews(viewGroup, visibilityViewsMap)
        visibilityViewsMap.forEach {
            it.key.visibility = View.INVISIBLE
        }

        super.startLoading()
    }

    override fun stopLoading() {
        super.stopLoading()
        visibilityViewsMap.forEach {
            it.key.visibility = it.value
            (it.key as? ISkeletonDrawer)?.stopLoading()
        }
    }

    private fun getAllVisibilityNonViewGroupViews(subview: View, viewsMap: HashMap<View, Int>) {
        subview.takeIf { subview is ViewGroup }?.let {
            for (i in 0 until (subview as ViewGroup).childCount) {
                getAllVisibilityNonViewGroupViews(subview.getChildAt(i), viewsMap)
            }
        } ?: run {
            (subview as? ISkeletonDrawer)?.getSkeletonDrawer()?.disableAnimation = true
            viewsMap.put(subview, subview.visibility)
        }
    }

    private fun getPathLeft(subView: View): Int {
        var left = 0
        var view = subView

        do {
            if (viewGroup != view) {
                left += view.left
            }
            view = view.parent as View
        } while (view != viewGroup.parent)
        return left
    }

    private fun getPathTop(subView: View): Int {
        var top = 0
        var view = subView

        do {
            if (viewGroup != view) {
                top += view.top
            }
            view = view.parent as View
        } while (view != viewGroup.parent)
        return top
    }

    override fun createSkeleton() {
        skeletonPaths.clear()
        skeletonRects.clear()

        visibilityViewsMap.forEach { map ->
            val subView = map.key
            val left = getPathLeft(subView).toFloat()
            val top = getPathTop(subView).toFloat()

            if (subView is TextView && subView !is Button && subView !is EditText) {
                val allLineBounds = subView.getAllLineBounds(
                    splitSkeletonTextByLines, clipToText
                )

                allLineBounds.forEach { lineBound ->
                    lineBound.offset(left.toInt(), top.toInt())
                    skeletonPaths.add(Path().also { path ->
                        path.addRoundRect(
                            RectF(lineBound),
                            skeletonCornerRadius,
                            skeletonCornerRadius,
                            Path.Direction.CCW
                        )
                    })
                    skeletonRects.add(lineBound)
                }
            } else if (subView is ISkeletonDrawer) {
                val skeletonDrawer = subView.getSkeletonDrawer()
                val skeletonDrawerRects = skeletonDrawer.getSkeletonRects()
                val radius = skeletonDrawer.skeletonCornerRadius

                skeletonDrawerRects.forEach { skeletonRect ->
                    skeletonRect.offset(left.toInt(), top.toInt())
                    skeletonPaths.add(Path().also { path ->
                        path.addRoundRect(
                            RectF(skeletonRect),
                            radius, radius,
                            Path.Direction.CCW
                        )
                    })
                    skeletonRects.add(skeletonRect)
                }

            } else {
                val viewWidth = subView.width.toFloat()
                val viewHeight = subView.height.toFloat()
                val right = viewWidth + left
                val bottom = viewHeight + top
                val rect = RectF(left, top, right, bottom)

                skeletonPaths.add(Path().also { path ->
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
