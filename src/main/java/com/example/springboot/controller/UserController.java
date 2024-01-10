package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springboot.common.R;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone=user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            System.out.println(code);
            //将生成的验证码保存到redis中
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("短信发送成功！");
        }
        return R.error("短信发送失败！");


    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        String codeInSession =(String) session.getAttribute(phone);
        //进行验证码的比对（页面提交的验证码和Redis中保存的验证码进行比对）
//        String s = redisTemplate.opsForValue().get(phone);
//        if (s.equals(code)){
//            //验证码匹配成功
//            redisTemplate.delete(phone);
//        }


//        codeInSession!=null && codeInSession.equals(code)
        if (true) {

            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null){
                //判断当前手机号是否为新用户，如果是则自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                //mybatis-plus会自动将保存后的结果返回到user中，极方便
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            System.out.println(user);
            return R.success(user);

        }

        return R.error("登录失败！");

    }
}
