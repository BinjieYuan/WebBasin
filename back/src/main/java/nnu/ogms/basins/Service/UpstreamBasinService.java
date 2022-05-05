package nnu.ogms.basins.Service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import nnu.ogms.basins.Entity.SubBasinsScopeEntity;
import nnu.ogms.basins.Entity.ZipFileRequest;
import nnu.ogms.basins.Utils.RunExe;
import nnu.ogms.basins.Utils.Shp2Json;
import nnu.ogms.basins.Utils.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UpstreamBasinService {
    private final MongoTemplate mongoTemplate;


    private static String basinPythonRoot;

    @Value("${basin.pythonRoot}")
    public void setBasinPythonRoot(String basinPythonRoot) {
        UpstreamBasinService.basinPythonRoot = basinPythonRoot;
    }

    private static String basinPythonScript;

    @Value("${basin.pythonScript}")
    public void setBasinPythonScript(String basinPythonScript) {
        UpstreamBasinService.basinPythonScript = basinPythonScript;
    }

    private static String basinProjectRoot;

    @Value("${basin.projectRoot}")
    public void setBasinProjectRoot(String basinProjectRoot) {
        UpstreamBasinService.basinProjectRoot = basinProjectRoot;
    }

    private static String outputDirectory;
    @Value("${basin.outputDirectory}")
    public void setOutputDirectory(String outputDirectory) {
        UpstreamBasinService.outputDirectory = outputDirectory;
    }

    @Autowired
    public UpstreamBasinService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }


    public Object queryUpstreamBasinByLoc(Integer level, double lon, double lat, Boolean dir, Boolean upa, Boolean elv, String timeStamp){
        try {
            double[] point = new double[]{lon, lat};
            String collection = "UpstreamBasin_lv" + level;

            DBObject queryCmd = new BasicDBObject();
            queryCmd.put("features.geometry", new BasicDBObject("$geoIntersects",
                    new BasicDBObject("$geometry",
                            new BasicDBObject("type", "Point")
                                    .append("coordinates", point))));
            Query query = new BasicQuery(queryCmd.toString());
            SubBasinsScopeEntity sbse = mongoTemplate.findOne(query, SubBasinsScopeEntity.class, collection);

            String space = " ";
//            String featureId = "";
            assert sbse != null;
            Integer featureId = (Integer) sbse.getFeatures().get(0).getProperties().get("PFAF_ID");
//            long nowDate = DateUtil.current();

            String cmd = basinPythonRoot + space +basinPythonScript + "main.py " + lon + space + lat + space + featureId + space + basinProjectRoot + " -directory " + outputDirectory + " -id " + timeStamp;
            StringBuilder params = new StringBuilder();
            if (dir) {
                params.append(" -dir");
            }
            if (upa) {
                params.append(" -upa");
            }
            if (elv) {
                params.append(" -elv");
            }
            String cmdRun = cmd + params;
            RunExe.openExe(cmdRun);
            // shp to geojson
            String shpFilePath = outputDirectory + timeStamp + "_bound.shp";
            String jsonFilePath = outputDirectory + timeStamp + "_bound.geojson";
            String JsonString = Shp2Json.transformShpToGeoJson(shpFilePath, jsonFilePath);
            String nowTime = DateUtil.now();
            System.out.println(nowTime + "-qureyUpstreamBasin " + cmdRun);
            return JsonString;
        }catch (Exception e){
            return e;
        }
    }

    public String boundaryDownload(String fileName) {
        List<ZipFileRequest> fileNameList = new ArrayList<>();
        Map<String, Object> reMap = new HashMap<>();

        String shpFile = outputDirectory + fileName + ".shp";
        String dbfFile = outputDirectory + fileName + ".dbf";
        String prjFile = outputDirectory + fileName + ".prj";
        String shxFile = outputDirectory + fileName + ".shx";
        fileNameList.add(new ZipFileRequest(shpFile, fileName + ".shp"));
        fileNameList.add(new ZipFileRequest(dbfFile, fileName + ".dbf"));
        fileNameList.add(new ZipFileRequest(prjFile, fileName + ".prj"));
        fileNameList.add(new ZipFileRequest(shxFile, fileName + ".shx"));
        try{
            if(fileNameList.size() <= 0){
                reMap.put("msg", "文件路径为空");
            }
            ZipUtil.downZip(fileNameList, fileName);
            reMap.put("msg", "文件下载完成");
        }catch(Exception e){
            e.printStackTrace();
            reMap.put("msg", "下载文件失败,error:" + e.getMessage());
        }

        String resStr = JSON.toJSONString(reMap);
        return resStr;
    }

}
