package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberAction(view: View)
    {
        if (view is Button)
        {
            if (view.text == ".")
            {
                if (canAddDecimal)
                    binding.workingsTv.append(view.text)
                canAddDecimal = false
            }
            else
                binding.workingsTv.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View)
    {
        if (view is Button && canAddOperation)
        {
            binding.workingsTv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View)
    {
        binding.workingsTv.text = ""
        binding.resultsTv.text = ""
    }

    fun  backSpaceAction(view: View)
    {
        val length = binding.workingsTv.length()
        if (length > 0)
            binding.workingsTv.text = binding.workingsTv.text.subSequence(0, length-1)
    }

    fun equalsAction(view: View)
    {
        binding.resultsTv.text = calculateResults()
    }

    private fun calculateResults(): CharSequence
    {
        val digitsOperators = digitsOperator()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculator(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }


    private fun timesDivisionCalculator(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
            list = calcTimesDiv(list)
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var restartIndex = passedList.size
        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        list.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/' ->
                    {
                        list.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else ->
                    {
                        list.add(prevDigit)
                        list.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                list.add(passedList[i])
        }

        return list
    }

    private fun digitsOperator(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in binding.workingsTv.text.toString())
        {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }
}