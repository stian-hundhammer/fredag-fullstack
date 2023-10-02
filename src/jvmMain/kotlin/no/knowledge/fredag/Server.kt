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
import org.slf4j.LoggerFactory
import java.io.File

fun main() {

    val logger = LoggerFactory.getLogger("main-server-backend")
    val fredagService = FredagService("/Users/stianhundhammer/devel/projects/fredag/fredagsdata-json.txt")
    fredagService.loadLegacyData()

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

            val pictures = File("pictures")
            val musicDir = File("music")

            staticFiles(
                remotePath = "/pict",
                dir = pictures
                )

            staticFiles(
                remotePath = "/mp3",
                dir = musicDir
            )

            static("/") {
                resources("")
            }

            route(Article.articleListPath) {
                get {
                    call.respond(fredagService.articleList)
                }
            }

            route(Article.articlePath) {
                get {
                    call.respond(fredagService.currentArticle())
                }

                get("/{id}") {
                    val id = call.parameters["id"]?.toLong()
                    logger.info("id: $id")

                    //val a = if (id.isNullOrBlank()) fr edagService.currentArticle()
                    val a = if (id == null) fredagService.currentArticle()
                        else fredagService.articleList.find { it.id == id }

                    if (a != null) {
                        call.respond(a)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }.start(wait = true)
}