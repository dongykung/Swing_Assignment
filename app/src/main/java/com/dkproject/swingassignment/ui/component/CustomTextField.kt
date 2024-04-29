package com.dkproject.swingassignment.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dkproject.swingassignment.R

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    leadingIcon: ImageVector,
    isSingleLine:Boolean=true,
    keyboardType: KeyboardType=KeyboardType.Text,
    imeAction: ImeAction= ImeAction.Done,
    onAction : KeyboardActions=KeyboardActions.Default,
    ontextChange: (String) -> Unit
) {
    //텍스트 필드
    OutlinedTextField(
        modifier=modifier,
        value = text,
        onValueChange = ontextChange,
        placeholder = { Text(text = placeholder)},
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null)},
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )
}