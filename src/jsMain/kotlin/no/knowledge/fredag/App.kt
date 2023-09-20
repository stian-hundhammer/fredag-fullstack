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
    // var articleList by useState(emptyList<Article>())
    var article: Article? by useState<Article>()

    useEffectOnce {
        scope.launch {
            // function in Api
            // load data, call function in Api
            article = getArticle("two")
        }
    }

    h1 {
        +"${article?.header}"
    }

    div {
        +"${article?.body}"
    }
}