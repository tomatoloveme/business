package com.bh.service;

import com.bh.common.ServerResponse;
import com.bh.pojo.Category;

public interface ICategoryService {
    /*
     * 添加类别
     *
     * */

    public ServerResponse addCategory(Category category);
    /*
     * 修改类别
     * categoryid
     * categoryname
     * categoryUrl
     */

    public ServerResponse updateCategory(Category category);

    /*
     * 查看平级类别
     * categoryid
     */

    public ServerResponse getCategoryById( Integer categoryId);

    /*
     *
     *递归查看
     *
     * */


    public ServerResponse deepCategory( Integer categoryId);

    /*
    * 根据id来查询类别
    * */
    public ServerResponse selectCategory(Integer categoryId);
}
