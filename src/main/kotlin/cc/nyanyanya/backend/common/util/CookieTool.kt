package cc.nyanyanya.backend.common.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

object CookieTool {
    fun getCookieValue(cookie: String, name: String): String? {
        val cookieList = cookie.split("; ")
        for (c in cookieList) {
            if (c.startsWith(name + "=")) {
                return c.substring(name.length + 1)
            }
        }
        return null
    }

    fun addCookie(cookie: String, name: String, response: HttpServletResponse) {
        val logonStateCookie = Cookie(cookie, name)
        logonStateCookie.maxAge = 30 * 24 * 60 * 60 // 30 days
        logonStateCookie.secure = true
        response.addCookie(logonStateCookie)
    }
}
