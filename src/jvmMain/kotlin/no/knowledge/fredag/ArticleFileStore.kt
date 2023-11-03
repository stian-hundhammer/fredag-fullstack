package no.knowledge.fredag

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.slf4j.LoggerFactory
import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

class ArticleFileStore(
    storeLocation: String,
    val json: Json
) {

    val articlePath: Path
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        // create store
        articlePath = FileSystems.getDefault().getPath(storeLocation)
        articlePath.toFile().mkdir()
    }

    fun saveArticle(article: Article) {
        val articlePath = articlePath.resolve(article.id.toString())
        articlePath.takeIf { !it.exists() }?.toFile()?.mkdir()

        articlePath.resolve("article.json")
            .writeText(json.encodeToString(article))
    }

    fun loadAllArticles(): List<Article> =
        articlePath.toFile().walk()
            .filter { file -> file.name.equals("article.json") }
            .map { file ->
                json.decodeFromStream<Article>(
                    file.inputStream()
                )
            }.toList()
}
