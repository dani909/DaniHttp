package de.dani09.http

class HttpResponseDummy @JvmOverloads constructor(responseCode: Int = 0) : HttpResponse(responseCode, byteArrayOf(), mapOf())