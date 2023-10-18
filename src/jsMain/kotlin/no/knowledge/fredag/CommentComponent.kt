package no.knowledge.fredag

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.li
import react.useState

external interface OneCommentProps: Props {
    var comment: Comment
}

val commentComponent = FC<OneCommentProps> { props ->
    li {
        ReactHTML.strong {
            +"${props.comment.userName}: "
        }
        +"${props.comment.text}"
    }
}

