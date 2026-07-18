package com.deltacode.deltacode.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.deltacode.deltacode.data.model.AIProvider
import com.deltacode.deltacode.data.model.Project
import com.deltacode.deltacode.ui.theme.*

@Composable
fun IDEButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    enabled: Boolean = true
) {
    val bgColor = if (isPrimary) AccentPrimary else Color.Transparent
    val borderStroke = if (isPrimary) null else BorderStroke(1.dp, BorderDefault)
    val contentColor = if (isPrimary) Color.White else TextPrimary

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp) // Meets touch target minimum
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = contentColor,
            disabledContainerColor = if (isPrimary) AccentPrimary.copy(alpha = 0.5f) else Color.Transparent,
            disabledContentColor = TextDisabled
        ),
        shape = RoundedCornerShape(6.dp),
        border = borderStroke,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun IDETextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(BgElevated, RoundedCornerShape(6.dp))
                .border(1.dp, BorderDefault, RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(CursorColor),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CreateProjectDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var projectName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(420.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = BgElevated),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BorderDefault)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "New Project",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                IDETextField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    placeholder = "e.g. my_awesome_app",
                    label = "Project Name"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IDEButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        isPrimary = false,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IDEButton(
                        text = "Create",
                        onClick = { onSubmit(projectName) },
                        isPrimary = true,
                        enabled = projectName.isNotBlank()
                    )
                }
            }
        }
    }
}

@Composable
fun ImportFolderDialog(
    defaultPath: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var folderPath by remember { mutableStateOf(defaultPath) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(460.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = BgElevated),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BorderDefault)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Import Local Folder",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                IDETextField(
                    value = folderPath,
                    onValueChange = { folderPath = it },
                    placeholder = "/absolute/path/to/folder",
                    label = "Directory Path"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IDEButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        isPrimary = false,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IDEButton(
                        text = "Import",
                        onClick = { onSubmit(folderPath) },
                        isPrimary = true,
                        enabled = folderPath.isNotBlank()
                    )
                }
            }
        }
    }
}

@Composable
fun RenameProjectDialog(
    project: Project,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var newName by remember { mutableStateOf(project.name) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(420.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = BgElevated),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BorderDefault)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Rename Project",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                IDETextField(
                    value = newName,
                    onValueChange = { newName = it },
                    placeholder = "Type new name...",
                    label = "New Name"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IDEButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        isPrimary = false,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IDEButton(
                        text = "Save",
                        onClick = { onSubmit(newName) },
                        isPrimary = true,
                        enabled = newName.isNotBlank() && newName != project.name
                    )
                }
            }
        }
    }
}

@Composable
fun AIConfigDialog(
    provider: AIProvider,
    onDismiss: () -> Unit,
    onSubmit: (AIProvider) -> Unit
) {
    var apiKey by remember { mutableStateOf(provider.apiKey) }
    var defaultModel by remember { mutableStateOf(provider.defaultModel) }
    var isEnabled by remember { mutableStateOf(provider.isEnabled) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(440.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = BgElevated),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BorderDefault)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Configure ${provider.name}",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                IDETextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    placeholder = "Enter API Key",
                    label = "API Access Link / Secret Hex Key"
                )

                Spacer(modifier = Modifier.height(16.dp))

                IDETextField(
                    value = defaultModel,
                    onValueChange = { defaultModel = it },
                    placeholder = "e.g. gemini-1.5-pro",
                    label = "Default Engine Model"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isEnabled = !isEnabled }
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = isEnabled,
                        onCheckedChange = { isEnabled = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AccentPrimary,
                            uncheckedColor = TextSecondary
                        )
                    )
                    Text(
                        text = "Set as default AI capability provider",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IDEButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        isPrimary = false,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IDEButton(
                        text = "Save Settings",
                        onClick = {
                            onSubmit(
                                provider.copy(
                                    apiKey = apiKey,
                                    defaultModel = defaultModel,
                                    isEnabled = isEnabled
                                )
                            )
                        },
                        isPrimary = true
                    )
                }
            }
        }
    }
}
