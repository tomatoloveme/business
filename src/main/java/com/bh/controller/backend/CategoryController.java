package com.bh.controller.backend;

import com.bh.common.ResponseCode;
import com.bh.common.RoleEnum;
import com.bh.common.ServerResponse;
import com.bh.pojo.Category;
import com.bh.pojo.User;
import com.bh.service.ICategoryService;
import com.bh.utils.Const;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category/")
public class CategoryController {

    @Resource(name = "categoryServiceImpl")
    ICategoryService categoryService;

    /*
     * 添加类别
     *
     * */

    @RequestMapping("addCategory.do")
    public ServerResponse addCategory(Category category, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
          if (user ==null)
              return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登陆！");
          if (user.getRole()== RoleEnum.ROLE_USER.getRole())
              return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足，别瞎操作！");

        return categoryService.addCategory(category);
    }
    /*
    * 修改类别
    * categoryid
    * categoryname
    * categoryUrl
    */
    @RequestMapping("setCategory.do")
    public ServerResponse updateCategory(Category category,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登陆！");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足，别瞎操作！");

        return categoryService.updateCategory(category);
    }

    /*
     * 查看平级类别
     * categoryid
     */
    @RequestMapping("{categoryId}")
    public ServerResponse getCategoryById(@PathVariable("categoryId") Integer categoryId,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登陆！");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足，别瞎操作！");

        return categoryService.getCategoryById(categoryId);
    }

    /*
    *
    *递归查看
    *
    * */

    @RequestMapping("deep/{categoryId}")
    public ServerResponse deepCategory(@PathVariable("categoryId") Integer categoryId,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null)
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"用户未登陆！");
        if (user.getRole()== RoleEnum.ROLE_USER.getRole())
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足，别瞎操作！");

        return categoryService.deepCategory(categoryId);
    }

}
