package com.maksimov.caria.data

sealed class Failure {

    object UnknownError: Failure()
    object NetworkConnectionError: Failure()
}
