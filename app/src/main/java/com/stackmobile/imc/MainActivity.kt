package com.stackmobile.imc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMCCalculatorApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCCalculatorApp() {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf<Float?>(null) }
    var message by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.White) }
    var isMale by remember { mutableStateOf(true) } // Estado para alternar gênero
    var imageResource by remember { mutableStateOf(R.drawable.bmi) } // Estado para alterar imagem

    // IMC Calculation
    LaunchedEffect(weight, height, isMale) {
        if (weight.isNotEmpty() && height.isNotEmpty()) {
            try {
                val heightInMeters = height.toFloat() / 100
                val weightInKg = weight.toFloat()

                // Verifica se a altura e o peso são válidos
                if (heightInMeters <= 0 || weightInKg <= 0) {
                    message = "Valores inválidos"
                    color = Color.Gray // Cor para indicar erro
                    imageResource = R.drawable.bmi // Substitua com uma imagem de erro apropriada
                    return@LaunchedEffect
                }

                imc = weightInKg / (heightInMeters * heightInMeters)

                // Seleciona imagem e mensagem baseada no IMC e no gênero
                when {
                    imc!! < 18.5 -> {
                        message = if (isMale) "Abaixo do peso\n IMC: <18,5" else "Abaixo do peso\n IMC: <18,5"
                        color = Color(0xFF9C27B0) // Purple
                        imageResource = if (isMale) R.drawable.fotofem5 else R.drawable.fotomasc4
                    }
                    imc!! in 18.5..24.9 -> {
                        message = if (isMale) "Peso normal\n IMC: 18,5 - 24,9" else "Peso normal\n IMC: 18,5 - 24,9"
                        color = Color(0xFF4CAF50) // Green
                        imageResource = if (isMale) R.drawable.fotofem4 else R.drawable.fotomasc3
                    }
                    imc!! in 25.0..29.9 -> {
                        message = if (isMale) "Sobrepeso\n IMC: 25 - 29,9" else "Sobrepeso\n IMC: 25 - 29,9"
                        color = Color(0xFFFFEB3B) // Yellow
                        imageResource = if (isMale) R.drawable.fotofem3 else R.drawable.fotomasc2
                    }
                    imc!! in 30.0..34.9 -> {
                        message = if (isMale) "Obesidade Grau I\n IMC: 30 - 34,9" else "Obesidade Grau I\n IMC: 30 - 34,9"
                        color = Color(0xFFFFEB3B) // Yellow
                        imageResource = if (isMale) R.drawable.fotofem2 else R.drawable.fotomasc1
                    }
                    imc!! in 35.0..39.9 -> {
                        message = if (isMale) "Obesidade Grau II\n IMC: 35 - 39,9" else "Obesidade Grau II\n IMC: 35 - 39,9"
                        color = Color(0xFFF44336) // Red
                        imageResource = if (isMale) R.drawable.fotofem1 else R.drawable.fotomasc1
                    }
                    else -> {
                        message = if (isMale) "Obesidade Grau III ou Mórbida \n IMC: >40" else "Obesidade Grau III ou Mórbida \n IMC: >40"
                        color = Color(0xFFF44336) // Red
                        imageResource = if (isMale) R.drawable.fotofem1 else R.drawable.fotomasc1
                    }
                }
            } catch (e: NumberFormatException) {
                message = "Formato inválido"
                color = Color.Gray // Cor para indicar erro
                imageResource = R.drawable.bmi // Substitua com uma imagem de erro apropriada
            }
        }
    }


    @Composable
    fun AnimatedText() {
        // Estado para controlar a alternância do texto
        var currentTextIndex by remember { mutableStateOf(0) }

        // Lista com os textos a serem exibidos
        val texts = listOf("IMC Calculadora", "BMI Calculator")

        // Alterna o índice do texto a cada 3 segundos
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000) // Espera 3 segundos
                currentTextIndex = (currentTextIndex + 1) % texts.size // Alterna entre 0 e 1
            }
        }

        // Texto animado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter // Alinha o conteúdo ao topo
        ) {
            Text(
                text = texts[currentTextIndex],
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // AnimatedText
        AnimatedText()

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Ícone Masculino
            IconButton(
                onClick = { isMale = true }, // Seleciona Masculino
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.femininoicon),
                    contentDescription = "Masculino",
                    tint = if (isMale) Color.Blue else Color.White // Azul se masculino for selecionado
                )
            }

            // Ícone Feminino
            IconButton(
                onClick = { isMale = false }, // Seleciona Feminino
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.masculinoicon),
                    contentDescription = "Feminino",
                    tint = if (!isMale) Color.Blue else Color.White // Azul se feminino for selecionado
                )
            }
        }

        // Exibição da imagem baseada no gênero e IMC
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Imagem baseada no IMC e gênero",
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = imc?.let { "IMC: %.2f".format(it) } ?: "IMC: 0.0",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = message,
            color = color,
            fontSize = 19.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Altura (cm)", color = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Fundo transparente para aplicar o gradiente
                focusedIndicatorColor = Color(0xFF03A9F4), // Indicador azul quando focado
                unfocusedIndicatorColor = Color(0xFFB0BEC5), // Indicador cinza quando desfocado
                cursorColor = Color(0xFF03A9F4), // Cor do cursor
                focusedLabelColor = Color(0xFF03A9F4), // Cor da label quando focada
                unfocusedLabelColor = Color.Gray // Cor da label quando desfocada
            ),
            textStyle = TextStyle(
                color = Color.Black, // Cor do texto
                fontSize = 14.sp // Tamanho da fonte menor
            ),
            shape = RoundedCornerShape(16.dp), // Bordas arredondadas menores
            modifier = Modifier
                .width(150.dp) // Largura fixa para diminuir o tamanho
                .align(Alignment.CenterHorizontally) // Centralizar horizontalmente
                .padding(bottom = 4.dp) // Espaçamento externo reduzido
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                    ),
                    shape = RoundedCornerShape(16.dp) // Usando o mesmo arredondamento
                )
                .padding(horizontal = 8.dp, vertical = 6.dp) // Espaçamento interno ajustado
        )



        // Weight Input
        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Fundo transparente para aplicar o gradiente
                focusedIndicatorColor = Color(0xFF03A9F4), // Indicador azul quando focado
                unfocusedIndicatorColor = Color(0xFFB0BEC5), // Indicador cinza quando desfocado
                cursorColor = Color(0xFF03A9F4), // Cor do cursor
                focusedLabelColor = Color(0xFF03A9F4), // Cor da label quando focada
                unfocusedLabelColor = Color.Gray // Cor da label quando desfocada
            ),
            textStyle = TextStyle(
                color = Color.Black, // Cor do texto
                fontSize = 14.sp // Tamanho da fonte menor
            ),
            shape = RoundedCornerShape(16.dp), // Bordas arredondadas menores
            modifier = Modifier
                .width(150.dp) // Largura fixa para diminuir o tamanho
                .align(Alignment.CenterHorizontally) // Centralizar horizontalmente
                .padding(bottom = 4.dp) // Espaçamento externo reduzido
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                    ),
                    shape = RoundedCornerShape(16.dp) // Usando o mesmo arredondamento
                )
                .padding(horizontal = 8.dp, vertical = 6.dp) // Espaçamento interno ajustado
        )


    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IMCCalculatorApp()
}
