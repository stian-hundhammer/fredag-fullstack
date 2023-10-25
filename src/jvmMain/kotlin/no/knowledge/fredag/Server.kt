package no.knowledge.fredag

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val logger = LoggerFactory.getLogger("main-server-backend")

    val fredagService = FredagService(
        legacyDataLocation = environment.config.propertyOrNull("fredag.legacyDataLocation")?.getString(),
        articleStoreLocation = environment.config.propertyOrNull("fredag.articles")?.getString()!!
    )

    fredagService.loadLegacyData()

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
                contentType = ContentType.Text.Html
            )
        }

        staticFiles(
            remotePath = "/pict/fredag/",
            dir = File(environment?.config?.propertyOrNull("fredag.pict")?.getString() ?: "pict")
        )

        staticFiles(
            remotePath = "/mp3/",
            dir = File(environment?.config?.propertyOrNull("fredag.music")?.getString() ?: "music")
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

                // val a = if (id.isNullOrBlank()) fr edagService.currentArticle()
                val a = if (id == null) {
                    fredagService.currentArticle()
                } else {
                    fredagService.articleList.find { it.id == id }
                }

                if (a != null) {
                    call.respond(a)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }

        route(ArticleRef.articleRefPath) {
            get() {
                call.respond(fredagService.articleRefList)
            }
        }

        route(Comment.commentPath) {
            post {
                val comment = call.receive<Comment>().copy(
                    // yet another hack...psaudo-random longs with meaning
                    System.currentTimeMillis()
                )
                fredagService.addComment(comment)
                logger.debug("${Comment.commentPath}:post: $comment")
            }
        }
    }
}
