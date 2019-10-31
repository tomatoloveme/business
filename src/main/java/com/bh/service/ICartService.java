package com.bh.service;

import com.bh.common.ServerResponse;
import com.bh.pojo.Cart;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    /*
    *
    * 根据用户id查看用户已经选中的商品
    * */
    public  ServerResponse<List<Cart>> findCartsByUseridAndChecked(Integer userId);

}
