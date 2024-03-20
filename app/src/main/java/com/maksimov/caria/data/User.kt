package com.maksimov.caria.data

data class User(
    val defaultLinks: DefaultLinks,
    var isReg: Boolean = false,
    val history: BrowserHistory? = null
): java.io.Serializable