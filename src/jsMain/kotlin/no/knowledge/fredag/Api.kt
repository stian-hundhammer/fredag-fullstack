package no.knowledge.fredag

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

/**
 * Set up a JavaScript client
 * Do call on the client to talk to the backend
 */
val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

//
// define suspend functions used from frontend/react
//

suspend fun getArticleList(): List<Article> = jsonClient.get(Article.articleListPath).body()

suspend fun getArticle(id: String? = null): Article {
    val url = if (id == null) {
        Article.articlePath
    } else {
        Article.articlePath + "/$id"
    }

    return jsonClient.get(url).body()
}

suspend fun getArticleRefList(): List<ArticleRef> = jsonClient.get(ArticleRef.articleRefPath).body()

suspend fun addComment(comment: Comment) {
    jsonClient.post(Comment.commentPath) {
        contentType(ContentType.Application.Json)
        setBody(comment)
    }
}
