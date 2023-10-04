package no.knowledge.fredag

import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState
import web.cssom.ClassName

//
// Scope for UI so we can use suspend functions
//
private val scope = MainScope()


// React app
val App = FC<Props> { props ->

    // properties
    var article: Article? by useState()
    var articleList: List<Article> by useState(emptyList())

    useEffectOnce {
        scope.launch {
            // function in Api
            // load data, call function in Api
            article = getArticle()
            articleList = getArticleList()
        }
    }

    div {
        id = "header"
        className = ClassName("header")
        +"Siden det er fredag..."
    }

    div {
        id = "navigation"
        className = ClassName("navigation")
        // +"navigation"
    }

    div {
        id = "row"
        className = ClassName("row")

        div {

            id = "article"
            className = ClassName("article")

            h1 {
                +"${article?.header}"
            }

            div {
                dangerouslySetInnerHTML = jso {
                    __html = article?.body ?: ""
                }
            }

            div {
                id = "comment-form"
                // +"comment form here!"
            }

            div {
                className = ClassName("commenttext")
                ul {
                    article?.comments?.forEach {
                        li {
                            +"${it.userName} : ${it.text}"
                        }
                    }
                }
            }
        }
    }

    div {
        className = ClassName("footer")
        id = "footer"
        // +"footer"
    }
}

fun String.startOf() = this.substring(0, kotlin.math.min(20, this.length))