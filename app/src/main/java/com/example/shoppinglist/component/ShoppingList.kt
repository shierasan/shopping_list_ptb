package com.example.shoppinglist.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ShoppingList(items: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it }) { item ->
            AnimatedShoppingListItem(item)
        }
    }
}

@Composable
private fun AnimatedShoppingListItem(item: String) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(item) {
        delay(100)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) + expandVertically(animationSpec = tween(500))
    ) {
        ShoppingListItem(item)
    }
}

@Composable
private fun ShoppingListItem(item: String) {
    var isSelected by remember { mutableStateOf(false) }

    // Animasi warna latar card
    val cardColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(300),
        label = "cardColor"
    )

    // Animasi elevasi
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 2.dp,
        animationSpec = tween(200),
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isSelected = !isSelected },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Lingkaran huruf inisial
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(2.dp, shape = CircleShape),
                shape = CircleShape,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = item.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // Nama item
            Text(
                text = item,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
            )

            // Icon kanan (+ atau ✔)
            Surface(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (isSelected) "Selected" else "Add to cart",
                    tint = if (isSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}
