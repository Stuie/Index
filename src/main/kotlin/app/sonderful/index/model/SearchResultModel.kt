package app.sonderful.index.model

/**
 * Encapsulates an item that can be displayed as a search result.
 *
 * @param id is a unique identifier for the result
 * @param name is the name to be displayed to the user
 * @param path is the filesystem location of the result, which can be used for launching an app, getting an icon, etc.
 */
data class SearchResultModel(val id: String, val name: String, val path: String)