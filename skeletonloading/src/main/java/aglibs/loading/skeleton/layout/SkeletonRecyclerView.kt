package aglibs.loading.skeleton.layout

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import aglibs.loading.skeleton.drawer.SkeletonListViewDrawer
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

class SkeletonRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr),
    ISkeletonDrawer {

    private var skeletonDrawer = SkeletonListViewDrawer(this).apply {
        getStyles(attrs, defStyleAttr)
    }

    init {
        setWillNotDraw(false)
        invalidate()
    }

    override fun getSkeletonDrawer() = skeletonDrawer

    override fun isLoading() = skeletonDrawer.isLoading()

    override fun startLoading() {
        skeletonDrawer.startLoading()
    }

    override fun stopLoading() {
        skeletonDrawer.stopLoading()
        notifyVisibleItems()
    }

    private fun notifyVisibleItems() {
        adapter?.let {
            var start = 0
            var end = 0

            layoutManager?.let { lm ->
                when (lm) {
                    is LinearLayoutManager -> {
                        start = lm.findFirstVisibleItemPosition()
                        end = lm.findLastVisibleItemPosition()
                    }
                    is GridLayoutManager -> {
                        start = lm.findFirstVisibleItemPosition()
                        end = lm.findLastVisibleItemPosition()
                    }
                    else -> {
                        start = 0
                        end = ceil((height / skeletonDrawer.viewHolderHeight.toFloat())).toInt()
                    }
                }
            }

            for (i in start..end) {
                it.notifyItemChanged(i)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (!skeletonDrawer.draw(canvas)) {
            super.onDraw(canvas)
        }
    }

    override fun onDetachedFromWindow() {
        stopLoading()
        super.onDetachedFromWindow()
    }

}
