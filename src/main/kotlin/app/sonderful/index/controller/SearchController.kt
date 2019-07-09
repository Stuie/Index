package app.sonderful.index.controller

import app.sonderful.index.SearchTask
import app.sonderful.index.model.SearchResultModel
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.Controller

class SearchController: Controller() {
    val results: ObservableList<SearchResultModel> = FXCollections.observableArrayList()
    val resultsNotEmptyProperty = SimpleBooleanProperty(false)

    fun search(searchTerm: String) {
        results.clear()
        results.addAll(SearchTask().run(searchTerm))
        resultsNotEmptyProperty.set(results.isNotEmpty())
    }

    fun clearResults() {
        results.clear()
        resultsNotEmptyProperty.set(false)
    }

    fun execute(searchResultModel: SearchResultModel) {
        println(searchResultModel.name)
    }
}