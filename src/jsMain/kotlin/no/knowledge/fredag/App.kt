package no.knowledge.fredag

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState

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
            article = getArticle("two")
            articleList = getArticleList()
        }
    }

    div {
        id = "article"

        h1 {
            +"${article?.header}"
        }


        div {
            +"${article?.body}"
        }
    }

    div {
        key = "articleList"
        ul {
            articleList.forEach {
                li {
                    key = it.id
                    +"${it.header.startOf()}"
                    onClick = {
                        scope.launch {
                            article = getArticle(key)
                        }
                    }
                }
            }
        }
    }
}

fun String.startOf() = this.substring(0, kotlin.math.min(20, this.length))