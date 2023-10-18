package no.knowledge.fredag

import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.textarea
import react.useState
import web.cssom.ClassName
import web.html.HTMLFormElement
import web.html.HTMLInputElement
import web.html.HTMLTextAreaElement
import web.html.InputType

external interface CommentProps : Props {
    var onSubmit: (String, String, String?) -> Unit
}

val articleCommentComponent = FC<CommentProps> { props ->
    val (userName, setUserName) = useState("")
    val (text, setText) = useState("")
    val (commentId, setCommentId) = useState(null)

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        setText("")
        setUserName("")
        props.onSubmit(userName, text, commentId)
    }

    val userNameChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setUserName(it.target.value)
    }

    val textChangeHandler: ChangeEventHandler<HTMLTextAreaElement> = {
        setText(it.target.value)
    }

    form {

        className = ClassName("comment-form")
        onSubmit = submitHandler

        label {
            htmlFor = "userName"
            +"Navn:"
        }

        input {
            name = "userName"
            type = InputType.text
            onChange = userNameChangeHandler
            value = userName
        }

        br

        label {
            htmlFor = "text"
            +"Kommentar:"
        }

        textarea {
            name = "text"
            className = ClassName("commenttext")
            onChange = textChangeHandler
            value = text
        }

        button {
            +"Flesk inn!"
        }
    }
}