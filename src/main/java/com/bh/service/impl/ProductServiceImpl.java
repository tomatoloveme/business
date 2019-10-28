package com.bh.service.impl;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.dao.ProductMapper;
import com.bh.pojo.Product;
import com.bh.service.IProductService;
import com.bh.vo.ProductListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service("productServiceImpl")
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse addOrUpdate(Product product) {
        if (product == null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空！");

        //商品中的subImage 1.png,2.png,3.png 而主图字段是没有值的，是subimage中的第一张图，
        //所以此处需要修改
        String subImage = product.getSubImages();
        if (subImage != null&&!subImage.equals("")){
            String[] subImageArr = subImage.split(",");
            if (subImageArr.length>0)    //在做一次判断，有可能
                product.setMainImage(subImageArr[0]);
        }





        Integer productId = product.getId();

        if (productId == null){ //如果传入的id是空，就是添加，否则是更新
            int result = productMapper.insert(product);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败！");
            return ServerResponse.serverResponseBySuccess();
        }else {
            int result  = productMapper.updateByPrimaryKey(product);
            if (result<=0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新失败！");
            return ServerResponse.serverResponseBySuccess();
        }

    }


    /*
    * 商品的上下架
    * */
    @Override
    public ServerResponse downOrUp(Product product) {
        if (product == null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"id不能为空！");
        int result = productMapper.updateByPrimaryKey(product);
        if (result <0 )
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"上下架失败");

        return ServerResponse.serverResponseBySuccess("修改产品状态成功");
    }

    @Override
    public ServerResponse search(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        if (productName!=null)
            productName = "%"+productName+"%";
        //是一个spring AOP 在下面的sql语句语句执行之前添加一个limit
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.findProductsByNameAndId(productId,productName);

        List<ProductListVO> productListVOList = Lists.newArrayList();
        //将List<Product>->List<ProductListVO>
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                //product->productListVO
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }

        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());

        return  productListVO;
    }


/*    public ServerResponse upload(MultipartFile file, String path) {

        if(file==null){
            return ServerResponse.serverResponseByError();
        }

        //step1:获取图片名称
        String  orignalFileName=  file.getOriginalFilename();
        //获取图片的扩展名
        String exName=  orignalFileName.substring(orignalFileName.lastIndexOf(".")); // .jpg
        //为图片生成新的唯一的名字
        String newFileName= UUID.randomUUID().toString()+exName;

        File pathFile=new File(path);
        if(!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }

        File file1=new File(path,newFileName);

        try {
            file.transferTo(file1);
            //上传到图片服务器
            FTPUtil.uploadFile(Lists.newArrayList(file1));
            //.....
            Map<String,String> map= Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);

            //删除应用服务器上的图片
            file1.delete();

            return ServerResponse.serverResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }*/
}
