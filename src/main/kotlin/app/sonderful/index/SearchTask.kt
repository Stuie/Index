package app.sonderful.index

import app.sonderful.index.model.SearchResultModel

class SearchTask {
    fun run(searchTerm: String): List<SearchResultModel> {
        val results = mutableListOf(
            SearchResultModel("UUID-0", "Calc", "C:\\Windows\\System32\\calc.exe"),
            SearchResultModel("UUID-1", "Notepad", "C:\\Windows\\System32\\notepad.exe"),
            SearchResultModel("UUID-2", "cmd", "C:\\Windows\\System32\\cmd.exe"),
            SearchResultModel("UUID-3", "Steam", "C:\\Program Files (x86)\\Steam\\Steam.exe")
        )

        return results.filter { it.name.contains(searchTerm, ignoreCase = true) }
    }
}