package aglibs.loading.skeleton.view

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import aglibs.loading.skeleton.drawer.SkeletonViewDrawer
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class SkeletonTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr),
    ISkeletonDrawer {

    private var skeletonDrawer = SkeletonViewDrawer(this).apply {
        getStyles(attrs, defStyleAttr)
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
