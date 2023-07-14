import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.VPos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

class Controller{
    val panel = BorderPane()
    val toolbar = HBox()
    var datasetChoice = ChoiceBox<String>()
    var inputArea = TextField()
    var createButton = Button("Create")
    var lineButton = ToggleButton("Line")
    var barButton = ToggleButton("Bar")
    var barSEMButton = ToggleButton("Bar(SEM)")
    var pieButton = ToggleButton("Pie")
    val graphSelector = HBox(lineButton, barButton, barSEMButton, pieButton)
    val toolbarHeight = 20.0
    val buttonWidth = 75.0
    val datasetName = Label("Dataset name: ")
    var datasetPane = ScrollPane()
    val datasetVBox = VBox()

    var model: Model? = null

    val defaultPadding = Insets(10.0, 10.0, 10.0, 10.0)
    fun createController(): BorderPane {
        lineButton.prefWidth = buttonWidth
        barButton.prefWidth = buttonWidth
        barSEMButton.prefWidth = buttonWidth
        pieButton.prefWidth = buttonWidth
        createButton.prefWidth = buttonWidth
        createButton.minWidth = 0.0
        lineButton.minWidth = 0.0
        barButton.minWidth = 0.0
        barSEMButton.minWidth = 0.0
        pieButton.minWidth = 0.0
        graphSelector.minWidth = 0.0

        inputArea.maxHeight = toolbarHeight
        inputArea.maxWidth = 100.0
        toolbar.maxHeight = toolbarHeight
        toolbar.minWidth = 0.0
        datasetChoice.maxHeight = toolbarHeight
        model?.let { datasetChoice.items.addAll(it.getDataSetsNames()) }

        toolbar.children.addAll(datasetChoice, createSeparator(),
            inputArea, createButton, createSeparator(), graphSelector)
        panel.top = toolbar

        createButton.onMouseClicked = EventHandler {
            if (inputArea.text.isNotEmpty()) {
                model?.addDataset(inputArea.text)
                inputArea.clear()
            }
        }

        datasetChoice.selectionModel.selectedItemProperty()
            .addListener { _, _, new:String? ->
                if (new != null) {
                    chooseDataset(new)
                }
            }

        lineButton.onMouseClicked = EventHandler {
            model?.changeGraphType(datasetChoice.selectionModel.selectedItem, "Line")
            barButton.isSelected = false
            barSEMButton.isSelected = false
            pieButton.isSelected = false
        }
        barButton.onMouseClicked = EventHandler {
            model?.changeGraphType(datasetChoice.selectionModel.selectedItem, "Bar")
            lineButton.isSelected = false
            barSEMButton.isSelected = false
            pieButton.isSelected = false
        }
        barSEMButton.onMouseClicked = EventHandler {
            model?.changeGraphType(datasetChoice.selectionModel.selectedItem, "Bar(SEM)")
            lineButton.isSelected = false
            barButton.isSelected = false
            pieButton.isSelected = false
        }
        pieButton.onMouseClicked = EventHandler {
            model?.changeGraphType(datasetChoice.selectionModel.selectedItem, "Pie")
            lineButton.isSelected = false
            barButton.isSelected = false
            barSEMButton.isSelected = false
        }

        return panel
    }

    fun createDatasetController(): ScrollPane {
        datasetName.padding = defaultPadding
        datasetVBox.children.add(datasetName)
        datasetPane = ScrollPane(datasetVBox)
        datasetPane.isFitToWidth = true
        datasetPane.minWidth = 0.0

        datasetPane.widthProperty().addListener { _, _, new ->
            datasetVBox.prefWidth = new.toDouble()
        }
        datasetChoice.selectionModel.select(0)


        return datasetPane
    }

    private fun chooseDataset(name: String) {

        datasetVBox.children.clear()
        val data = model?.getDataByName(name)!!
        datasetName.text = "Dataset name: $name"
        datasetVBox.children.add(datasetName)
        for (e in 0 until data.size) {
            val cur = data[e]
            val curLabel = Label("Entry #$e")
            val curInput = TextField()
            val deleteButton = Button("X")
            if (data.size == 1) {
                deleteButton.isDisable = true
            }

            curInput.prefHeight = toolbarHeight + 5
            curInput.text = cur.toString()
            curInput.textProperty().addListener{ _,_,new ->
                model!!.changeData(name, e, new)
                if (data.min() < 0.0) {
                    barSEMButton.isDisable = true
                    pieButton.isDisable = true
                } else {
                    barSEMButton.isDisable = false
                    pieButton.isDisable = false
                }
            }

            curLabel.padding = Insets(5.0, 0.0, 5.0, 10.0)
            deleteButton.prefHeight = toolbarHeight + 5
            deleteButton.onMouseClicked = EventHandler {
                model!!.deleteData(name, e)
            }
            val entry = HBox(10.0, curLabel, curInput, deleteButton)
            entry.padding = Insets(5.0, 10.0, 5.0, 0.0)
            HBox.setHgrow(curInput, Priority.ALWAYS)
            datasetVBox.children.add(entry)
        }
        val addEntryButton = Button("Add Entry")
        addEntryButton.maxWidth = 1000000.0
        val addEntryBox = HBox(10.0, addEntryButton)
        addEntryBox.padding = Insets(5.0, 10.0, 5.0, 10.0)

        addEntryButton.onMouseClicked = EventHandler {
            model!!.addData(name, "0.0")
        }

        HBox.setHgrow(addEntryButton, Priority.ALWAYS)
        datasetVBox.children.add(addEntryBox)
        model!!.chooseDataset(name)

        if (data.min() < 0.0) {
            barSEMButton.isDisable = true
            pieButton.isDisable = true
        } else {
            barSEMButton.isDisable = false
            pieButton.isDisable = false
        }
    }

    fun notifyChanges(name: String) {
        datasetChoice.items = FXCollections.observableList(model?.getDataSetsNames() )
        datasetChoice.selectionModel.select(name)
        datasetChoice.minWidth = 141.0
        chooseDataset(name)
    }

    fun resize() {
        model!!.changeGraphType(datasetChoice.selectionModel.selectedItem, model!!.curGraphType)
    }

    private fun createSeparator(): Separator {
        val divider = Separator()
        divider.orientation = Orientation.VERTICAL
        divider.valignment = VPos.CENTER
        divider.padding = Insets(0.0, 20.0, 0.0, 20.0)
        return divider
    }
}