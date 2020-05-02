package aglibs.loading.skeleton.layout

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import aglibs.loading.skeleton.drawer.SkeletonListViewDrawer
import aglibs.loading.skeleton.drawer.SkeletonViewGroupDrawer
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.ListView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

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
