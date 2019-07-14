package app.sonderful.index

import app.sonderful.index.view.SearchView
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.App

class Index: App(SearchView::class) {
    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)
        stage.minHeight = 75.0
        stage.maxHeight = 75.0
        stage.minWidth = 580.0
        stage.maxWidth = 580.0

        super.start(stage)
    }
}
