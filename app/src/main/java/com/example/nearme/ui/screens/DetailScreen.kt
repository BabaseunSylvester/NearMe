package com.example.nearme.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nearme.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val openUrl = { url: String ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val dialNumber = { phone: String ->
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                ErrorMessage(
                    message = state.error!!,
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                state.place?.let { place ->
                    // Photo Gallery
                    if (state.photos.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.height(250.dp)
                        ) {
                            items(state.photos) { photo ->
                                val imageUrl = "${photo.prefix}original${photo.suffix}"
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(300.dp)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(24.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                                Text(
                                    "No photos available",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = place.name,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = (-0.5).sp
                                    )
                                )
                                Text(
                                    text = if (place.categories?.isNotEmpty() == true) 
                                        place.categories.joinToString { it.name ?: "" }
                                    else "Local Interest",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            Surface(
                                color = if (place.rating != null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (place.rating != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = place.rating?.toString() ?: "New",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (place.rating != null) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))

                        // Quick Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailActionButton(
                                icon = Icons.Default.Phone,
                                label = "Call",
                                onClick = { place.tel?.let { dialNumber(it) } },
                                enabled = !place.tel.isNullOrBlank(),
                                modifier = Modifier.weight(1f)
                            )
                            DetailActionButton(
                                icon = Icons.Default.Info,
                                label = "Website",
                                onClick = { place.website?.let { openUrl(it) } },
                                enabled = !place.website.isNullOrBlank(),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        DetailInfoRow(
                            icon = Icons.Default.LocationOn,
                            text = place.location?.formattedAddress ?: "No address available"
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "About this place",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (!place.description.isNullOrBlank()) place.description 
                                else "Experience the unique charm of this location. A wonderful spot in the heart of the city, offering a memorable atmosphere for all visitors.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Highlights",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val tags = if (place.tastes?.isNotEmpty() == true) place.tastes 
                                        else listOf("Authentic", "Scenic", "Local Favorite")
                            tags.forEach { taste ->
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(taste) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
fun DetailInfoRow(
    icon: ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.padding(10.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
