package nnu.ogms.basins.Controller;

import nnu.ogms.basins.Service.FileService;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    @Autowired
    private FileService fileService;

    //localhost:8083/api/upload,这里建议使用postmen上传文件，格式form-data
    //文件上传接口
    @Transactional
    @PostMapping("/upload")
    public Object upLoadFiles(@RequestParam("file") MultipartFile[] multipartFile) {
        //如果文件为空，直接返回错误信息
        for (MultipartFile file : multipartFile) {
            if (file.isEmpty()) {
                return new ResponseMessage(ErrorEnum.FILE_MAX_SIZE.getHttpCode(),ErrorEnum.FILE_MAX_SIZE.getMessage(),ErrorEnum.FILE_MAX_SIZE.getCode(),null);
            }
        }
        //否则调用service上传文件
        return fileService.upLoadFiles(multipartFile);
    }
}
