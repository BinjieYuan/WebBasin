package nnu.ogms.basins.Controller;

import com.alibaba.fastjson.JSON;
import nnu.ogms.basins.Service.UpstreamBasinService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;



@RestController
public class UpstreamDownloadController {

    @Resource
    private MongoTemplate mongoTemplate;

    private static String outputDirectory;
    @Value("${basin.outputDirectory}")
    public void setOutputDirectory(String outputDirectory) {
        UpstreamDownloadController.outputDirectory = outputDirectory;
    }
    @RequestMapping(value = "/downloadUpstreamBoundary", method = RequestMethod.GET)
    public String downloadZipTemplate(@RequestParam("fileName") String fileName){
        UpstreamBasinService boundaryDownload = new UpstreamBasinService(mongoTemplate);
        return boundaryDownload.boundaryDownload(fileName);
    }

    @RequestMapping(value = "/downloadUpstream", method = RequestMethod.GET)
    public String downloadTemplate(HttpServletResponse response,@RequestParam("fileName") String fileName){
        Map<String, Object> reMap = new HashMap<>();
        String filePath = outputDirectory + fileName;

        File file = new File(filePath);
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null; //输出流
        try {
            //判断文件是否存在
            if (file.exists()) {
                //设置返回文件信息
                response.reset();
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("utf-8");
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                while(bis.read(buffer) != -1){
                    os.write(buffer);
                    reMap.put("msg", "下载附件成功");
                }
            }else {
                reMap.put("msg", "下载附件失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reMap.put("msg", "下载附件失败,error:" + e.getMessage());
        } finally {
            try {
                if(bis != null) {
                    bis.close();
                }
                if(os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String resStr = JSON.toJSONString(reMap);
        return resStr;
    }
}
