package com.homemylove.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.homemylove.auth.AuthInfo;
import com.homemylove.auth.Authenticator;
import com.homemylove.core.api.R;
import com.homemylove.core.mdc.MDCKey;
import com.homemylove.entity.User;
import com.homemylove.mapper.UserMapper;
import com.homemylove.properties.JwtProperties;
import com.homemylove.service.UserService;
import com.homemylove.utils.JwtUtil;
import com.homemylove.utils.SecurityUtil;
import com.homemylove.vo.AuthUserVo;
import com.homemylove.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private Authenticator authenticator;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private RedisTemplate<String,AuthInfo> redisTemplate;


    @Override
    public R<?> register(User user) {
        User one = userMapper.selectOne(User.gw().eq(User::getAccount, user.getAccount()));
        if(one != null){
            return R.fail("账号已存在");
        }
        user.setCreateTime(new Date());
        // 生成 salt
        String salt = SecurityUtil.generateSalt();
        user.setSalt(salt);
        String username = "用户" + RandomUtil.randomString(10);
        user.setUsername(username);
        // 生成加密密码
        String password = SecurityUtil.encryptPassword(user.getPassword(), salt);
        user.setPassword(password);
        if (userMapper.insert(user) == 1)
            return R.success("注册成功");
        else
            return R.fail("注册失败");
    }

    @Override
    public R<AuthUserVo> login(User user) {
        // 根据账号名获取
        User one = userMapper.selectOne(User.gw().eq(User::getAccount, user.getAccount()));
        if(one == null ){
            return R.fail("用户不存在");
        }
        String salt = one.getSalt();
        String password = SecurityUtil.encryptPassword(user.getPassword(), salt);
        if(one.getPassword().equals(password)){
            // 登陆成功
            AuthInfo authInfo = new AuthInfo(one.getId(), one.getUsername(), one.getAvatar());
            String token = JwtUtil.generateToken(authInfo, jwtProperties.getExpiration(), jwtProperties.getSecret());
            redisTemplate.opsForValue().set(token,authInfo);

            AuthUserVo userVo = new AuthUserVo();
            BeanUtils.copyProperties(one,userVo);
            userVo.setToken(token);

            return R.data(userVo);
        }else {
            return R.fail("密码错误");
        }
    }

    @Override
    public UserInfoVo getUserInfoById(Long id) {
        if(id == null) return null;
        User user = userMapper.selectById(id);
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(user,vo);
        return vo;
    }

    @Override
    public UserInfoVo updateUserInfo(User user) {
        String id = MDC.get(MDCKey.ID);
        user.setId(Long.valueOf(id));
        int i = userMapper.updateById(user);
        return getUserInfoById(user.getId());
    }

    @Override
    public R<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 使当前会话无效
        }
        // 清除 token
        String authToken = JwtUtil.getTokenFromRequest(request);
        if(authToken != null){
            redisTemplate.delete(authToken);
        }
        return R.success("退出登陆");
    }
}
