package no.knowledge.fredag

import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.textarea
import react.useState
import web.html.HTMLFormControlsCollection
import web.html.HTMLFormElement
import web.html.HTMLInputElement
import web.html.InputType

external interface InputProps : Props {
    var onSubmit: (String) -> Unit
}

val inputComponent = FC<InputProps> {props ->
    val (text, setText) = useState("")

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        setText("")
        props.onSubmit(text)
    }

    val changeHandler: ChangeEventHandler<HTMLInputElement> = {
        setText(it.target.value)
    }

    h2 {
        +"Flesk inn!"
    }

    form {
        onSubmit = submitHandler
        input {
            type = InputType.text
            onChange = changeHandler
            value = text
        }

        textarea {

        }

        button {
            +"Flesk inn!"
        }
    }
}