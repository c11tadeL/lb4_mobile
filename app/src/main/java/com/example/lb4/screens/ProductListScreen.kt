package com.example.lb4.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lb4.components.*
import com.example.lb4.data.model.*
import com.example.lb4.utils.getShopApplication
import com.example.lb4.viewmodel.ProductViewModel
import com.example.lb4.viewmodel.ProductViewModelFactory

@Composable
fun ProductListScreen() {
    val context = LocalContext.current
    val repository = context.getShopApplication().repository

    val viewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(repository)
    )

    val listItems by viewModel.listItems.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showAddPromotionDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var editingPromotion by remember { mutableStateOf<PromotionEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = { showAddPromotionDialog = true },
                    containerColor = MaterialTheme.colorScheme.tertiary
                ) {
                    Icon(Icons.Default.Add, "Додати акцію")
                }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = { showAddProductDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Add, "Додати продукт")
                }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = { showAddCategoryDialog = true }
                ) {
                    Icon(Icons.Default.Add, "Додати категорію")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "🛒 Магазин",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            if (listItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Немає даних.\nДодайте категорію або акцію кнопкою внизу справа!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items = listItems, key = { it.id }) { listItem ->
                        when (listItem) {
                            is ListItem.PromotionItem -> PromotionCard(
                                promotion = listItem.promotion,
                                onDelete = { viewModel.deletePromotion(it) },
                                onEdit = { editingPromotion = it }
                            )
                            is ListItem.CategoryHeader -> CategoryItemWithActions(
                                categoryWithProducts = listItem.categoryWithProducts,
                                onDeleteCategory = { viewModel.deleteCategory(it) },
                                onEditCategory = { editingCategory = it },
                                onDeleteProduct = { viewModel.deleteProduct(it) },
                                onEditProduct = { editingProduct = it }
                            )
                        }
                    }
                }
            }
        }
    }

    // Діалоги
    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name, icon ->
                viewModel.addCategory(name, icon)
                showAddCategoryDialog = false
            }
        )
    }

    if (showAddProductDialog) {
        AddProductDialog(
            categories = allCategories,
            onDismiss = { showAddProductDialog = false },
            onConfirm = { name, price, emoji, categoryId ->
                viewModel.addProduct(name, price, emoji, categoryId)
                showAddProductDialog = false
            }
        )
    }

    if (showAddPromotionDialog) {
        AddPromotionDialog(
            onDismiss = { showAddPromotionDialog = false },
            onConfirm = { title, description, discount, emoji ->
                viewModel.addPromotion(title, description, discount, emoji)
                showAddPromotionDialog = false
            }
        )
    }

    editingCategory?.let { category ->
        EditCategoryDialog(
            category = category,
            onDismiss = { editingCategory = null },
            onConfirm = { updatedCategory ->
                viewModel.updateCategory(updatedCategory)
                editingCategory = null
            }
        )
    }

    editingProduct?.let { product ->
        EditProductDialog(
            product = product,
            categories = allCategories,
            onDismiss = { editingProduct = null },
            onConfirm = { updatedProduct ->
                viewModel.updateProduct(updatedProduct)
                editingProduct = null
            }
        )
    }

    editingPromotion?.let { promotion ->
        EditPromotionDialog(
            promotion = promotion,
            onDismiss = { editingPromotion = null },
            onConfirm = { updatedPromotion ->
                viewModel.updatePromotion(updatedPromotion)
                editingPromotion = null
            }
        )
    }
}