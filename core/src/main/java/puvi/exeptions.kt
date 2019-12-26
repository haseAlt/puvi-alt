package puvi

class NoConnectionException(cause: Throwable? = null) :
    RuntimeException("No connection", cause)

class HttpException(val code: Int, cause: Throwable? = null) :
    RuntimeException("Http error code [$code]", cause)
