package com.bh.service.impl;

import com.bh.common.ResponseCode;
import com.bh.common.RoleEnum;
import com.bh.common.ServerResponse;
import com.bh.dao.UserMapper;
import com.bh.pojo.User;
import com.bh.service.IUserService;
import com.bh.utils.MD5Utils;
import com.bh.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse register(User user) {
        //1.参数校验
        if(user == null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        //2.判断用户名是否存在
        int result = userMapper.isexistsusername(user.getUsername());
        if(result>0){//用户名已经存在
            return ServerResponse.serverResponseByError(ResponseCode.USERNAME_EXISTS,"用户名已经存在！");
        }
        //3.判断邮箱是否存在
        int resultemil = userMapper.isexistsemail(user.getEmail());
        if(resultemil>0){//邮箱已经存在
            return ServerResponse.serverResponseByError(ResponseCode.EMAIL_EXISTS,"邮箱已经存在！");
        }
        //4.MD5密码加密，设置用户角色
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        //设计角色为普通用户
        user.setRole(RoleEnum.ROLE_USER.getRole());
        //5.注册
       int insertResult =  userMapper.insert(user);
       if (insertResult <= 0){
           return ServerResponse.serverResponseByError(ResponseCode.ERROR,"注册失败！");
       }
        //6.返回
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse login(String username, String password,int type) {

        //参数校验
        if (username == null||username.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空！");
        if (password == null||password.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码不能为空！");
        //判断用户名是否存在
        int result = userMapper.isexistsusername(username);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不存在");
        //密码加密
        password = MD5Utils.getMD5Code(password);
        //登录
        User user = userMapper.findUserByUsernameAndPassword(username,password);
        if (user == null)//密码错误
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码错误！");
        //密码正确
        if (type == 0) //0是管理员
            if (user.getRole()==RoleEnum.ROLE_USER.getRole())//没有管理员权限
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"没有登录权限！");
        return ServerResponse.serverResponseBySuccess(user);

    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //1.参数非空校验
        if (username == null||username.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空！");
        //2.根据用户名查询问题
        String question = userMapper.forget_get_question(username);
        //3.返回结果
        if (question == null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"没有查询到问题！");

        return ServerResponse.serverResponseBySuccess(question);
    }
        /*
        * 根据用户名和问题查询是否正确
        * */
    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //1.参数非空校验
        if (username == null||username.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空！");
        if (question == null||question.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"问题不能为空！");
        if (answer == null||answer.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案不能为空！");
        //2.查看答案是否正确
        int result = userMapper.forget_check_answer(username,question,answer);
        //3.返回结果
        if (result <= 0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案错误！");

        //4.正确则返回
        //生成token
        String token =  UUID.randomUUID().toString();
        TokenCache.set("username:"+username,token);
        return ServerResponse.serverResponseBySuccess(token);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String newpassword, String forgettoken) {
        //1.参数非空校验
        if (username == null||username.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空！");
        if (newpassword == null||newpassword.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"新密码不能为空！");
        if (forgettoken == null||forgettoken.equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"token不能为空！");
        //判断是否修改的自己的账号
        String token = TokenCache.get("username:"+username);
        if (token == null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"不能修改别人的密码！或者token已经过期！");
        }
        if (!token.equals(forgettoken))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"无效的token！");

        int result = userMapper.forget_reset_password(username,MD5Utils.getMD5Code(newpassword));

        if (result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码修改失败！");
        }
        return ServerResponse.serverResponseBySuccess();     //密码修改成功
    }

    @Override
    public ServerResponse update_information(User user) {
        if(user == null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }


        int result = userMapper.updateUserByActivate(user);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败！");
        return ServerResponse.serverResponseBySuccess();



    }


}
