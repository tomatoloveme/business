package com.bh.service;

import com.bh.common.ServerResponse;
import javax.servlet.http.HttpSession;

public interface ICartService {

    /*
    * 添加商品到购物车
    * */
    public ServerResponse addProductToCart(Integer userId,Integer productId,Integer count);


    /*
     * 查询自己的购物车
     *
     *
     * */
    public ServerResponse listAll(Integer userid);
}
