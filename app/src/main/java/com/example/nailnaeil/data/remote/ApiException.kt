package com.example.nailnaeil.data.remote

/** [code]는 서버가 내려준 error.code (예: INVALID_CREDENTIALS). 없으면 null. */
class ApiException(val code: String?, message: String) : Exception(message)
