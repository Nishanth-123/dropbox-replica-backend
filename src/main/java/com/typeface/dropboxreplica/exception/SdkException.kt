package com.typeface.dropboxreplica.exception

class SdkException(message: String) : TypefaceException(message, ErrorCodes.SDK_ERROR.name, 500)