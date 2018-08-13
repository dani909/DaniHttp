package de.dani09.http

/**
 * This is a ResponseDummy that has no response, no headers
 * and the responseCode you will pass into the Constructor.
 * This class is only intended for internal use!
 */
internal class HttpResponseDummy @JvmOverloads constructor(responseCode: Int = 0) : HttpResponse(responseCode, byteArrayOf(), mapOf())