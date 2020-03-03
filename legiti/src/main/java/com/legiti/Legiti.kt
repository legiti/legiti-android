package com.legiti

object Legiti {
    // There will be a warning saying that you cannot store the context in a static class
    // but we are actually saving the applicationContext and according to this (https://stackoverflow.com/a/40235834)
    // we should not have a problem

    private val legitiClient = LegitiClient()

    fun sharedInstance(): LegitiClient {
        return this.legitiClient
    }
}