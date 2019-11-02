package com.bh.controller.front;

import com.bh.common.ResponseCode;
import com.bh.common.RoleEnum;
import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import com.bh.service.IUserService;
import com.bh.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;
    /*
    * 注册接口
    *
    * */
    @RequestMapping(value = "register.do")
    public ServerResponse register(User user){

        return userService.register(user);
    }

    /*
    * 登录接口
    * */
    @RequestMapping("login/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username,
                                @PathVariable("password") String password,
                                HttpSession session){
        ServerResponse serverResponse = userService.login(username,password, RoleEnum.ROLE_USER.getRole()); //普通用户
        //判断是否成功
        if (serverResponse.isSuccess())
               session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        return serverResponse;
    }


    /*
    * 根据username获取密保问题
    * */
    @RequestMapping("forget_get_question/{username}")
    public ServerResponse forget_get_question(@PathVariable("username") String username){
        return userService.forget_get_question(username);
    }

    /*
    * 提交答案
    * */
    @RequestMapping("forget_check_answer.do")
    public ServerResponse forget_check_answer(String username, String question, String answer){
        return userService.forget_check_answer(username,question,answer);
    }

    /*
    * 修改密码
    * */

    @RequestMapping("forget_reset_password.do")
    public ServerResponse forget_reset_password(String username,String newpassword,String forgettoken){
        return userService.forget_reset_password(username,newpassword,forgettoken);
    }


    /*
    *
    * 修改用户信息
    * */
    @RequestMapping("update_information.do")
    public ServerResponse update_information(User user,HttpSession session){
       User loginUser = (User) session.getAttribute(Const.CURRENT_USER);
       if (loginUser==null){
           return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录！");
       }
       user.setId(loginUser.getId());
       ServerResponse serverResponse = userService.update_information(user);
        return serverResponse;
    }
}
