package aglibs.loading.skeleton.layout

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import aglibs.loading.skeleton.drawer.SkeletonViewGroupDrawer
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat

class SkeletonLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr),
    ISkeletonDrawer {

    private var skeletonDrawer = SkeletonViewGroupDrawer(this).apply {
        getStyles(attrs, defStyleAttr)
    }

    init {
        setWillNotDraw(false)
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
