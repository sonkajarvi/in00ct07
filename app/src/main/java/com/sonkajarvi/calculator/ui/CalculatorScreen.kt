package com.sonkajarvi.calculator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonkajarvi.calculator.EntryViewModel
import com.sonkajarvi.calculator.data.Entry
import com.sonkajarvi.calculator.expression.ExpressionHandler

@Composable
fun CalculatorScreen(
    viewModel: EntryViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            ExpressionHandler.latest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            fontSize = 40.sp,
            textAlign = TextAlign.Right,
            overflow = TextOverflow.Visible,
            maxLines = 1,
            softWrap = false,
            color = Color.Gray
        )
        Text(
            ExpressionHandler.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Right,
            overflow = TextOverflow.Visible,
            maxLines = 1,
            softWrap = false
        )
        Keypad(
            modifier = Modifier.padding(bottom = 32.dp),
            extra = {
                viewModel.insert(Entry(expr = ExpressionHandler.latest))
            }
        )
    }
}
