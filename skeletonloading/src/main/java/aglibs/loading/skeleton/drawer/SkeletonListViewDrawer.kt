package aglibs.loading.skeleton.drawer

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlin.math.truncate

class SkeletonListViewDrawer(private val viewGroup: View) : SkeletonViewGroupDrawer(viewGroup) {

    private var height = 0
    private val windowManager: WindowManager = viewGroup.context
        .getSystemService(WINDOW_SERVICE) as WindowManager

    override fun startLoading() {
        val parent = viewGroup.parent
        if (skeletonViewHolderItem != -1 && parent is ViewGroup && skeletonRects.isEmpty()) {
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )

            val layoutInflater = viewGroup.context
                .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?

            viewGroup.post {
                layoutInflater?.inflate(skeletonViewHolderItem, null)?.also { view ->
                    view.let {
                        setVisibilityForChildViews(it, View.INVISIBLE)
                        windowManager.addView(view, params)

                        it.post {
                            visibilityViewsMap.clear()
                            getAllVisibilityNonViewGroupViews(view, visibilityViewsMap)
                            height = view.height
                            createSkeleton()
                            setVisibilityForChildViews(it, View.GONE)
                            windowManager.removeView(view)
                        }
                    }
                }
            }
        }
        super.startLoading()
    }

    private fun setVisibilityForChildViews(view: View, visibility: Int) {
        (view as? ViewGroup)?.let {
            val childCount = it.childCount
            for (i in 0 until childCount) {
                if (it.visibility != View.GONE) {
                    it.getChildAt(i).visibility = visibility
                }
            }
        } ?: run { view.visibility = visibility }
    }

    override fun createSkeleton() {
        if (skeletonRects.isEmpty()) {
            super.createSkeleton()
            val rects = arrayListOf<Rect>()
            skeletonRects.forEach { rect ->
                val amount = if (skeletonViewHolderAmount == -1) {
                    truncate((viewGroup.height / height.toFloat())).toInt()
                } else {
                    skeletonViewHolderAmount
                }

                for (i in 1..amount) {
                    Rect(rect).also {
                        it.offset(0, height * i)
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
