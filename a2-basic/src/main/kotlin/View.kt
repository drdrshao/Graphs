import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import java.lang.Double.max
import java.lang.Double.min
import java.lang.System.gc
import java.util.Random
import kotlin.math.sqrt


class View {
    var model :Model? = null
    var canv = Canvas(600.0, 600.0)
    var canvPane = VBox(canv)

    val colorPalette = listOf<Color>(
        Color.RED, Color.ORANGERED, Color.DARKORANGE, Color.ORANGE,
        Color.YELLOW, Color.YELLOWGREEN, Color.LIGHTGREEN,
        Color.GREEN, Color.LIMEGREEN, Color.DARKGREEN,
        Color.CYAN, Color.BLUE, Color.BLUEVIOLET, Color.VIOLET
    )
    var colorIndex = 0

    init {
        canv.widthProperty().bind(canvPane.widthProperty())
        canv.heightProperty().bind(canvPane.heightProperty())
        canvPane.minWidth = 10.0
        canvPane.minHeight = 0.0
        canvPane.prefHeight = 526.0
    }

    fun createVisualizaion(dataset: String, graphType: String) {
        canv.graphicsContext2D.clearRect(0.0, 0.0, canv.width, canv.height)
        when (graphType) {
            "Line" -> {
                createLineGraph(dataset)
            }
            "Bar" -> {
                createBarGraph(dataset)
            }
            "Bar(SEM)" -> {
                createBarSEMGraph(dataset)
            }
            "Pie" -> {
                createPieGraph(dataset)
            }
        }
    }

    private fun createLineGraph(dataset: String) {
        val data = model?.getDataByName(dataset)!!
        if (data.size == 1) {
            canv.graphicsContext2D.apply {
                fill = Color.RED
                fillOval(canv.width / 2, canv.height - 20, 10.0, 10.0)
            }
            return
        }
        val canvWidth = canv.width - 20
        val canvHeight = canv.height - 20
        val xdiff = data.size
        val ydiff = data.max() - data.min()+0.0001

        for (i in 0 until data.size - 1) {
            val cur = data[i]
            val next = data[i+1]
            val curX = i * canvWidth / (xdiff - 1) + 10
            val curY = canvHeight - (cur - data.min()) * canvHeight / ydiff + 10
            val nextX = (i + 1) * canvWidth / (xdiff - 1) + 10
            val nextY = canvHeight - (next - data.min()) * canvHeight / ydiff + 10

            canv.graphicsContext2D.apply {
                fill = Color.BLACK
                lineWidth = 5.0
                strokeLine(curX, curY, nextX, nextY)
            }
        }

        for (i in 0 until data.size) {
            val cur = data[i]
            val curX = i * canvWidth / (xdiff - 1) + 5
            val curY = canvHeight - (cur - data.min()) * canvHeight / ydiff + 5

            canv.graphicsContext2D.apply {
                fill = Color.RED
                fillOval(curX, curY, 10.0, 10.0)
            }
        }
    }

    private fun createBarGraph(dataset: String) {
        colorIndex = 0
        val data = model?.getDataByName(dataset)!!

        val canvWidth = canv.width - 40
        val canvHeight = canv.height - 20
        val xdiff = data.size
        var mins = min(data.min(), 0.0)
        var maxs = max(data.max(), 0.0)
        var ydiff = maxs - mins+0.0001
        val zeroY = canvHeight - (0 - mins) * canvHeight / ydiff + 5
        if (data.size == 1) {
            if (data[0] < 0) {
                canv.graphicsContext2D.apply {
                    fill = Color.RED
                    fillRect(canvWidth / 2, 20.0, 10.0, canvHeight)
                    fill = Color.BLACK
                    lineWidth = 3.0
                    strokeLine(10.0, 20.0, canvWidth + 30, 20.0)
                }
            } else if (data[0] > 0){
                canv.graphicsContext2D.apply {
                    fill = Color.RED
                    fillRect(canvWidth / 2, 20.0, 10.0, canvHeight - 20)
                    fill = Color.BLACK
                    lineWidth = 3.0
                    strokeLine(10.0, canvHeight, canvWidth + 30, canvHeight)
                }
            } else {
                canv.graphicsContext2D.apply {
                    fill = Color.BLACK
                    lineWidth = 3.0
                    strokeLine(10.0, canvHeight, canvWidth + 30, canvHeight)
                }
            }
            return
        }
        for (i in 0 until data.size) {
            val cur = data[i]
            val curX = i * canvWidth / (xdiff - 1) + 15
            val curY = canvHeight - (cur - mins) * canvHeight / ydiff + 5
            canv.graphicsContext2D.apply {
                fill = generateRandomColor(1)
                if (cur > 0) {
                    fillRect(curX, curY, 10.0, zeroY - curY)
                } else {
                    fillRect(curX, zeroY, 10.0, curY - zeroY)
                }
            }
        }

        canv.graphicsContext2D.apply {
            fill = Color.BLACK
            lineWidth = 3.0
            strokeLine(10.0, zeroY, canvWidth + 30, zeroY)
        }
    }

