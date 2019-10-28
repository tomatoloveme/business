package com.bh.service.impl;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.dao.CategoryMapper;
import com.bh.pojo.Category;
import com.bh.service.ICategoryService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service("categoryServiceImpl")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ServerResponse addCategory(Category category) {
        if (category == null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空！");
         int result = categoryMapper.insert(category);
         if (result<=0)
             return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加品类失败！");
         return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse updateCategory(Category category) {
        if (category==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空！");
        if (category.getId()==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"类别的id必须为空！");
        int result = categoryMapper.updateByPrimaryKey(category);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新类别失败！");

        return ServerResponse.serverResponseBySuccess() ;
    }

    @Override
    public ServerResponse getCategoryById(Integer categoryId) {
        if (categoryId == null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"id必须要传！");
        List<Category> categoryList = categoryMapper.selectCategoryById(categoryId);
        return ServerResponse.serverResponseBySuccess(categoryList,"成功");
    }

    @Override
    public ServerResponse deepCategory(Integer categoryId) {
    Set<Category> categorySet = Sets.newHashSet();

    Set<Category> categorySet1 = findAllChildCategory(categorySet,categoryId);

    Set<Integer> categoryIds = Sets.newHashSet();

    Iterator<Category> it = categorySet1.iterator();
    while (it.hasNext()){
        Category category = it.next();
        categoryIds.add(category.getId());
    }
        return ServerResponse.serverResponseBySuccess(categoryIds);
    }

    //递归查询
    public Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        //查看categoryid的信息
        Category category = (Category) categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null)
            categorySet.add(category);
        //查看parentId 为categoryId的元素
        List<Category> categoryList = categoryMapper.selectCategoryById(categoryId);
        for (Category category1:categoryList){
            //递归调用
            findAllChildCategory(categorySet,category1.getId());
        }
        return categorySet;
    }
}
