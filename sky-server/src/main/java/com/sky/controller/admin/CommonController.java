package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}", file);
        try {
            //上传时的原始文件名
            String oringinalName = file.getOriginalFilename();
            //截取原始文件名后缀
            String extention = null;
            if (oringinalName != null) {
                extention = oringinalName.substring(oringinalName.lastIndexOf("."));
            }
            //构造新文件名称
            String newName = UUID.randomUUID().toString() + extention;
            //文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), newName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败:{}",e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
