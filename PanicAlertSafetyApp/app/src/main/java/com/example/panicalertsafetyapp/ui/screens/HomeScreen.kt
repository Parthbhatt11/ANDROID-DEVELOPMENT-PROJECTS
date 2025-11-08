package com.example.panicalertsafetyapp.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.* // 💡 Added necessary imports for state and coroutines
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.panicalertsafetyapp.ui.viewmodel.PanicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch // 💡 Added necessary import for coroutines

@Composable
fun HomeScreen(viewModel: PanicViewModel) {

    // 1. State for visual feedback and coroutine scope for timing
    var isAlertActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 2. Animate the size change (button pulses when active)
    val buttonSize by animateDpAsState(
        targetValue = if (isAlertActive) 220.dp else 200.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ButtonSizeAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // 3. Status Message
            Text(
                // Change text and color based on active state
                text = if (isAlertActive)
                    "🚨 ALERT SENT! Locating and sending messages..."
                else
                    "Tap to send instant alert to all contacts.",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isAlertActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(64.dp))

            // 4. Animated Button
            FloatingActionButton(
                onClick = {
                    if (!isAlertActive) {
                        isAlertActive = true // Activate visual feedback
                        viewModel.onPanicTriggered() // Execute the core logic

                        // Reset the visual status after a short delay (e.g., 5 seconds)
                        scope.launch {
                            delay(5000)
                            isAlertActive = false
                        }
                    }
                },
                // Change color from Red (inactive) to Blue (active/confirmation)
                containerColor = if (isAlertActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.error,
                // Apply the animated size
                modifier = Modifier.size(buttonSize)
            ) {
                Text(
                    text = if (isAlertActive) "SENT" else "PANIC",
                    color = Color.White,
                    fontSize = if (isAlertActive) 38.sp else 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "SMS and WhatsApp alert will be triggered.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}