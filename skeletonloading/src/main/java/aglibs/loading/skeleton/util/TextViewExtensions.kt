package aglibs.loading.skeleton.util

import android.graphics.Rect
import android.text.Layout
import android.widget.TextView

fun TextView.getAllLineBounds(splitSkeletonTextByLines: Boolean = false): ArrayList<Rect> {
    val allLineBounds = ArrayList<Rect>()
    if (splitSkeletonTextByLines) {
        val lineCount = this.lineCount
        val lineSpacingExtra = this.lineSpacingExtra
        val lineSpacingMultiplier = this.lineSpacingMultiplier
        val lineSpace = lineSpacingExtra * lineSpacingMultiplier
        for (i in 0 until lineCount) {
            val lineBounds = Rect()
            this.getLineBounds(i, lineBounds)
            if (i != lineCount - 1) {
                lineBounds.bottom -= lineSpace.toInt()
            }
            if (this.layout.alignment == Layout.Alignment.ALIGN_CENTER) {
                val boundWidth = lineBounds.width()
                val lineWidth = this.layout.getLineWidth(i)
                val whiteSpace = boundWidth - lineWidth
                lineBounds.left += (whiteSpace / 2).toInt()
                lineBounds.right -= (whiteSpace / 2).toInt()
            }
            allLineBounds.add(lineBounds)
        }
    } else {
        val rect = Rect(0, 0, this.width, this.height)
        allLineBounds.add(rect)
    }
    return allLineBounds
}