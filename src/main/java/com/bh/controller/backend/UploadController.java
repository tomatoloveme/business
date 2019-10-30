package com.bh.controller.backend;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.service.IProductService;
import com.bh.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping(value = "/manage/product")
public class UploadController {


    @Value("${business.imageHost}")
    private String imageHost;

    @Resource(name = "productServiceImpl")
    IProductService productService;
    /*
    * 请求页面的时候规定必须要用get,当请求到了html的时候，点击提交按钮的时候，当前页面的url没有变化
    * 所以点击提交以后，会提交到本类的post方法中处理
    *
    * */
    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public  String upload(){
        return "upload"; //逻辑视图     前缀+逻辑视图+后缀  --》 /WEB-INF/jsp/upload.jsp
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
/*    public ServerResponse upload2(@RequestParam(value = "upload_file",required = false) MultipartFile file){


        // String path="D:\\ftpfile";
        String path="/usr/gy/developer/img";
        return productService.upload(file,path); //逻辑视图     前缀+逻辑视图+后缀  --》 /WEB-INF/jsp/upload.jsp
    }*/

    public ServerResponse upload2(@RequestParam("uploadfile") MultipartFile uploadfile){
        if (uploadfile == null||uploadfile.getOriginalFilename().equals(""))
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"");
        //获得上传的图片名称
        String oldFileName = uploadfile.getOriginalFilename();
        //获得文件扩展名
        String extendName = oldFileName.substring(oldFileName.lastIndexOf("."));
        //生成新的文件名
        String newFilename = UUID.randomUUID().toString()+extendName;
        File mkdir = new File("F:/upload");
        if (!mkdir.exists()){
            mkdir.mkdirs();
        }

        File newFile = new File(mkdir,newFilename);

        try {
            //保存到新文件中
            uploadfile.transferTo(newFile);
            //http://localhost/filename
            ImageVO imageVO = new ImageVO(newFilename,imageHost+newFilename);
            return ServerResponse.serverResponseBySuccess(imageVO);
        }catch (IOException e){
            e.printStackTrace();
        }
        return ServerResponse.serverResponseByError();
    }



}