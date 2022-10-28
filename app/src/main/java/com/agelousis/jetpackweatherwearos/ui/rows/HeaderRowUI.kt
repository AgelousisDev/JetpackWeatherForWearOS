package com.agelousis.jetpackweatherwearos.ui.rows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.agelousis.jetpackweatherwearos.ui.models.HeaderModel
import com.agelousis.jetpackweatherwearos.ui.theme.Typography

@Composable
fun HeaderRowLayout(
    modifier: Modifier = Modifier,
    headerModel: HeaderModel
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                other = modifier
            )
    ) {
        val (lineConstrainedReference, headerLabelConstrainedReference) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(lineConstrainedReference) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.value(
                        dp = 4.dp
                    )
                    height = Dimension.value(
                        dp = 20.dp
                    )
                }
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(
                        size = 8.dp
                    )
                )
        )
        Text(
            text = headerModel.header,
            style = Typography.display2,
            modifier = Modifier
                .constrainAs(headerLabelConstrainedReference) {
                    start.linkTo(lineConstrainedReference.end, 8.dp)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Preview
@Composable
fun HeaderRowLayoutPreview() {
    HeaderRowLayout(
        headerModel = HeaderModel(
            header = "Hello World"
        )
    )
}