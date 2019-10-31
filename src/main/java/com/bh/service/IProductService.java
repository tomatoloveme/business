package com.bh.service;

import com.bh.common.ServerResponse;
import com.bh.pojo.Product;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

public interface IProductService {
    /*
    * 商品的添加或者更新
    *
    * */
    public ServerResponse addOrUpdate(Product product);


    /*
    * 商品的上下架
    * */

    public ServerResponse downOrUp(Product product);

    /*
    * 文件上传和下载
    * */


   /* public ServerResponse upload(MultipartFile file, String path) ;*/

    /*
    * 后台商品的搜索
    * */
    public ServerResponse search(String productName,
                                 Integer productId,
                                 Integer pageNum,
                                 Integer pageSize);

    /*
    * 商品的详情页
    * */

    public ServerResponse<Product> detail(Integer productId);


    /*
    * 根据商品Id来查询商品信息(库存数量)
    * */
    public ServerResponse<Product> findProductById(Integer productId);
}
