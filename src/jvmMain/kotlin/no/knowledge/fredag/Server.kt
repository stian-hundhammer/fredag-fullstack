package no.knowledge.fredag

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// dummy data to start with
val articleList = listOf("one", "two", "three").map {
    Article(
        id = "id-$it",
        header = "header $it",
        body = "body $it",
        comments = listOf(
            Comment(id = "comment-id-$it", userName = "user $it", text = "comment text $it"),
            Comment(id = "comment-id-x-$it", userName = "user x-$it", text = "comment text x-$it")
        )
    )
}
fun main() {

    embeddedServer(Netty, port = 8080) {

        install(ContentNegotiation) {
            json()
        }

        install(CORS) {
            allowMethod(HttpMethod.Get)

            anyHost()
        }

        install(Compression) {
            gzip {
                priority = 1.0
            }
            deflate {
                priority = 10.0
                minimumSize(1024) // condition
            }
        }

        routing {
            get("/") {
                call.respondText(
                    text = javaClass.classLoader.getResource("index.html")!!.readText(),
                    contentType = ContentType.Text.Html,
                )
            }

            static("/") {
                resources("")
            }

            route(Article.path) {
                get {
                    call.respond(articleList)
                }
            }
        }
    }.start(wait = true)
}