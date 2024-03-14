package no.knowledge.fredag

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.li
import kotlin.js.Date

external interface OneCommentProps : Props {
    var comment: Comment
}

val commentComponent = FC<OneCommentProps> { props ->
    li {
        +"${Date(props.comment.id).toLocaleDateString()} ${Date(props.comment.id).toLocaleTimeString()} "
        ReactHTML.strong {
            +"${props.comment.userName}"
        }
        +": ${props.comment.text}"
    }
}
