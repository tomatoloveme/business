package com.bh.controller.front;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import com.bh.service.ICartService;
import com.bh.utils.Const;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {
    /*
    * 添加商品到购物车
    * */
    @Resource(name = "cartServiceImpl")
    ICartService cartService;
    @RequestMapping("add/{productId}/{count}")
    public ServerResponse addCart(@PathVariable("productId")Integer productId,
                                  @PathVariable("count")Integer count,
                                  HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
       /* if (user == null){
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }*/
    return cartService.addProductToCart(user.getId(),productId,count);
    }

    /*
    * 查询自己的购物车
    *
    *
    * */
    @RequestMapping("list.do")
    public ServerResponse listAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }
        return cartService.listAll(user.getId());
    }

    /*
    * 更新购物车某个产品数量
    * */
 /*   @RequestMapping("update.do")
    public ServerResponse updateProductNum(Integer productId,Integer count,HttpSession){
        if (productId == null)
            return ServerResponse.serverResponseByError("productid不能为空");

        return
    }*/
}
