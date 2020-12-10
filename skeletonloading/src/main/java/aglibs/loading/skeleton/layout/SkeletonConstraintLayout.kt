package aglibs.loading.skeleton.layout

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import aglibs.loading.skeleton.drawer.SkeletonViewGroupDrawer
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class SkeletonConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    ISkeletonDrawer {

    private var skeletonDrawer = SkeletonViewGroupDrawer(this).apply {
        getStyles(attrs, defStyleAttr)
    }

    init {
        setWillNotDraw(false)
        invalidate()
    }

    override fun getSkeletonDrawer() = skeletonDrawer

    override fun isLoading() = skeletonDrawer.isLoading()

    override fun startLoading() {
        post {
            skeletonDrawer.startLoading()
        }
    }

    override fun stopLoading() {
        post {
            skeletonDrawer.stopLoading()
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
