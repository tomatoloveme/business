package com.bh.service.impl;

import com.bh.common.CheckEnum;
import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.dao.CartMapper;
import com.bh.pojo.Cart;
import com.bh.pojo.Product;
import com.bh.service.ICartService;
import com.bh.service.IProductService;
import com.bh.utils.BigDecimalUtils;
import com.bh.vo.CartProductVO;
import com.bh.vo.CartVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("cartServiceImpl")
public class CartServiceImpl implements ICartService {
    @Resource(name = "productServiceImpl")
    IProductService productService;
    @Autowired
    CartMapper cartMapper;
    @Override
    public ServerResponse addProductToCart(Integer userId, Integer productId, Integer count) {

        //参数非空判断
        if (productId ==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id不能为空！");
        if (count ==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"数量不能为0！");

        //判断此商品是否存在
        ServerResponse<Product> serverResponse = productService.findProductById(productId);
        if (!serverResponse.isSuccess()) //商品不存在
            return ServerResponse.serverResponseByError(serverResponse.getStatus(),serverResponse.getMsg());
        Product product = serverResponse.getData();
        if (product.getStock()<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"卖完了！嘿嘿");

        //判断商品是否在购物车中
        Cart cart = cartMapper.findCartByUseridAndProductId(userId,productId);

        if (cart == null){
            //添加
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            newCart.setChecked(CheckEnum.CART_PRODCUT_CHECK.getCheck());
            int result = cartMapper.insert(newCart);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败！");
        }else {
            //更新购物车中商品的数量
            cart.setQuantity(cart.getQuantity()+count);
            int result =cartMapper.updateByPrimaryKey(cart);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新失败！");
        }

        //封装购物车   的对象VO
        CartVO cartVO = getCartVO(userId);


        //返回CartVO
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse listAll(Integer userid) {
        return ServerResponse.serverResponseBySuccess(getCartVO(userid));
    }

    @Override
    public ServerResponse findCartsByUseridAndChecked(Integer userId) {
        List<Cart> cartList = cartMapper.findCartsByUseridAndChecked(userId);
        return ServerResponse.serverResponseBySuccess(cartList);
    }

    //根据传入的userid把他的购物车信息封装成一个cartVo
    private CartVO getCartVO(Integer userid){
        CartVO cartVO = new CartVO();

        //根据userid查询用户的购物信息->List<Cart>
        List<Cart> cartList = cartMapper.findCartByUserid(userid);
        if (cartList==null||cartList.size()==0)
            return cartVO;
        //List<Cart>->List<CartProductVO>
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        //定义购物车总价格
        BigDecimal cartTotalPrice = new BigDecimal("0");
        int limit_quantity = 0; //购物车商品数量
        String limitQuantity = null;


        for (Cart cart:cartList){
            //cart->CartProductVO

            CartProductVO cartProductVO = new CartProductVO();
            cartProductVO.setId(cart.getId());
            cartProductVO.setUserId(userid);
            cartProductVO.setProductId(cart.getProductId());
            ServerResponse<Product> serverResponse = productService.findProductById(cart.getProductId());
            if (serverResponse.isSuccess()){
                Product product = serverResponse.getData();
                if (product.getStock()>=cart.getQuantity()){
                    limit_quantity = cart.getQuantity();
                    limitQuantity = "LIMIT_NUM_SUCCESS";
                }else {
                    limit_quantity = product.getStock();
                    limitQuantity = "LIMIT_NUM_FAIL";
                }
                cartProductVO.setQuantity(limit_quantity);
                cartProductVO.setLimitQuantity(limitQuantity);
                cartProductVO.setProductName(product.getName());
                cartProductVO.setProductSubtitle(product.getSubtitle());
                cartProductVO.setProductMainImage(product.getMainImage());
                cartProductVO.setProductPrice(product.getPrice());
                cartProductVO.setProductStatus(product.getStatus());
                cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue()
                        ,cart.getQuantity()*1.0));
                cartProductVO.setProductStock(product.getStock());
                cartProductVO.setProductChecked(cart.getChecked());

                cartProductVOList.add(cartProductVO);

                 if (cart.getChecked() == CheckEnum.CART_PRODCUT_CHECK.getCheck()){
                     //商品已经选中
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                 }



            }
        }
        //计算购物车总的价格
        cartVO.setCarttotalprice(cartTotalPrice);

        //判断是否全选
        Integer isAllChecked = cartMapper.isAllChecked(userid);
        if (isAllChecked == 0){
            //全选
            cartVO.setIsallchecked(true);
        }else{
            cartVO.setIsallchecked(false);
        }
        //构建cartvo
        cartVO.setCartProductVOList(cartProductVOList);

        return cartVO;
    }
}
