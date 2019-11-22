package com.bh.service;

import com.bh.common.ServerResponse;
import com.bh.pojo.Product;
import com.bh.vo.ProductDetailVO;
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

    public ServerResponse<ProductDetailVO> detail(Integer productId);
    /*
     * 商品的详情页
     * */

    public ServerResponse<ProductDetailVO> findByProductId(Integer productId);

    /*
    * 根据商品Id来查询商品信息(库存数量)
    * */
    public ServerResponse<Product> findProductById(Integer productId);

    /*
    * 扣库存
    * */

    public ServerResponse reduceStock(Integer productId,Integer count);

    /*
    * 根据categoryid的查询哪些商品热门
    * */
    public ServerResponse findHotByCategoryId(Integer categoryId,Integer isHot);
}
