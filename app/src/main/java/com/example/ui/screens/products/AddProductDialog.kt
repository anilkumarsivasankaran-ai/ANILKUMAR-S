package com.example.ui.screens.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ProductEntity

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onSaveProduct: (ProductEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("$49.00") }
    var features by remember { mutableStateOf("") }
    var benefits by remember { mutableStateOf("") }
    var targetLocation by remember { mutableStateOf("United States, Global") }
    var targetAgeGroup by remember { mutableStateOf("20-45") }
    var targetGender by remember { mutableStateOf("All Genders") }
    var targetCustomerType by remember { mutableStateOf("") }
    var painPoints by remember { mutableStateOf("") }
    var usp by remember { mutableStateOf("") }
    var competitors by remember { mutableStateOf("") }
    var brandName by remember { mutableStateOf("") }
    var brandTone by remember { mutableStateOf("Confident, modern, value-driven") }
    var websiteUrl by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add New Product",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Text(
                            text = "Enter details to generate AI Marketing Strategy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Core Fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; showError = false },
                    label = { Text("Product Name *") },
                    placeholder = { Text("e.g., Lumina Smart Desk Lamp") },
                    isError = showError && name.isBlank(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_product_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category *") },
                        placeholder = { Text("Smart Home / Tech") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_product_category")
                    )
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(0.8f)
                            .testTag("input_product_price")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Product Description") },
                    placeholder = { Text("Comprehensive overview of how the product works and who it is for...") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_product_description")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = usp,
                    onValueChange = { usp = it },
                    label = { Text("Unique Selling Proposition (USP)") },
                    placeholder = { Text("What makes it 10x better than existing alternatives?") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = features,
                    onValueChange = { features = it },
                    label = { Text("Key Features") },
                    placeholder = { Text("Wireless charging pad, circadian rhythm sync, aircraft aluminum...") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = benefits,
                    onValueChange = { benefits = it },
                    label = { Text("Key Benefits to Customer") },
                    placeholder = { Text("Reduces eye strain, increases focus, modern aesthetic...") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = painPoints,
                    onValueChange = { painPoints = it },
                    label = { Text("Customer Problems / Pain Points") },
                    placeholder = { Text("Harsh flickering lights, cluttered desks, afternoon fatigue...") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = targetCustomerType,
                    onValueChange = { targetCustomerType = it },
                    label = { Text("Target Customer Persona") },
                    placeholder = { Text("Remote developers, architects, university students...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = targetAgeGroup,
                        onValueChange = { targetAgeGroup = it },
                        label = { Text("Target Age") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = targetGender,
                        onValueChange = { targetGender = it },
                        label = { Text("Gender") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = targetLocation,
                    onValueChange = { targetLocation = it },
                    label = { Text("Target Region / Location") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = brandName,
                        onValueChange = { brandName = it },
                        label = { Text("Brand Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = brandTone,
                        onValueChange = { brandTone = it },
                        label = { Text("Brand Tone") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = competitors,
                    onValueChange = { competitors = it },
                    label = { Text("Main Competitors") },
                    placeholder = { Text("BenQ, Dyson, Xiaomi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = websiteUrl,
                    onValueChange = { websiteUrl = it },
                    label = { Text("Website URL (Optional)") },
                    placeholder = { Text("https://mybrand.com") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                showError = true
                            } else {
                                onSaveProduct(
                                    ProductEntity(
                                        name = name.trim(),
                                        category = category.ifBlank { "General Merchandise" },
                                        description = description.ifBlank { "High quality $name designed for everyday performance." },
                                        price = price.ifBlank { "$39.99" },
                                        features = features,
                                        benefits = benefits,
                                        targetLocation = targetLocation,
                                        targetAgeGroup = targetAgeGroup,
                                        targetGender = targetGender,
                                        targetCustomerType = targetCustomerType.ifBlank { "Consumers seeking quality $category" },
                                        painPoints = painPoints,
                                        usp = usp.ifBlank { "Next-generation $name delivering superior value." },
                                        competitors = competitors,
                                        brandName = brandName.ifBlank { "Brand Co." },
                                        brandTone = brandTone,
                                        websiteUrl = websiteUrl,
                                        imageUrl = imageUrl
                                    )
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("submit_product_button")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save & Generate Strategy")
                    }
                }
            }
        }
    }
}
