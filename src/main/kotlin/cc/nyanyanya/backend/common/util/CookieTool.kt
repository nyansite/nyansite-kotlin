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

    fun addCookie(cookieKey: String, name: String, response: HttpServletResponse) {
        val cookie = Cookie(cookieKey, name)
        cookie.maxAge = 30 * 24 * 60 * 60 // 30 days
        cookie.secure = true
        cookie.setAttribute("sameSite", "Lax")
        response.addCookie(cookie)
    }
}
