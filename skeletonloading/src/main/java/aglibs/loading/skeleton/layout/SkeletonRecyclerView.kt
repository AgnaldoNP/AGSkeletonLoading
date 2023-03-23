package aglibs.loading.skeleton.layout

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import aglibs.loading.skeleton.drawer.SkeletonListViewDrawer
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
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

    // adapter magic

    private var actualAdapter: Adapter<*>? = null
    override fun setAdapter(adapter: Adapter<*>?) {
        if (isLayoutSuppressed) {
            actualAdapter = adapter
        } else {
            super.setAdapter(adapter)
        }
    }

    override fun swapAdapter(adapter: Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        if (isLayoutSuppressed) {
            actualAdapter = adapter
        } else {
            super.swapAdapter(adapter, removeAndRecycleExistingViews)
        }
    }

    override fun getAdapter(): Adapter<*>? {
        return if (isLayoutSuppressed) actualAdapter else super.getAdapter()
    }

    internal fun suppressAdapter() {
        if (!isLayoutSuppressed) {
            actualAdapter = super.getAdapter()
            super.setAdapter(EmptyAdapter)
            suppressLayout(true) // important to be *after* setAdapter() which unsuppresses layout
        }
    }
    internal fun unsuppressAdapter() {
        if (isLayoutSuppressed) {
            super.setAdapter(actualAdapter)
            actualAdapter = null
            suppressLayout(false)
        }
    }

}

private object EmptyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        throw UnsupportedOperationException()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        throw UnsupportedOperationException()
}
