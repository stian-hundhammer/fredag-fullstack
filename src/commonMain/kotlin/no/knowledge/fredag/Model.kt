package no.knowledge.fredag

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: String,
    val header: String,
    val body: String,
    val comments: List<Comment>
) {

    companion object {
        const val path = "/articleList"
    }
}
@Serializable
data class Comment(
    val id: String,
    val userName: String?,
    val text: String?,
)