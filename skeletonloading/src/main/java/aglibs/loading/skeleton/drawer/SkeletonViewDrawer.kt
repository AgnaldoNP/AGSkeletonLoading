package aglibs.loading.skeleton.drawer

import aglibs.loading.skeleton.util.getAllLineBounds
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

@Suppress("unused")
class SkeletonViewDrawer(private val view: View) : SkeletonDrawer(view) {

    override fun createSkeleton() {
        skeletonRects.clear()

        if (view is TextView && view !is Button && view !is EditText) {
            val allLineBounds = view.getAllLineBounds(
                splitSkeletonTextByLines, clipToText
            )

            allLineBounds.forEach { lineBound ->
                skeletonPath.addRoundRect(
                    RectF(lineBound),
                    skeletonCornerRadius,
                    skeletonCornerRadius,
                    Path.Direction.CCW
                )
                skeletonRects.add(lineBound)
            }
        } else {
            val viewWidth = view.width
            val viewHeight = view.height

            val rect = Rect(0, 0, viewWidth, viewHeight)
            skeletonPath.addRoundRect(
                RectF(rect),
                skeletonCornerRadius,
                skeletonCornerRadius,
                Path.Direction.CCW
            )
            skeletonRects.add(rect)
        }
    }

}
