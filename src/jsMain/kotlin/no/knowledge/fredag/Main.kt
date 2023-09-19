package no.knowledge.fredag

import react.create
import react.dom.client.createRoot
import web.dom.document

/**
 * js main
 * set up "root" div and render it with App
 */
fun main() {
    val container = document.getElementById("root") ?: error("Can not find container named root!")
    createRoot(container).render(App.create())
}