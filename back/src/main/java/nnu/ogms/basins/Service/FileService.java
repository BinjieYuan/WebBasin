package nnu.ogms.basins.Service;


import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.ResponseMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    @Value("${file.save-path}")
    private String savePath;

    public Object upLoadFiles(MultipartFile[] files) {
        //设置支持最大上传的文件，这里是1024*1024*20=20M
        long MAX_SIZE = 20971520L;
        //获取要上传文件的名称
        try {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            //如果名称为空，返回一个文件名为空的错误
            if (StringUtils.isEmpty(fileName)) {
                return new ResponseMessage(ErrorEnum.UPLOAD_FILENAME_EMPTY.getHttpCode(), ErrorEnum.UPLOAD_FILENAME_EMPTY.getMessage(), ErrorEnum.UPLOAD_FILENAME_EMPTY.getCode(), null);
            }
            //如果文件超过最大值，返回超出可上传最大值的错误
            if (file.getSize() > MAX_SIZE) {
                return new ResponseMessage(ErrorEnum.FILE_MAX_SIZE.getHttpCode(), ErrorEnum.FILE_MAX_SIZE.getMessage(), ErrorEnum.FILE_MAX_SIZE.getCode(), null);
            }
            //获取到后缀名
            String suffixName = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : null;
            //文件的保存重新按照时间戳命名
            String newName = System.currentTimeMillis() + suffixName;
            File newFile = new File(savePath, newName);
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
            //文件写入
            file.transferTo(newFile);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseMessage.success(null);
    }
}
