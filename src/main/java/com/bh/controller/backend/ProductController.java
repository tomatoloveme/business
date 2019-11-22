package com.bh.controller.backend;

import com.bh.common.ResponseCode;
import com.bh.common.RoleEnum;
import com.bh.common.ServerResponse;
import com.bh.pojo.Product;
import com.bh.pojo.User;
import com.bh.service.IProductService;
import com.bh.utils.Const;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
@CrossOrigin
@RestController
@RequestMapping("/manage/product/")
public class ProductController {
    @Resource(name = "productServiceImpl")
    IProductService productService;
    /*
    * 商品添加&更新
    *
    * */
    @RequestMapping(value = "save.do")
    public ServerResponse addOrUpdate(Product product, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登陆！");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足，别瞎操作！");
        return productService.addOrUpdate(product);
    }

    /*
    * 商品的上下架
    * */
    @RequestMapping("set_sale_status.do")
    public ServerResponse downOrUp(Product product,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登陆！");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足，别瞎操作！");
        return productService.downOrUp(product);
    }

    /*
    * 搜索商品
    * */
    @RequestMapping("search.do")
    public ServerResponse search(@RequestParam(name = "productName",required = false)String productName,
                                @RequestParam(name = "productId",required = false)Integer productId,
                                 @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize,HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        System.out.println(productId);
        if (user ==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登陆！");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足，别瞎操作！");

        return productService.search(productName,productId,pageNum,pageSize);
    }

    /*
    *商品详情
    *
    * */
    @RequestMapping("{productId}")
    public ServerResponse detail(@PathVariable("productId")Integer productId){


        return productService.detail(productId);
    }

    /*
     * 根据类别id和isHot值来查询哪些是product是热门的
     * */
    @RequestMapping("isHot")
    public ServerResponse findHotByCategoryId(Integer categoryId,Integer isHot){

        return productService.findHotByCategoryId(categoryId, isHot);
    }

}
