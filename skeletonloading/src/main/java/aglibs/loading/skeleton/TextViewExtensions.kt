package aglibs.loading.skeleton

import android.graphics.Rect
import android.text.Layout
import android.widget.TextView

fun TextView.getAllLineBounds(): ArrayList<Rect> {
    val lineCount = this.lineCount
    val lineSpacingExtra = this.lineSpacingExtra
    val lineSpacingMultiplier = this.lineSpacingMultiplier
    val lineSpace = lineSpacingExtra * lineSpacingMultiplier
    val allLineBounds = ArrayList<Rect>()
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
    return allLineBounds
}