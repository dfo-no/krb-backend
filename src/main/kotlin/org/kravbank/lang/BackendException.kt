package org.kravbank.lang

open class BackendException(message: String, exception: Throwable?) : Exception(message, exception) {

    constructor(message: String) : this(message, null)

}