package com.bh.service;

import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import org.springframework.stereotype.Component;



public interface IUserService {

    /*
    *
    * 注册接口
    * */

    public ServerResponse register(User user);
    /*
     * 登录接口
     * 1.普通用户1
     * 2.管理员是0
     * */

    public ServerResponse login(String username,String password,int type);


    /*
     * 根据username获取密保问题
     * */

    public ServerResponse forget_get_question( String username);


    /*
     * 提交答案
     * */
    public ServerResponse forget_check_answer(String username, String question, String answer);


    /*
     * 修改密码
     * */


    public ServerResponse forget_reset_password(String username,String newpassword,String forgettoken);



    /*
     * 更新用户信息
     * */
    public ServerResponse update_information(User user);



}

