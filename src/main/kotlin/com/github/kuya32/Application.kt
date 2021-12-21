package com.github.kuya32

import com.github.kuya32.di.mainModule
import io.ktor.application.*
import com.github.kuya32.plugins.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    configureSecurity()
    configureRouting()
    configureHTTP()
//    configureSockets()
    configureMonitoring()
    configureSerialization()

}
