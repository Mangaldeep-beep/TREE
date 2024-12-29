package com.treeandroid.tree.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treeandroid.tree.data.*
import kotlinx.coroutines.launch

class FeatureTabsViewModel : ViewModel() {
    var audiobooks by mutableStateOf<List<Audiobook>>(emptyList())
    var ebooks by mutableStateOf<List<Ebook>>(emptyList())
    var podcasts by mutableStateOf<List<Podcast>>(emptyList())
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        fetchContent()
    }

    private fun fetchContent() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                // Simulate data fetching
                audiobooks = listOf(
                    Audiobook("1", "The Great Gatsby", "Jake Gyllenhaal", "https://picsum.photos/300"),
                    Audiobook("2", "1984", "Simon Prebble", "https://picsum.photos/301")
                )
                ebooks = listOf(
                    Ebook("1", "The Hobbit", "J.R.R. Tolkien", "https://picsum.photos/302"),
                    Ebook("2", "Dune", "Frank Herbert", "https://picsum.photos/303")
                )
                podcasts = listOf(
                    Podcast("1", "Tech Talk", "Technology", "https://picsum.photos/304"),
                    Podcast("2", "True Crime", "Crime", "https://picsum.photos/305")
                )
            } catch (e: Exception) {
                error = "Failed to load content"
            } finally {
                loading = false
            }
        }
    }
} 