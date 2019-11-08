package com.bh.interceptors;

import com.bh.common.ResponseCode;
import com.bh.common.RoleEnum;
import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import com.bh.utils.Const;
import com.bh.utils.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*
* 拦截后台请求
* */
@Component
public class AdminAuthroityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            response.reset();
            try {
                response.setHeader("Content-type","application/json;charset=utf-8");
                PrintWriter printWriter = response.getWriter();
                ServerResponse serverResponse = ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录！");
                String json = JsonUtils.obj2String(serverResponse);
                printWriter.write(json);
                printWriter.close();
                printWriter.flush();
            }catch (IOException e){
                e.printStackTrace();
            }

            return false;
        }

        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()){

            response.reset();
            try {
                PrintWriter printWriter = response.getWriter();
                ServerResponse serverResponse = ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足！");
                String json = JsonUtils.obj2String(serverResponse);
                printWriter.write(json);
                printWriter.close();
                printWriter.flush();
            }catch (IOException e){
                e.printStackTrace();
            }

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
