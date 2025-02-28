package com.sonkajarvi.calculator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonkajarvi.calculator.R
import com.sonkajarvi.calculator.expression.ExpressionHandler

data class B(
    val text: String,
    val func: ((() -> Unit)?) -> Unit,
    val color: Int = R.color.gray
)

val ORANGE = Color.hsv(36f, 1f, 1f)

val buttons = arrayOf(
    arrayOf(
        B("C", ExpressionHandler::clear, R.color.gray_light),
        B("±", ExpressionHandler::negate, color = R.color.gray_light),
        B("%", ExpressionHandler::percentage, R.color.gray_light),
        B("÷", ExpressionHandler::div, R.color.yellow),
    ),
    arrayOf(
        B("7", ExpressionHandler::insert7),
        B("8", ExpressionHandler::insert8),
        B("9", ExpressionHandler::insert9),
        B("×", ExpressionHandler::mul, R.color.yellow),
    ),
    arrayOf(
        B("4", ExpressionHandler::insert4),
        B("5", ExpressionHandler::insert5),
        B("6", ExpressionHandler::insert6),
        B("-", ExpressionHandler::sub, R.color.yellow),
    ),
    arrayOf(
        B("1", ExpressionHandler::insert1),
        B("2", ExpressionHandler::insert2),
        B("3", ExpressionHandler::insert3),
        B("+", ExpressionHandler::add, R.color.yellow),
    ),
    arrayOf(
        B(",", ExpressionHandler::comma),
        B("0", ExpressionHandler::insert0),
        B("⌫", ExpressionHandler::remove),
        B("=", ExpressionHandler::eval, color = R.color.yellow),
    ),
)

@Composable
fun Keypad(
    modifier: Modifier = Modifier,
    extra: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        for (row in buttons) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (button in row) {
                    Button(
                        onClick = { button.func.invoke(extra) },
                        modifier = Modifier
                            .width(90.dp)
                            .height(90.dp)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(button.color),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            button.text,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
