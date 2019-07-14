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
import javafx.scene.paint.Color
import tornadofx.*
import java.io.File

class SearchView: View() {
    private val maxResults = 6
    private val resultHeight = 50

    private val model = ViewModel()
    private val controller: SearchController by inject()
    private val searchTerm = model.bind { SimpleStringProperty("") }

    private var searchField: TextField by singleAssign()
    private var resultsListView: ListView<SearchResultModel> by singleAssign()

    override val root = group {
        vbox {
            form {
                fieldset {
                    field {
                        searchField = textfield(searchTerm) {
                            action {
                                Platform.runLater {
                                    executeSearch(searchTerm.value)
                                }
                            }
                            style {
                                minWidth = 580.px
                                maxWidth = 580.px
                                minHeight = 75.px
                                maxHeight = 75.px
                                paddingAll = 0
                                fontSize = 75.px
                                prefHeight = 75.px
                            }
                            onKeyReleased = EventHandler { keyEvent ->
                                when (keyEvent.code) {
                                    KeyCode.ESCAPE -> return@EventHandler
                                    KeyCode.DOWN -> {
                                        resultsListView.requestFocus()
                                    }
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
                    backgroundColor = multi(Color.WHITE, Color.WHITE)
                    maxHeight = maxResults * resultHeight.px
                    minHeight = 0.px
                    prefHeight = 0.px
                    maxWidth = 580.px
                    minWidth = 0.px
                    prefWidth = 0.px
                    paddingAll = 0
                }
                whenVisible {
                    prefHeight = items.size * resultHeight.toDouble()
                    maxWidth = 580.0
                    currentStage?.sizeToScene()
                }
                whenHidden {
                    currentStage?.sizeToScene()
                }
                onKeyReleased = EventHandler { keyEvent ->
                    when (keyEvent.code) {
                        KeyCode.ENTER -> {
                            selectedItem?.let {
                                val file = File(it.path)
                                val processBuilder = ProcessBuilder(file.absolutePath)
                                processBuilder.directory(file.absoluteFile.parentFile)
                                processBuilder.start()
                                currentStage?.close()
                            }
                        }
                        KeyCode.ESCAPE -> currentStage?.close()
                        else -> return@EventHandler
                    }
                }
                fixedCellSize = resultHeight.toDouble()
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