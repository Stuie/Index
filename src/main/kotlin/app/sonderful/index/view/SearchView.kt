package app.sonderful.index.view

import app.sonderful.index.controller.SearchController
import app.sonderful.index.model.SearchResultModel
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import tornadofx.*

class SearchView: View() {
    private val model = ViewModel()
    private val controller: SearchController by inject()
    private val searchTerm = model.bind { SimpleStringProperty("") }

    private var searchField: TextField by singleAssign()
    private var resultsListView: ListView<SearchResultModel> by singleAssign()

    override val root = group {
        form {
            fieldset {
                field {
                    searchField = textfield(searchTerm) {
                        action {
                            Platform.runLater {
                                executeSearch(searchTerm.value)
                            }
                        }
                        onKeyReleased = EventHandler { keyEvent ->
                            when (keyEvent.code) {
                                KeyCode.ESCAPE -> return@EventHandler
                                else -> executeSearch(searchTerm.value)
                            }
                        }
                    }
                }
            }
        }
        resultsListView = listview(controller.results) {
            cellFormat {
                text = it.name
            }
            style {
                selectionModel.selectionMode = SelectionMode.SINGLE
                visibleWhen {
                    controller.resultsNotEmptyProperty
                }
            }
            whenVisible {
                currentStage?.sizeToScene()
            }
            whenHidden {
                currentStage?.sizeToScene()
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.scene?.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            when (event.code) {
                KeyCode.ESCAPE -> currentStage?.close()
                else -> return@addEventHandler
            }
        }
    }

    private fun executeSearch(searchTerm: String) {
        if (searchTerm.isNotEmpty()) {
            controller.search(searchTerm)
        } else {
            controller.clearResults()
        }
    }
}

fun Node.whenHidden(runLater: Boolean = true, op: () -> Unit) {
    visibleProperty().onChange {
        if (!it) {
            if (runLater) Platform.runLater(op) else op()
        }
    }
}