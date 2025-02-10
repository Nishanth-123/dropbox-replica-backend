package com.typeface.dropboxreplica.exception

open class TypefaceException(
    message: String,
    val errorCode: String,
    @Deprecated("Will remove Http Response from Exceptions in future")
    val httpResponseCode: Int? = null,
) : Exception(message)