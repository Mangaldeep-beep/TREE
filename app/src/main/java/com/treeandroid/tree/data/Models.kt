package com.treeandroid.tree.data

data class Audiobook(val id: String, val title: String, val narrator: String, val imageUrl: String)
data class Ebook(val id: String, val title: String, val author: String, val imageUrl: String)
data class Podcast(val id: String, val title: String, val category: String, val imageUrl: String)
data class ContentItem(val id: String, val title: String, val subtitle: String, val imageUrl: String)

fun Audiobook.toContentItem() = ContentItem(id, title, "Narrated by $narrator", imageUrl)
fun Ebook.toContentItem() = ContentItem(id, title, "By $author", imageUrl)
fun Podcast.toContentItem() = ContentItem(id, title, category, imageUrl) 