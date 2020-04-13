package com.zq.service;

import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 86132 on 2020/04/01.
 * 文件上传
 */
@Component
public class FileService {

    public List<String> fileUpload(MultipartFile file) throws BusinessException {
        if (file.isEmpty()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"图片不能为空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "D:\\img\\"; // 上传后的路径
//        String filePath = "\\www\\wwwroot\\zqain.xyz\\img\\"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = fileName;
        List<String> fileList = new ArrayList<>();
        fileList.add(filename);
        return fileList;
    }

    public void deleteFile(String pathname) throws BusinessException {
        pathname = pathname.substring(pathname.lastIndexOf("/")+1);
        File file = new File("D:\\img\\"+pathname);
//        File file = new File("\\www\\wwwroot\\zqain.xyz\\img\\"+pathname);
        if (!file.exists()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (!"headImg.jpg".equals(pathname)){
            file.delete();
        }
        return;
    }
}
