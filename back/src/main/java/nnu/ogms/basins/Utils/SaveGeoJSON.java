package nnu.ogms.basins.Utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import nnu.ogms.basins.Entity.SubBasinsScopeEntity;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.alibaba.fastjson.JSON.toJSONString;
@Component
public class SaveGeoJSON {

    private static String projectSavePath;
    @Value("${file.project-save-path}")
    public void setProjectSavePath(String projectSavePath) {
        this.projectSavePath= projectSavePath;
    }
    public static void saveBasinScopeJson(String project,SubBasinsScopeEntity sbse){
        try{
            StringBuilder projectDataPrepare = new StringBuilder(projectSavePath);
            projectDataPrepare.append(project).append("/data_prepare").append("/spatial").append("/basin_scope.geojson");
            String jsonObject= toJSONString(sbse);
            FileWriter writer = new FileWriter(String.valueOf(projectDataPrepare));
            writer.write(jsonObject);
        }catch (Exception e) {
            throw new GeneralException(ErrorEnum.SAVE_GEOJSON_ERROR);
        }
    }
}
