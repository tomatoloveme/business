package com.bh.interceptors;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import com.bh.utils.Const;
import com.bh.utils.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/*
* 来拦截前台用户的
*
* */
@CrossOrigin
@Component
public class PortalAuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            ServerResponse serverResponse = ServerResponse.serverResponseByError(ResponseCode.ERROR,"未登录");
            response.reset();


            //设置跨域访问
            response.setHeader("Access-Control-Allow-Credentials","true");
            response.setHeader("Content-type","application/json;charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin","http://localhost:8081");
            response.setHeader("X-Powered-By","3.2.1");
            response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild");

            PrintWriter printWriter = response.getWriter();
            String json = JsonUtils.obj2String(serverResponse);
            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
            return false;
        }
        return true;
    }
}
