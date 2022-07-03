package com.lukman.kalkulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View)
    {
        if (view is Button)
        {
            if (view.text == ".")
            {
                if (canAddDecimal)
                    tvHitung.append(view.text)

                canAddDecimal = false
            }
            else
                tvHitung.append(view.text)

            canAddOperation = true
        }
    }
    fun operatorAction(view: View)
    {
        if (view is Button && canAddOperation){
            tvHitung.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClear(view: View)
    {
        tvHasil.text = ""
        tvHitung.text = ""
    }
    fun clear(view: View)
    {
        val length = tvHitung.length()
        if (length>0)
            tvHitung.text = tvHitung.text.subSequence(0,length -1)
    }
    fun equalAction(view: View)
    {
        tvHasil.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timeDivision = timeDivisionCalculate(digitsOperators)
        if (timeDivision.isEmpty()) return ""

        val result = addSubstractCalculate(timeDivision)
        return result.toString()
    }

    private fun addSubstractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigits = passedList[i + 1] as Float
                if (operator == "+")
                    result += nextDigits
                if (operator == "-")
                    result -= nextDigits

//                Toast.makeText(applicationContext, "pre "+result+" Op "+ operator+" nex "+nextDigits, Toast.LENGTH_SHORT).show()
            }
        }
        return result
    }

    private fun timeDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator)
                {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigits = ""
        for (character in tvHitung.text)
        {
            if (character.isDigit()|| character =='.')
                currentDigits += character
            else
            {
                list.add((currentDigits.toFloat()))
                currentDigits = ""
                list.add(character)
            }
        }
        if (currentDigits != "")
            list.add(currentDigits.toFloat())

        return list
    }
}