package com.treeandroid.tree.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.treeandroid.tree.data.ContentItem
import com.treeandroid.tree.viewmodel.FeatureTabsViewModel

@Composable
fun FeatureTabs(viewModel: FeatureTabsViewModel = viewModel()) {
    var activeTab by remember { mutableStateOf(0) }
    val tabs = listOf("Audiobook", "E-Book", "Podcast")

    Column(Modifier.fillMaxSize()) {
        // Tab Navigation
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            tabs.forEachIndexed { index, tab ->
                Text(
                    text = tab,
                    color = if (index == activeTab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = if (index == activeTab) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .clickable { activeTab = index }
                        .padding(8.dp)
                )
            }
        }

        // Content Area
        AnimatedVisibility(visible = viewModel.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (!viewModel.loading) {
            when (activeTab) {
                0 -> ContentGrid(viewModel.audiobooks.map { it.toContentItem() })
                1 -> ContentGrid(viewModel.ebooks.map { it.toContentItem() })
                2 -> ContentGrid(viewModel.podcasts.map { it.toContentItem() })
            }
        }

        viewModel.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun ContentGrid(items: List<ContentItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Image(
                        painter = rememberAsyncImagePainter(item.imageUrl),
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(Modifier.padding(8.dp)) {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.subtitle,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
} 