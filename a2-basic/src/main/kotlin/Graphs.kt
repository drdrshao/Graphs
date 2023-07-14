import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.SplitPane
import javafx.scene.paint.Color
import javafx.stage.Stage


class Graphs: Application(){
    val WINDOW_NAME = "CS349 - A2 Graphs - g3shao"
    val STAGE_WIDTH = 800.0
    val STAGE_HEIGHT = 600.0
    val STAGE_MIN_WIDTH = 640.0
    val STAGE_MIN_HEIGHT = 480.0
    override fun start(stage: Stage) {
        val model = Model()
        val controller = Controller()
        val view = View()
        controller.model = model
        view.model = model
        model.cont = controller
        model.view = view

        val panel = controller.createController()
        val datasetControl = controller.createDatasetController()
        val viewCanvas = view.canvPane
        val sp = SplitPane(datasetControl, viewCanvas)
        sp.minHeight = 0.0
        sp.minWidth = 0.0

        viewCanvas.widthProperty().addListener { _, _, new ->
            datasetControl.prefWidth = new.toDouble() / 2.0
            viewCanvas.prefWidth = new.toDouble() / 2
            controller.resize()
        }

        viewCanvas.heightProperty().addListener { _, _, new ->
            datasetControl.prefHeight = new.toDouble()
            viewCanvas.prefHeight = new.toDouble()
            controller.resize()
        }

        panel.center = sp
        val scene = Scene(panel)
        stage.scene = scene
        stage.title = WINDOW_NAME
        stage.minWidth = STAGE_MIN_WIDTH
        stage.minHeight = STAGE_MIN_HEIGHT
        stage.scene = scene
        stage.width = STAGE_WIDTH
        stage.height = STAGE_HEIGHT
        stage.show()
        view.createVisualizaion("quadratic", "Line")
    }
}