package com.example.coachmarktutorial.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.coachmarktutorial.ui.components.FeedItem
import com.example.coachmarktutorial.ui.theme.Dimens

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = Dimens.PaddingSmall)
    ) {
        items(10) { index ->
            FeedItem(index)
        }
    }
}