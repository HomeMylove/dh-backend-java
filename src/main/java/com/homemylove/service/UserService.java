package com.homemylove.service;

import com.homemylove.core.api.R;
import com.homemylove.entity.User;
import com.homemylove.vo.AuthUserVo;
import com.homemylove.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    R<?> register(User user);

    R<AuthUserVo> login(User user);

    UserInfoVo getUserInfoById(Long id);

    UserInfoVo updateUserInfo(User user);

    R<String> logout(HttpServletRequest request);
}
