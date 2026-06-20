package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBackgroundColor: Color,
    val iconColor: Color,
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "AI-Powered Analysis",
        description = "Advanced artificial intelligence analyzes dental radiographs to detect root resorption in primary teeth with high accuracy.",
        icon = Icons.Default.Psychology,
        iconBackgroundColor = Color(0xFFE0F7F9),
        iconColor = Color(0xFF0097A7)
    ),
    OnboardingPage(
        title = "Clinical Decision Support",
        description = "Get evidence-based recommendations and treatment guidance for managing root resorption cases effectively.",
        icon = Icons.Default.Shield,
        iconBackgroundColor = Color(0xFFE8EAF6),
        iconColor = Color(0xFF3F51B5)
    ),
    OnboardingPage(
        title = "Streamlined Workflow",
        description = "Simplify patient management, case documentation, and report generation in one comprehensive platform.",
        icon = Icons.Default.Bolt,
        iconBackgroundColor = Color(0xFFE0F2F1),
        iconColor = Color(0xFF009688)
    )
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState { onboardingPages.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(4f)
        ) { pageIndex ->
            OnboardingPagerItem(onboardingPages[pageIndex])
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pager Indicator
        Row(
            modifier = Modifier.height(50.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(onboardingPages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFF0097A7) else Color(0xFFD1D5DB)
                val width = if (pagerState.currentPage == iteration) 32.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .width(width)
                        .height(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation Button
        Button(
            onClick = {
                if (pagerState.currentPage < (onboardingPages.size - 1)) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onFinished()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0097A7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (pagerState.currentPage == (onboardingPages.size - 1)) "Get Started" else "Next",
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Skip Button
        if (pagerState.currentPage < (onboardingPages.size - 1)) {
            TextButton(onClick = onFinished) {
                Text(
                    text = "Skip",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        } else {
            // Placeholder to keep spacing
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun OnboardingPagerItem(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(page.iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                tint = page.iconColor
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = page.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    OnboardingScreen(onFinished = {})
}
