package nnu.ogms.basins.Service;

import cn.hutool.core.io.FileUtil;
import nnu.ogms.basins.Utils.FileUtils;
import nnu.ogms.basins.Utils.IniFileUtil;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;
import nnu.ogms.basins.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProjectService {
    @Value("${file.project-save-path}")
    private String projectSavePath;
    @Value("${basin.configFilesPath}")
    private String configFilesPath;

    public Object namedProject(String projectName){
        StringBuilder projectPath = new StringBuilder();
        projectPath.append(projectSavePath).append(projectName);
        File file =new File(String.valueOf(projectPath));
        //如果文件夹不存在则创建
        if  (!file.exists()  && !file.isDirectory())
        {
            file.mkdir();
            String fileArray[] = {"/data_prepare/climate", "/data_prepare/lookup", "/data_prepare/observed",
                    "/data_prepare/scenario", "/data_prepare/spatial", "/model_configs","/longterm_model",
                    "/workspace"};
            for (String pathName:fileArray) {
                StringBuilder projectSubFile = new StringBuilder();
                projectSubFile.append(projectPath).append(pathName);
                FileUtil.mkdir(String.valueOf(projectSubFile));
            }
            // copy config files
            String configFileArray[] = {"preprocess.ini", "postprocess.ini", "runmodel.ini",
                    "calibration.ini", "scenario_analysis.ini", "sensitivity_analysis.ini"};
            for (String configName:configFileArray) {
                StringBuilder projectConfigFileSource = new StringBuilder();
                projectConfigFileSource.append(configFilesPath).append(configName);
                StringBuilder projectConfigFileTarget = new StringBuilder();
                projectConfigFileTarget.append(projectPath).append("/model_configs/").append(configName);
                FileUtils.copyFileUsingFileChannels(String.valueOf(projectConfigFileSource), String.valueOf(projectConfigFileTarget));
            }
            StringBuilder projectPreConfigFile = new StringBuilder();
            projectPreConfigFile.append(projectPath).append("/model_configs/preprocess.ini");
            writeConfigFile(String.valueOf(projectPath),String.valueOf(projectPreConfigFile), projectName);
        } else
        {
            return new ResponseMessage(ErrorEnum.NEW_PROJECT_PATH_EXIST.getHttpCode(), ErrorEnum.NEW_PROJECT_PATH_EXIST.getMessage(), ErrorEnum.NEW_PROJECT_PATH_EXIST.getCode(), null);
        }
        return ResponseMessage.success(null);
    }
    public static void writeConfigFile(String projectPath, String configFilePath, String projectName){
        try {
            String varArray[] = {"BASE_DATA_DIR", "CLIMATE_DATA_DIR", "SPATIAL_DATA_DIR",
                    "MEASUREMENT_DATA_DIR", "BMP_DATA_DIR", "MODEL_DIR","TXT_DB_DIR","WORKING_DIR"};
//            List<String> varList = Arrays.asList(varArray);
            String valueArray[] = {"", "/data_prepare/climate", "/data_prepare/spatial",
                    "/data_prepare/observed", "/data_prepare/scenario", "/longterm_model",
                    "/data_prepare/lookup","/workspace"};
            for (int i=0;i<valueArray.length;i++) {
                StringBuilder projectSubFile = new StringBuilder();
                projectSubFile.append(projectPath).append(valueArray[i]);
                IniFileUtil.writeCfgValue(configFilePath, "PATH", varArray[i], String.valueOf(projectSubFile));
            }
            IniFileUtil.writeCfgValue(configFilePath, "MONGODB", "ClimateDBName", projectName+"_HydroClimate");
            IniFileUtil.writeCfgValue(configFilePath, "MONGODB", "BMPScenarioDBName", projectName+"_Scenario");
            IniFileUtil.writeCfgValue(configFilePath, "MONGODB", "SpatialDBName", projectName+"_longterm_model");
        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneralException(ErrorEnum.FILE_CONFIG_WRITE_ERROR,"修改config文件失败");
        }
    }
}
