package com.bh.controller.front;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import com.bh.service.IOrderService;
import com.bh.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    IOrderService orderService;

    /*
    *
    * 创建订单接口
    * */
    @RequestMapping("{shippingId}")
    public ServerResponse createOrder(@PathVariable("shippingId") Integer shippingId, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN, "未登录");
        }
        return orderService.createOrder(user.getId(),shippingId);
    }

}
