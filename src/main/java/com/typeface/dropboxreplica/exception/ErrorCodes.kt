package com.typeface.dropboxreplica.exception

enum class ErrorCodes(val code: Int) {
//    DUPLICATE_ENTITY(40001),
//    ENTITY_NOT_FOUND(40002),
//    ORDER_ERROR(40003),
//    RATE_LIMIT_EXCEEDED(40004),
    SDK_ERROR(40005),
    TRADING_ACCOUNT_DISCONNECTED(40006),
    VALIDATION_ERROR(40007),

    //For backward compatibility only
    INVALID_ARGUMENT(40008),
    INTERNAL_ERROR(50001),
    UNKNOWN_EXCEPTION(60001);

    companion object {
        fun fromCode(code: Int): ErrorCodes {
            return entries.find { it.code == code } ?: UNKNOWN_EXCEPTION
        }
    }
}