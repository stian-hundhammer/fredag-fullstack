package no.knowledge.fredag

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.slf4j.LoggerFactory
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * For now, the backend, free from ktor, routing etc
 */
class FredagService(
    val legacyDataLocation: String?,
    val articleStoreLocation: String
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    lateinit var articleList: List<Article>
    lateinit var articleRefList: List<ArticleRef>

    val json = Json {
        this.ignoreUnknownKeys = true
    }

    val articleFileStore = ArticleFileStore(
        articleStoreLocation,
        json
    )

    fun loadLegacyData() {
        val legacyArticleList = if (legacyDataLocation != null) {
            logger.info("abount to load data from $legacyDataLocation")

            json.decodeFromStream<List<Article>>(File(legacyDataLocation).inputStream())
                .map { legacyArticle ->
                    legacyArticle.copy(
                        comments = legacyArticle.comments.map { it.copy(articleId = legacyArticle.id) }
                    )
                }
        } else {
            // for now...
            listOf(
                Article(
                    id = 1,
                    header = "dummy",
                    body = "dummy",
                    comments = emptyList()
                )
            ).also {
                logger.info("no legacy data loaded. add dummy article")
            }
        }

        val map = articleFileStore.loadAllArticles()
            .associate { it.id to it }
            .toMutableMap()

        // replace those from legacy store by those stored
        // in files
        // Add those left in the map
        articleList = legacyArticleList.map {
            val newer = map.remove(it.id) ?: it
            if (newer != null) {
                newer
            } else {
                it
            }
        } + map.values.sortedBy { it.id }

        // bug. player should not be part of article source
        // When saved, the player is included
        // articleList = articleList.map { it.insertPlayer() }

        logger.info("loaded ${articleList.size} articles")

        articleRefList = articleList.reversed().map {
            ArticleRef(
                id = it.id.toString(),
                header = it.header,
                commentSize = it.comments.size
            )
        }
    }

    fun currentArticle(): Article = articleList.last()

    fun addComment(comment: Comment) {
        // ugly hack....
        articleList = articleList.map {
            if (it.id == comment.articleId) {
                it.copy(
                    comments = it.comments + comment
                ).also {
                    runBlocking {
                        launch { articleFileStore.saveArticle(it) }
                    }
                }
            } else {
                it
            }
        }
    }

    private fun Article.insertPlayer(): Article =
        copy(
            body = body.lines()
                .joinToString(
                    separator = "\n"
                ) { line ->
                    val matcher: Matcher = linkPattern.matcher(line)

                    if (matcher.find()) {
                        replaceLine(line, matcher).also {
                            logger.debug("articleId: ${this.id} -replace line with $it")
                        }
                    } else {
                        line
                    }
                }
        )

    private fun replaceLine(line: String, matcher: Matcher): String =
        line + String.format(audioPlayerReplacement, matcher.group(0))

    private val linkPattern = Pattern.compile("/mp3/\\w+.mp3")
    private val audioPlayerReplacement = "<br/><audio controls><source src=\"%s\" type=\"audio/mpeg\"></audio><br/>"
}
