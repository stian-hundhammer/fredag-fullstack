package no.knowledge.fredag

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path

/**
 * For now, the backend, free from ktor, routing etc
 */
class FredagService(
    val legacyDataLocation: String?,
    val articleStoreLocation: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    lateinit var articleList: List<Article>
    lateinit var articleRefList: List<ArticleRef>

    val json = Json {
        this.ignoreUnknownKeys = true
    }

    val articleFileStore = ArticleFileStore(
        articleStoreLocation,
        json,
    )

    fun loadLegacyData() {
        if (legacyDataLocation != null) {
            logger.info("abount to load data from $legacyDataLocation")

            articleList = json.decodeFromStream<List<Article>>(File(legacyDataLocation).inputStream())
                .map { legacyArticle ->
                    legacyArticle.copy(
                        comments = legacyArticle.comments.map { it.copy(articleId = legacyArticle.id) }
                    )
                }
        } else {
            // for now...
            articleList = listOf(
                Article(
                    id = 1,
                    header = "dummy",
                    body = "dummy",
                    comments = emptyList()
                )
            )

            logger.info("no legacy data loaded. add dummy article")
        }

        val map = articleFileStore.loadAllArticles()
            .associate  { it.id to it }
            .toMutableMap()

        articleList = articleList.map {
            val newer = map.remove(it.id)
            if (newer != null) {
                newer
            } else it
        }

        articleList = articleList + map.values

        logger.info("loaded ${articleList.size} articles")

        articleRefList = articleList.reversed().map {
            ArticleRef(
                id = it.id.toString(),
                header = it.header,
                commentSize = it.comments.size
            )
        }

    }

    fun currentArticle() : Article = articleList.last()

    fun addComment(comment: Comment) {
        // ugly hack....
        articleList = articleList.map {
            if (it.id == comment.articleId) {
                it.copy(
                    comments =  it.comments + comment
                ).also {
                    runBlocking {
                        launch { articleFileStore.saveArticle(it) }
                    }
                }
            } else it
        }
    }
}