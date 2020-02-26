package com.just.book_appoint_system.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传到服务器
 */
@Configuration
@PropertySource(value = {"classpath:file1.properties"})
@Component
public class FileUpload  {

    @Value("${file.uploadFolder}")
    private String fold;

    @Value("${file.image.path}")
    private String path;


    public  String uploadFileByFiles(MultipartFile[] files,String key) throws Exception {
        String fileNameStr = "";
        for(int i = 0; i < files.length; i++){ //对所有文件依次获取
            // 取得上传文件
            if (files[i] != null) {
                // 取得当前上传文件的文件名称
                String myFileName = files[i].getOriginalFilename();
                // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                if (myFileName.trim() != "") {
                    String fileTyps = myFileName.substring(myFileName.lastIndexOf("."));
                    // String tempName="demo"+fileTyps;
                    String tempName = UUID.randomUUID().toString() + fileTyps;
                    // 创建文件夹
                    String folderPath = fold + path+folderName()+"/"+key+"/";
                    File fileFolder = new File(folderPath);
                    if (!fileFolder.exists() && !fileFolder.isDirectory()) {
                        fileFolder.mkdirs();
                    }
                    //创建文件
                    File uploadFile = new File(folderPath + tempName);
                    //上传文件
                    files[i].transferTo(uploadFile);
                    if(fileNameStr == ""){
                        fileNameStr ="/"+folderName()+"/"+key+"/"+tempName;
                    }
                    else{
                        fileNameStr = fileNameStr + ";/" +folderName()+"/"+key+"/"+ tempName;
                    }
                }
            }
        }
        return fileNameStr;
    }

    /**
     * 当前日期当文件夹名
     *
     * @return
     */
    public static String folderName() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(new Date());
        return str;
    }
}
