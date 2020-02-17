package com.legiti

object Inspetor {
    // There will be a warning saying that you cannot store the context in a static class
    // but we are actually saving the applicationContext and according to this (https://stackoverflow.com/a/40235834)
    // we should not have a problem

    private val inspetorClient = InspetorClient()

    fun sharedInstance(): InspetorClient {
        return this.inspetorClient
    }
}