package aglibs.loading.skeleton.drawer

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup
import kotlin.math.ceil
import kotlin.math.truncate

class SkeletonListViewDrawer(private val viewGroup: View) : SkeletonViewGroupDrawer(viewGroup) {

    private var viewHolderHeight = 0

    override fun startLoading() {
        val parent = viewGroup.parent
        if (skeletonViewHolderItem != -1 && parent is ViewGroup && skeletonRects.isEmpty()
            && viewGroup.width > 0 && viewGroup.height > 0) {
            val layoutInflater = viewGroup.context
                .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater.inflate(skeletonViewHolderItem, null)!!

            view.measure(
                makeMeasureSpec(viewGroup.width - viewGroup.paddingLeft - viewGroup.paddingRight, MeasureSpec.AT_MOST),
                makeMeasureSpec(viewGroup.height - viewGroup.paddingTop - viewGroup.paddingBottom, MeasureSpec.AT_MOST),
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)

            visibilityViewsMap.clear()
            getAllVisibilityNonViewGroupViews(view, visibilityViewsMap)
            viewHolderHeight = view.height
            createSkeleton()
        }
        super.startLoading()
    }

    override fun createSkeleton() {
        if (skeletonRects.isEmpty()) {
            if (viewHolderHeight == 0)
                return

            val amount = if (skeletonViewHolderAmount == -1) {
                if (skeletonViewHolderTruncate) {
                    truncate((viewGroup.height / viewHolderHeight.toFloat())).toInt()
                } else {
                    ceil((viewGroup.height / viewHolderHeight.toFloat())).toInt()
                }
            } else {
                skeletonViewHolderAmount
            }

            if (amount == 0)
                return

            super.createSkeleton()
            val rects = arrayListOf<Rect>()
            skeletonRects.forEach { rect ->
                for (i in 2..amount) {
                    Rect(rect).also {
                        it.offset(0, (viewHolderHeight * (i - 1)))
                        rects.add(it)
                        skeletonPath.addRoundRect(
                            RectF(it),
                            skeletonCornerRadius,
                            skeletonCornerRadius,
                            Path.Direction.CCW
                        )
                    }
                }
            }
            skeletonRects.addAll(rects)
        }
    }

}
