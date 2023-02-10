package com.argump.visitstore.persistence

import com.argump.visitstore.model.ResponseStore
import com.argump.visitstore.source.ApiEndpoint
import org.koin.dsl.module

val moduleRepository = module {
    factory { Repository(get()) }
}

class Repository(
    private val apiEndpoint: ApiEndpoint
) {

    suspend fun fetchLogin(username: String, password: String): ResponseStore {
        return apiEndpoint.postLogin(username, password)
    }

}