    fun createBarSEMGraph(dataset: String) {
        colorIndex = 0
        val data = model?.getDataByName(dataset)!!

        if (data.min() < 0) {
            return
        }
        val canvWidth = canv.width - 40
        val canvHeight = canv.height - 20
        val xdiff = data.size
        var mins = min(data.min(), 0.0)
        var maxs = max(data.max(), 0.0)
        var ydiff = maxs - mins +0.0001
        val zeroY = canvHeight - (0 - mins) * canvHeight / ydiff + 5

        if (data.size == 1) {
            if (data[0] < 0) {
                canv.graphicsContext2D.apply {
                    fill = Color.RED
                    fillRect(canvWidth / 2, 20.0, 10.0, canvHeight)
                    fill = Color.BLACK
                    lineWidth = 3.0
                    strokeLine(10.0, 20.0, canvWidth + 30, 20.0)
                }
            } else if (data[0] > 0){
                canv.graphicsContext2D.apply {
                    fill = Color.RED
                    fillRect(canvWidth / 2, 20.0, 10.0, canvHeight - 20)
                    fill = Color.BLACK
                    lineWidth = 3.0
                    strokeLine(10.0, canvHeight + 5, canvWidth + 30, canvHeight + 5)
                }
            } else {
                canv.graphicsContext2D.apply {
                    fill = Color.BLACK
                    lineWidth = 3.0
                    strokeLine(10.0, canvHeight + 5, canvWidth + 30, canvHeight + 5)
                }
            }

        } else {
            for (i in 0 until data.size) {
                val cur = data[i]
                val curX = i * canvWidth / (xdiff - 1) + 15
                val curY = canvHeight - (cur - mins) * canvHeight / ydiff + 5
                canv.graphicsContext2D.apply {
                    fill = generateRandomColor(1)
                    if (cur > 0) {
                        fillRect(curX, curY, 10.0, zeroY - curY)
                    } else {
                        fillRect(curX, zeroY, 10.0, curY - zeroY)
                    }
                }
            }
        }
        canv.graphicsContext2D.apply {
            fill = Color.BLACK
            lineWidth = 3.0
            strokeLine(10.0, zeroY, canvWidth + 30, zeroY)
            val n = data.size
            val mean = data.sum() / n
            val meanY = canvHeight - (mean - mins) * canvHeight / ydiff + 5
            lineWidth = 1.0
            strokeLine(10.0, meanY, canvWidth + 30, meanY)

            var tmp_sum = 0.0
            for (i in 0 until data.size) {
                tmp_sum += (data[i] - mean) * (data[i] - mean)
            }
            val sd = sqrt(tmp_sum / n)
            val sem = sd / sqrt(n.toDouble())
            setLineDashes(5.0)
            val semU = canvHeight - (mean + sem - mins) * canvHeight / ydiff + 5
            val semD = canvHeight - (mean - sem - mins) * canvHeight / ydiff + 5
            strokeLine(10.0, semU, canvWidth + 30, semU)
            strokeLine(10.0, semD, canvWidth + 30, semD)
            setLineDashes(0.0)
            fill = Color.color(1.0, 1.0, 1.0, 0.5)
            fillRect(10.0, 10.0, 200.0, 60.0)
            fill = Color.BLACK
            fillText("mean: $mean", 30.0, 30.0)
            fillText("SEM: $sem", 30.0, 60.0)
        }
    }

    fun createPieGraph(dataset: String) {
        colorIndex = 0
        val data = model?.getDataByName(dataset)!!

        if (data.min() < 0) {
            return
        }
        val canvWidth = canv.width - 40
        val canvHeight = canv.height - 20
        val radius = min(canvWidth, canvHeight)

        val centerX = (canv.width - radius) / 2.0
        val centerY = (canv.height - radius) / 2.0
        var angle = 0.0
        for (i in 0 until data.size) {
            val cur = data[i] / data.sum() * 360.0
            canv.graphicsContext2D.apply {
                fill = generateRandomColor(2)
                fillArc(centerX, centerY, radius, radius, angle, cur, ArcType.ROUND)
            }
            angle += cur
        }
    }


    fun generateRandomColor(gap: Int): Color {
        val c = colorPalette[colorIndex]
        colorIndex = (colorIndex + gap) % colorPalette.size
        return c
    }
}