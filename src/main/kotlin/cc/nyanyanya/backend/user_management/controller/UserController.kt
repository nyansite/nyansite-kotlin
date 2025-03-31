package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.User
import cc.nyanyanya.backend.user_management.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(userService: UserService) {
    var userService: UserService = userService

//    @GetMapping("/selectAll")
//    fun selectAll(): List<User?> {
//        val list: List<User?> = userService.selectAll() ?: arrayListOf()
//        return list
//    }

//    /**
//     * 分页查询
//     */
//    @GetMapping("/selectPage")
//    fun selectPage(
//        @RequestParam(defaultValue = "1") pageNum: Int,
//        @RequestParam(defaultValue = "10") pageSize: Int,
//        @RequestParam(defaultValue = "") name: String
//    ): List<User> {
//        val page: List<User> = userService.selectPage(pageNum, pageSize, name)
//        return page
//    }


}
