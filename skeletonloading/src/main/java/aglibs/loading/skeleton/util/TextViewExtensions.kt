package aglibs.loading.skeleton.util

import android.graphics.Rect
import android.text.Layout
import android.view.View
import android.widget.TextView

fun TextView.getAllLineBounds(
    splitSkeletonTextByLines: Boolean = true,
    reduceToTextSpace: Boolean = true
): ArrayList<Rect> {
    val allLineBounds = ArrayList<Rect>()
    val lineCount = this.lineCount
    val lineSpacingExtra = this.lineSpacingExtra
    val lineSpacingMultiplier = this.lineSpacingMultiplier
    val lineSpace = lineSpacingExtra * lineSpacingMultiplier
    if (splitSkeletonTextByLines && lineSpace > 0) {
        for (i in 0 until lineCount) {
            val lineBounds = Rect()
            this.getLineBounds(i, lineBounds)
            if (i != lineCount - 1) {
                lineBounds.bottom -= lineSpace.toInt()
            }

            if (reduceToTextSpace) {
                val boundWidth = lineBounds.width()
                val lineWidth = this.layout.getLineWidth(i)
                val whiteSpace = boundWidth - lineWidth
                val isRtl = this.layoutDirection == View.LAYOUT_DIRECTION_RTL

                when (this.textAlignment) {
                    View.TEXT_ALIGNMENT_CENTER -> {
                        lineBounds.left += (whiteSpace / 2).toInt()
                        lineBounds.right -= (whiteSpace / 2).toInt()
                    }
                    View.TEXT_ALIGNMENT_VIEW_START, View.TEXT_ALIGNMENT_TEXT_START -> {
                        if (isRtl) {
                            lineBounds.left += whiteSpace.toInt()
                        } else {
                            lineBounds.right -= whiteSpace.toInt()
                        }
                    }
                    View.TEXT_ALIGNMENT_VIEW_END, View.TEXT_ALIGNMENT_TEXT_END -> {
                        if (isRtl) {
                            lineBounds.right -= whiteSpace.toInt()
                        } else {
                            lineBounds.left += whiteSpace.toInt()
                        }
                    }
                    View.TEXT_ALIGNMENT_GRAVITY -> {
                        when (this.layout.alignment) {
                            Layout.Alignment.ALIGN_NORMAL -> {
                                if (isRtl) {
                                    lineBounds.left += whiteSpace.toInt()
                                } else {
                                    lineBounds.right -= whiteSpace.toInt()
                                }
                            }
                            Layout.Alignment.ALIGN_CENTER -> {
                                lineBounds.left += (whiteSpace / 2).toInt()
                                lineBounds.right -= (whiteSpace / 2).toInt()
                            }
                            Layout.Alignment.ALIGN_OPPOSITE -> {
                                if (isRtl) {
                                    lineBounds.right -= whiteSpace.toInt()
                                } else {
                                    lineBounds.left += whiteSpace.toInt()
                                }
                            }
                        }
                    }
                    else -> {
                        lineBounds.right -= whiteSpace.toInt()
                    }
                }
            }
            allLineBounds.add(lineBounds)
        }
    } else {
        allLineBounds.add(Rect(0, 0, this.width, this.height))
    }
    return allLineBounds
}