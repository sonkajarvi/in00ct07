package com.sonkajarvi.calculator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonkajarvi.calculator.EntryViewModel
import com.sonkajarvi.calculator.R
import com.sonkajarvi.calculator.data.Entry

@Composable
fun HistoryScreen(
    viewModel: EntryViewModel
) {
    val entries by viewModel.getAll().observeAsState()

    Column(
        modifier= Modifier
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        entries?.let {
            LazyColumn(
                content = {
                    itemsIndexed(it) { _: Int, e: Entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                e.expr,
                                fontSize = 24.sp,
                                maxLines = 1,
                                softWrap = false,
                                color = Color.Gray
                            )
                            IconButton(
                                onClick = {
                                    viewModel.delete(e.id)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = stringResource(R.string.remove_entry)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
