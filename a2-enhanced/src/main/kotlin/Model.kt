import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import kotlin.random.Random


class Model() {
    var cont: Controller? = null
    var view: View? = null
    private var datas = LinkedHashMap<String, ArrayList<Double>>()
    var curGraphType = "Line"
    var activeDataset = "quadratic"

    init {
        datas["quadratic"] = ArrayList()
        for (i in 1..4) {
            datas["quadratic"]?.add(i * i.toDouble())
        }
        datas["negative quadratic"] = ArrayList()
        for (i in 1..4) {
            datas["negative quadratic"]?.add(-1.0 * i * i)
        }
        datas["alternating"] = arrayListOf(-1.0, 3.0, -1.0, 3.0, -1.0, 3.0)
        datas["random"] = ArrayList()
        for (i in 0..19) {
            datas["random"]?.add(Random.nextDouble(-100.0, 100.0))
        }
        datas["inflation '90-'22"] = arrayListOf(4.8, 5.6, 1.5, 1.9, 0.2, 2.1, 1.6, 1.6, 1.0, 1.7, 2.7, 2.5,
            2.3, 2.8, 1.9, 2.2, 2.0, 2.1, 2.4, 0.3, 1.8, 2.9, 1.5, 0.9, 1.9, 1.1, 1.4, 1.6,
            2.3, 1.9, 0.7, 3.4, 6.8)
    }

    fun addData(dataset: String, data: String): Boolean {
        try {
            val dataDouble = data.toDouble()
            if (!datas.containsKey(dataset)) {
                datas[dataset] = ArrayList()
            }
            datas[dataset]?.add(dataDouble)

        } catch (e: Exception) {
            return false
        }

        cont?.notifyChanges(dataset)
        view?.createVisualizaion(dataset, curGraphType)
        return true
    }

    fun addDataset(dataset: String) {
        if (datas.containsKey(dataset)) {
            val a = Alert(AlertType.ERROR)
            a.contentText = "You cannot add a dataset that has the same name as an existing dataset."
            a.show()
            return
        }
        datas[dataset] = ArrayList()
        datas[dataset]?.add(0.0)
        cont?.notifyChanges(dataset)
        view?.createVisualizaion(dataset, curGraphType)
    }

    fun deleteData(dataset: String, index: Int) {
        datas[dataset]?.removeAt(index)
        cont?.notifyChanges(dataset)
        view?.createVisualizaion(dataset, curGraphType)
    }

    fun changeData(dataset: String, index: Int, data: String) {
        try {
            val dataDouble = data.toDouble()

            if (datas[dataset]?.min()!! >= 0.0 && dataDouble < 0.0) {
                datas[dataset]?.set(index, dataDouble)
                changeGraphType(dataset, "Line")
            } else {
                datas[dataset]?.set(index, dataDouble)
                view?.createVisualizaion(dataset, curGraphType)
            }
        } catch (_: Exception) {

        }
    }

    fun changeGraphType(dataset: String, target: String) {
        curGraphType = target
        view!!.createVisualizaion(dataset, curGraphType)
    }

    fun chooseDataset(dataset: String) {
        if (datas.containsKey(dataset)) {
            activeDataset = dataset
            view?.createVisualizaion(dataset, curGraphType)
        }
    }

    fun getDataSetsNames(): List<String> {
        return datas.keys.toList()
    }

    fun getDataByName(name: String): ArrayList<Double>? {
        return datas[name]
    }
}