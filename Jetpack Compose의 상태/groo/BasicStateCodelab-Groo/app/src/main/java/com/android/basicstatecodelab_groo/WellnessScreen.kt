package com.android.basicstatecodelab_groo

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    viewModel: WellnessViewModel = viewModel()
) {
    Column(modifier) {
        StatefulCounter()

        WellnessTaskList(
            list = viewModel.tasks,
            onCheckedTask = { task, newChecked -> viewModel.changeTaskStated(task, newChecked) },
            onCloseTask = { task -> viewModel.remove(task) }
        )
    }
}
