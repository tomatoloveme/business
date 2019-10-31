package com.bh.controller.front;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.pojo.Shipping;
import com.bh.pojo.User;
import com.bh.service.IShippingService;
import com.bh.utils.Const;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {
    @Resource(name = "shippingServiceImpl")
    IShippingService shippingService;
    @RequestMapping("add.do")
    public ServerResponse add(Shipping shipping, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN, "未登录");
        }
        shipping.setUserId(user.getId());

        return shippingService.add(shipping);
    }
}
