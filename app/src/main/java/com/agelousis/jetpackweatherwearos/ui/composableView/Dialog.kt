package com.agelousis.jetpackweatherwearos.ui.composableView

import android.app.AlertDialog
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.agelousis.jetpackweatherwearos.R
import com.agelousis.jetpackweatherwearos.ui.composableView.models.SimpleDialogDataModel
import com.agelousis.jetpackweatherwearos.ui.theme.Typography
import com.agelousis.jetpackweatherwearos.ui.theme.textViewAlertTitleFont

@Composable
fun SimpleDialog(
    show: Boolean,
    simpleDialogDataModel: SimpleDialogDataModel
) {
    if (show)
        AlertDialog(
            onDismissRequest = simpleDialogDataModel.dismissBlock ?: {},
            title = {
                Text(
                    text = simpleDialogDataModel.title,
                    style = textViewAlertTitleFont
                )
            },
            text = {
                Text(
                    text = simpleDialogDataModel.message,
                    style = Typography.body2
                )
            },
            confirmButton = {
                TextButton(
                    onClick = simpleDialogDataModel.positiveButtonBlock ?: {}
                ) {
                    Text(
                        text = simpleDialogDataModel.positiveButtonText ?: stringResource(id = R.string.key_ok_label),
                        style = Typography.body2
                    )
                }
            },
            dismissButton = {
                if (simpleDialogDataModel.negativeButtonText != null
                    && simpleDialogDataModel.negativeButtonBlock != null
                )
                    TextButton(
                        onClick = simpleDialogDataModel.negativeButtonBlock
                    ) {
                        Text(
                            text = simpleDialogDataModel.negativeButtonText,
                            style = Typography.body2
                        )
                    }
            },
            shape = RoundedCornerShape(
                size = 16.dp
            )
        )
}