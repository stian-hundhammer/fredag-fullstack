package no.knowledge.fredag

import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import org.litote.kmongo.json
import org.slf4j.LoggerFactory
import java.io.File

/**
 * For now, the backend, free from ktor, routing etc
 */
class FredagService(
    val legacyDataLocation: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    lateinit var articleList: List<Article>

    fun loadLegacyData() {
        logger.info("abount to load data from $legacyDataLocation")
        val j = Json {
            this.ignoreUnknownKeys = true
        }
        articleList = j.decodeFromStream<List<Article>>(File(legacyDataLocation).inputStream())


        /*
        articleList = Json.decodeFromStream<List<Article>>(
            deserializer = ,
            stream = File(legacyDataLocation)
        .inputStream())
        */
        logger.info("loaded ${articleList.size} articles")
    }

    fun currentArticle() : Article = articleList.last()


}