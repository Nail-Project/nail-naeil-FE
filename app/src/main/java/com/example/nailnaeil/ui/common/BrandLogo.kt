package com.example.nailnaeil.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.R

@Composable
fun BrandLogo(modifier: Modifier = Modifier, size: Dp = 96.dp) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "네일내일 로고",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size)
    )
}
