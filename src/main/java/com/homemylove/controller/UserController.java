package com.homemylove.controller;

import com.homemylove.core.api.R;
import com.homemylove.entity.User;
import com.homemylove.service.UserService;
import com.homemylove.vo.AuthUserVo;
import com.homemylove.vo.UserInfoVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public R<?> register(@RequestBody User user){
        return userService.register(user);
    }
    /**
     * 登陆返回 token
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<AuthUserVo> login(@RequestBody User user){
        return userService.login(user);
    }

    @GetMapping("/info/{id}")
    public R<UserInfoVo> getUserInfo(@Nullable @PathVariable("id") Long id){
        return R.data(userService.getUserInfoById(id));
    }

    @PostMapping("/update")
    public R<UserInfoVo> updateUserInfo(@RequestBody User user){
        return R.data(userService.updateUserInfo(user));
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        return userService.logout(request);
    }

}
