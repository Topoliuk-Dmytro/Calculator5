package com.example.calculator4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator4.ui.theme.Calculator4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculator4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Калькулятор надійності ЕПС") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Порівняння надійності") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Збитки від перерв") }
            )
        }
        
        when (selectedTab) {
            0 -> ReliabilityComparisonScreen()
            1 -> LossesCalculationScreen()
        }
    }
}

@Composable
fun ReliabilityComparisonScreen() {
    var omega1 by remember { mutableStateOf("0.01") } // частота відмов одноколової
    var mu1 by remember { mutableStateOf("10") } // частота ремонтів одноколової
    var tB1 by remember { mutableStateOf("0.045") } // час відновлення одноколової (рік)
    
    var omega2 by remember { mutableStateOf("0.01") } // частота відмов двоколової
    var mu2 by remember { mutableStateOf("10") } // частота ремонтів двоколової
    var tB2 by remember { mutableStateOf("0.045") } // час відновлення двоколової (рік)
    
    var result1 by remember { mutableStateOf<Double?>(null) }
    var result2 by remember { mutableStateOf<Double?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Порівняння надійності одноколової та двоколової систем",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Одноколова система",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = omega1,
                    onValueChange = { omega1 = it },
                    label = { Text("Частота відмов ω₁ (рік⁻¹)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = mu1,
                    onValueChange = { mu1 = it },
                    label = { Text("Частота ремонтів μ₁ (рік⁻¹)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = tB1,
                    onValueChange = { tB1 = it },
                    label = { Text("Час відновлення tB₁ (рік)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Двоколова система",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = omega2,
                    onValueChange = { omega2 = it },
                    label = { Text("Частота відмов ω₂ (рік⁻¹)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = mu2,
                    onValueChange = { mu2 = it },
                    label = { Text("Частота ремонтів μ₂ (рік⁻¹)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = tB2,
                    onValueChange = { tB2 = it },
                    label = { Text("Час відновлення tB₂ (рік)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
        
        Button(
            onClick = {
                try {
                    val w1 = omega1.toDouble()
                    val m1 = mu1.toDouble()
                    val tb1 = tB1.toDouble()
                    val w2 = omega2.toDouble()
                    val m2 = mu2.toDouble()
                    val tb2 = tB2.toDouble()
                    
                    // Розрахунок коефіцієнта готовності Kг = μ / (ω + μ)
                    result1 = m1 / (w1 + m1)
                    result2 = m2 / (w2 + m2)
                } catch (e: Exception) {
                    result1 = null
                    result2 = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Розрахувати")
        }
        
        if (result1 != null && result2 != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Результати",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text("Коефіцієнт готовності одноколової системи: ${String.format("%.6f", result1)}")
                    Text("Коефіцієнт готовності двоколової системи: ${String.format("%.6f", result2)}")
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val comparison = if (result2!! > result1!!) {
                        "Двоколова система надійніша"
                    } else if (result1!! > result2!!) {
                        "Одноколова система надійніша"
                    } else {
                        "Системи мають однакову надійність"
                    }
                    
                    Text(
                        text = comparison,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun LossesCalculationScreen() {
    var omega by remember { mutableStateOf("0.01") } // частота відмов
    var tB by remember { mutableStateOf("0.045") } // час відновлення (рік)
    var kP by remember { mutableStateOf("0.004") } // середній час планового простою
    var pM by remember { mutableStateOf("5120") } // максимальне навантаження (кВт)
    var tM by remember { mutableStateOf("6451") } // час використання максимального навантаження (год)
    var zPerA by remember { mutableStateOf("23.6") } // питомі збитки від аварійних вимкнень (грн/кВт·год)
    var zPerP by remember { mutableStateOf("17.6") } // питомі збитки від планових вимкнень (грн/кВт·год)
    
    var resultWnedA by remember { mutableStateOf<Double?>(null) }
    var resultWnedP by remember { mutableStateOf<Double?>(null) }
    var resultZPer by remember { mutableStateOf<Double?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Розрахунок збитків від перерв електропостачання",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Однотрансформаторна ГПП",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Параметри трансформатора",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = omega,
                    onValueChange = { omega = it },
                    label = { Text("Частота відмов ω (рік⁻¹)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = tB,
                    onValueChange = { tB = it },
                    label = { Text("Час відновлення tB (рік)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = kP,
                    onValueChange = { kP = it },
                    label = { Text("Час планового простою kп") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Параметри навантаження",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = pM,
                    onValueChange = { pM = it },
                    label = { Text("Максимальне навантаження PM (кВт)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = tM,
                    onValueChange = { tM = it },
                    label = { Text("Час використання максимуму TM (год)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Питомі збитки",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = zPerA,
                    onValueChange = { zPerA = it },
                    label = { Text("Збитки від аварійних вимкнень (грн/кВт·год)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = zPerP,
                    onValueChange = { zPerP = it },
                    label = { Text("Збитки від планових вимкнень (грн/кВт·год)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
        
        Button(
            onClick = {
                try {
                    val w = omega.toDouble()
                    val tb = tB.toDouble()
                    val kp = kP.toDouble()
                    val pm = pM.toDouble()
                    val tm = tM.toDouble()
                    val zpa = zPerA.toDouble()
                    val zpp = zPerP.toDouble()
                    
                    // M(Wнед.а) = ω·tB·PM·TM
                    resultWnedA = w * tb * pm * tm
                    
                    // M(Wнед.п) = kп·PM·TM
                    resultWnedP = kp * pm * tm
                    
                    // M(Зпер) = Зпер.а·M(Wнед.а) + Зпер.п·M(Wнед.п)
                    resultZPer = zpa * resultWnedA!! + zpp * resultWnedP!!
                } catch (e: Exception) {
                    resultWnedA = null
                    resultWnedP = null
                    resultZPer = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Розрахувати")
        }
        
        if (resultWnedA != null && resultWnedP != null && resultZPer != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Результати розрахунків",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "M(Wнед.а) = ${String.format("%.2f", resultWnedA)} кВт·год",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    
                    Text(
                        text = "M(Wнед.п) = ${String.format("%.2f", resultWnedP)} кВт·год",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = "M(Зпер) = ${String.format("%.2f", resultZPer)} грн.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}