package com.maksimov.caria.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class GameViewModel(private val context: Application) : AndroidViewModel(context) {
    val betLiveData: MutableLiveData<Int> = MutableLiveData(10)
    val balanceLiveData: MutableLiveData<Int> = MutableLiveData(1000)

    var bet
        get() = betLiveData.value!!
        set(value) {
            if (value < 1 || value > balance) return
            betLiveData.value = value
        }

    var balance
        get() = balanceLiveData.value!!
        set(value) {
            balanceLiveData.value = value
        }

    fun calculate(lineUp: List<Int>, lineMaster: List<Int>, lineDown: List<Int>) {
        val line1 = lineUp.groupingBy { it }.eachCount().maxBy { it.value }.value
        val line2 = lineMaster.groupingBy { it }.eachCount().maxBy { it.value }.value
        val line3 = lineDown.groupingBy { it }.eachCount().maxBy { it.value }.value

        val profit = profitCalc(line1) + profitCalc(line2) + profitCalc(line3)
        if (profit == 0) return

        balance += profit
        if (balance == 0) balance = 1000
        if (bet > balance) bet = balance
    }



    private fun profitCalc(matches: Int) =
        when (matches) {
            2 -> 2 * -bet
            3 -> 4 * bet
            4 -> 6 * bet
            5 -> 10 * bet
            else -> 2 * -bet
        }
}