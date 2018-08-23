package de.dani09.http

/**
 * An ProgressListener for HttpRequest to get current execution progress
 * note that you can override just the methods that you need
 * @see HttpRequest.addProgressListener
 */
interface HttpProgressListener {
    /**
     * onStart is called when the HttpRequest starts
     * @param length the length that the Http Body will be in bytes
     */
    fun onStart(length: Long) {}

    /**
     * onProgress is called every time when some Progress was made some bytes were read
     * @param bytesRead the count of total bytes read since start of the HttpRequest
     * @param fullLength the count of total bytes that the HttpRequest has to read
     */
    fun onProgress(bytesRead: Long, fullLength: Long) {}

    /**
     * onProgress is called every time when some Progress was made some bytes were read
     * @param percentage the progress percentage of the request
     */
    fun onProgress(percentage: Double) {}

    /**
     * onFinish is called once the HttpRequest is done
     */
    fun onFinish() {}
}
