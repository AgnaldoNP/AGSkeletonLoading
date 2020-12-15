package aglibs.loading.skeleton.util

import android.graphics.Rect
import android.text.Layout
import android.view.View
import android.widget.TextView

fun TextView.getAllLineBounds(
    splitSkeletonTextByLines: Boolean = true,
    clipToText: Boolean = true
): ArrayList<Rect> {
    val allLineBounds = ArrayList<Rect>()
    val lineCount = this.lineCount
    if (splitSkeletonTextByLines) {
        for (line in 0 until lineCount) {
            allLineBounds.add(
                getSkeletonLineBounds(
                    this, line,
                    splitSkeletonTextByLines,
                    clipToText
                )
            )
        }
    } else {
        allLineBounds.add(
            Rect(0, 0, this.width, this.height)
        )
    }
    return allLineBounds
}

private fun getSkeletonLineBounds(
    textView: TextView,
    line: Int,
    splitSkeletonTextByLines: Boolean,
    clipToText: Boolean
): Rect {
    val lineCount = textView.lineCount
    val lineSpacingExtra = textView.lineSpacingExtra
    val lineSpacingMultiplier = textView.lineSpacingMultiplier
    val lineSpace = lineSpacingExtra * lineSpacingMultiplier

    val lineBounds = Rect()
    textView.getLineBounds(line, lineBounds)

    if (splitSkeletonTextByLines) {
        if (line != lineCount - 1) {
            lineBounds.bottom -= lineSpace.toInt()
        }
    }

    if (clipToText) {
        val boundWidth = lineBounds.width()
        val lineWidth = textView.layout.getLineWidth(line)
        val whiteSpace = boundWidth - lineWidth
        val isRtl = textView.layoutDirection == View.LAYOUT_DIRECTION_RTL

        when (textView.textAlignment) {
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
                when (textView.layout?.alignment) {
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

    return lineBounds
}
