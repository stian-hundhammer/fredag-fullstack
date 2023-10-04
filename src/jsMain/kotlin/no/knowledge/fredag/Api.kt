package no.knowledge.fredag

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

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
    val url = if (id == null) Article.articlePath
        else Article.articlePath + "/$id"

    return jsonClient.get(url).body()
}

suspend fun getArticleRefList() :List<ArticleRef> = jsonClient.get(ArticleRef.articleRefPath).body()