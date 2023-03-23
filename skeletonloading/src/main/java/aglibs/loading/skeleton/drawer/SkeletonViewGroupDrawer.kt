package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.layout.SkeletonRecyclerView
import aglibs.loading.skeleton.util.getAllLineBounds
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.toRect

@Suppress("unused")
open class SkeletonViewGroupDrawer(private val viewGroup: View) : SkeletonDrawer(viewGroup) {

    protected val visibilityViewsMap = hashMapOf<View, Int>()

    override fun startLoading() {
        visibilityViewsMap.clear()
        getAllVisibilityNonViewGroupViews(viewGroup, visibilityViewsMap)
        visibilityViewsMap.forEach {
            it.key.visibility = View.INVISIBLE
        }

        (viewGroup as? ListView)?.isEnabled = false
        (viewGroup as? SkeletonRecyclerView)?.suppressAdapter()
        super.startLoading()
    }

    override fun stopLoading() {
        visibilityViewsMap.forEach {
            it.key.visibility = it.value
            (it.key as? ISkeletonDrawer)?.stopLoading()
        }

        super.stopLoading()
        (viewGroup as? AbsListView)?.apply {
            invalidateViews()
            isEnabled = true
        }
        (viewGroup as? SkeletonRecyclerView)?.unsuppressAdapter()
    }

    protected fun getAllVisibilityNonViewGroupViews(subview: View, viewsMap: HashMap<View, Int>) {
        subview.takeIf { subview is ViewGroup }?.let {
            for (i in 0 until (subview as ViewGroup).childCount) {
                getAllVisibilityNonViewGroupViews(subview.getChildAt(i), viewsMap)
            }
        } ?: run {
            (subview as? ISkeletonDrawer)?.getSkeletonDrawer()?.disableAnimation = true
            viewsMap.put(subview, subview.visibility)
        }
    }

    private fun getPathLeft(subView: View?): Int {
        var left = 0
        var view = subView

        do {
            if (viewGroup != view) {
                left += view?.left ?: 0
            }
            view = view?.parent as? View
        } while (view != viewGroup.parent && view != null)
        return left
    }

    private fun getPathTop(subView: View?): Int {
        var top = 0
        var view = subView

        do {
            if (viewGroup != view) {
                top += view?.top ?: 0
            }
            view = view?.parent as? View
        } while (view != viewGroup.parent && view != null)
        return top
    }

    override fun createSkeleton() {
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
                    skeletonPath.addRoundRect(
                        RectF(lineBound),
                        skeletonCornerRadius,
                        skeletonCornerRadius,
                        Path.Direction.CCW
                    )
                    skeletonRects.add(lineBound)
                }
            } else if (subView is ISkeletonDrawer) {
                val skeletonDrawer = subView.getSkeletonDrawer()
                val skeletonDrawerRects = skeletonDrawer.getSkeletonRects()
                val radius = skeletonDrawer.skeletonCornerRadius

                skeletonDrawerRects.forEach { skeletonRect ->
                    skeletonRect.offset(left.toInt(), top.toInt())
                    skeletonPath.addRoundRect(
                        RectF(skeletonRect),
                        radius, radius,
                        Path.Direction.CCW
                    )
                    skeletonRects.add(skeletonRect)
                }

            } else {
                val viewWidth = subView.width.toFloat()
                val viewHeight = subView.height.toFloat()
                val right = viewWidth + left
                val bottom = viewHeight + top
                val rect = RectF(left, top, right, bottom)

                skeletonPath.addRoundRect(
                    rect,
                    skeletonCornerRadius,
                    skeletonCornerRadius,
                    Path.Direction.CCW
                )
                skeletonRects.add(rect.toRect())
            }
        }
    }

}
