package com.inspetor

object Inspetor {
    private val inspetorClient = InspetorClient()

    fun sharedInstance(): InspetorClient {
        return inspetorClient
    }
}