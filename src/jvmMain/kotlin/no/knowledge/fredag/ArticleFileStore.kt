package no.knowledge.fredag

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.json.internal.writeJson
import org.slf4j.LoggerFactory
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path
import java.util.logging.Logger
import kotlin.io.path.exists
import kotlin.io.path.fileStore
import kotlin.io.path.writeText

class ArticleFileStore(
    storeLocation: String,
    val json: Json,
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
}