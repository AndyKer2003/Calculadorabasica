package com.example.calculadorabasica

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private var currentInput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.result_text)

        // Botones numéricos
        val button1: Button = findViewById(R.id.button_1)
        val button2: Button = findViewById(R.id.button_2)
        val button3: Button = findViewById(R.id.button_3)
        val button4: Button = findViewById(R.id.button_4)
        val button5: Button = findViewById(R.id.button_5)
        val button6: Button = findViewById(R.id.button_6)
        val button7: Button = findViewById(R.id.button_7)
        val button8: Button = findViewById(R.id.button_8)
        val button9: Button = findViewById(R.id.button_9)
        val button0: Button = findViewById(R.id.button_0)

        // Botones de operaciones
        val buttonSum: Button = findViewById(R.id.button_sum)
        val buttonSubtract: Button = findViewById(R.id.button_subtract)
        val buttonMultiply: Button = findViewById(R.id.button_multiply)
        val buttonDivide: Button = findViewById(R.id.button_divide)

        // Botones de funciones
        val buttonClear: Button = findViewById(R.id.button_clear)
        val buttonEqual: Button = findViewById(R.id.button_equal)

        // Configuración de los botones numéricos
        val numberButtons = arrayOf(button1, button2, button3, button4, button5, button6, button7, button8, button9, button0)
        for (button in numberButtons) {
            button.setOnClickListener {
                currentInput += button.text
                resultText.text = currentInput
            }
        }

        // Configuración de las operaciones
        buttonSum.setOnClickListener { appendOperator("+") }
        buttonSubtract.setOnClickListener { appendOperator("-") }
        buttonMultiply.setOnClickListener { appendOperator("*") }
        buttonDivide.setOnClickListener { appendOperator("/") }

        // Botón de igual
        buttonEqual.setOnClickListener {
            try {
                val result = evaluateExpression(currentInput)
                resultText.text = result.toString()
                currentInput = result.toString()  // Guardamos el resultado para nuevas operaciones
            } catch (e: Exception) {
                resultText.text = "Error"
            }
        }

        // Botón de limpiar
        buttonClear.setOnClickListener {
            currentInput = ""
            resultText.text = "0"
        }
    }

    private fun appendOperator(operator: String) {
        if (currentInput.isNotEmpty() && !currentInput.endsWith(operator)) {
            currentInput += operator
            resultText.text = currentInput
        }
    }

    // Función para evaluar la expresión sin dependencias externas
    private fun evaluateExpression(expression: String): Double {
        val operands = mutableListOf<Double>()
        val operators = mutableListOf<Char>()

        var currentNum = ""

        for (char in expression) {
            when {
                char.isDigit() || char == '.' -> currentNum += char  // Construimos números
                else -> {
                    if (currentNum.isNotEmpty()) {
                        operands.add(currentNum.toDouble())  // Añadimos el número al arreglo
                        currentNum = ""
                    }
                    operators.add(char)  // Añadimos el operador
                }
            }
        }
        if (currentNum.isNotEmpty()) {
            operands.add(currentNum.toDouble())
        }

        // Evaluamos la expresión según el orden de operaciones
        while (operators.isNotEmpty()) {
            val index = operators.indexOfFirst { it in arrayOf('*', '/', '+', '-') }
            val op = operators.removeAt(index)
            val num1 = operands.removeAt(index)
            val num2 = operands.removeAt(index)

            val result = when (op) {
                '+' -> num1 + num2
                '-' -> num1 - num2
                '*' -> num1 * num2
                '/' -> num1 / num2
                else -> 0.0
            }
            operands.add(index, result)  // Insertamos el resultado en el lugar correcto
        }

        return operands.firstOrNull() ?: 0.0
    }
}
