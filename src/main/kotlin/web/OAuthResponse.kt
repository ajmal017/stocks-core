package org.cerion.stocks.core.web

data class OAuthResponse(val accessToken: String, val refreshToken: String?, val expiresIn: Int, val refreshExpiresIn: Int?)
