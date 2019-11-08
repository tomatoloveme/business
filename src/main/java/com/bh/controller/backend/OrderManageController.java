package com.bh.controller.backend;

import com.bh.common.ResponseCode;
import com.bh.common.RoleEnum;
import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import com.bh.service.IOrderService;
import com.bh.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/order/")
public class OrderManageController {
    @Autowired
    IOrderService orderService;
    @RequestMapping("send_goods.do")
    public ServerResponse sendGoods(@RequestParam("orderNo")long orderNo, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user.getRole() == RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足！");
        return orderService.sendGoods(orderNo);
    }

}
