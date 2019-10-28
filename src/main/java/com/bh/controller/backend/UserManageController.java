package com.bh.controller.backend;

import com.bh.common.RoleEnum;
import com.bh.common.ServerResponse;
import com.bh.service.IUserService;
import com.bh.utils.Const;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/")
public class UserManageController {
    @Resource(name = "userServiceImpl")
    IUserService userService;
    @RequestMapping("login/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username,
                                @PathVariable("password") String password,
                                HttpSession session){
        ServerResponse serverResponse = userService.login(username,password, RoleEnum.ROLE_ADMIN.getRole());//管理员
        //判断是否成功
        if (serverResponse.isSuccess())
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        return serverResponse;
    }
}
