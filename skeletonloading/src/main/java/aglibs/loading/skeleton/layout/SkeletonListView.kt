package aglibs.loading.skeleton.layout

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import aglibs.loading.skeleton.drawer.SkeletonListViewDrawer
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ListView

class SkeletonListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ListView(context, attrs, defStyleAttr),
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
    }

    override fun onDraw(canvas: Canvas) {
        if (!skeletonDrawer.draw(canvas)) {
            super.onDraw(canvas)
        }
    }

    override fun onDetachedFromWindow() {
        stopLoading()
        super.onDetachedFromWindow()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) skeletonDrawer.onLayoutChanged()
    }

}
