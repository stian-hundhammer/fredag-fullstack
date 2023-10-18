package no.knowledge.fredag

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Long,
    val header: String,
    val body: String,
    val comments: List<Comment>
) {

    companion object {
        const val articleListPath = "/articleList"
        const val articlePath = "/article"
    }
}
@Serializable
data class Comment(
    val id: Long,
    val userName: String?,
    val text: String?,
    val articleId: Long? = null,
    val commentId: String? = null,
) {

    companion object {
        const val commentPath = "/comment"
    }
}

@Serializable 
data class ArticleRef(
    val id: String,
    val header: String,
    val commentSize: Int,
) {
    companion object {
        const val articleRefPath = "/articleRef"
    }
}
/*
class EditorialContentReference(
    type: EditorialContentType,
    reference: String,
)

enum class EditorialContentType {
    ARTICLE,
    COMMENT,
    PICTURE,
    BAND,
    SONG,
}

 */