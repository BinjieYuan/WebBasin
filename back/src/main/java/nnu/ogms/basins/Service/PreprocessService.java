package nnu.ogms.basins.Service;

import nnu.ogms.basins.Utils.GDALUtil;
import nnu.ogms.basins.Utils.GeotoolsUtils;
import nnu.ogms.basins.Utils.IniFileUtil;
import nnu.ogms.basins.Utils.SpatialDataTransformUtil;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;
import nnu.ogms.basins.common.ResponseMessage;

import java.io.IOException;

public class PreprocessService {
    public Object basinScopeShp(String projectSavePath,String DEMPath, String projectName){
        try{
            StringBuilder geoJsonPath = new StringBuilder(projectSavePath);
            geoJsonPath.append(projectName).append("/data_prepare/spatial/basin_scope.geojson");
            StringBuilder shpPath = new StringBuilder(projectSavePath);
            shpPath.append(projectName).append("/data_prepare/spatial/basin_scope.shp");
            SpatialDataTransformUtil.transformGeoJsonToShp(String.valueOf(geoJsonPath),String.valueOf(shpPath));//geojson转shp
            StringBuilder targetDEMPath = new StringBuilder(projectSavePath);
            targetDEMPath.append(projectName).append("/data_prepare/spatial/dem90m.tif");
            GDALUtil.clip(DEMPath,String.valueOf(targetDEMPath),String.valueOf(shpPath));//裁剪
            return ResponseMessage.success(null);
        }catch(Exception e){
            throw new GeneralException(ErrorEnum.GEOJSON_TO_SHP_ERROR);
        }
    }
    public Object streamNetWorkValueDef(String configFilePath,String streamNetWorkValue){
        try {
            IniFileUtil.writeCfgValue(configFilePath, "OPTIONAL_PARAMETERS", "D8AccThreshold", streamNetWorkValue);
            return ResponseMessage.success(null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneralException(ErrorEnum.FILE_CONFIG_WRITE_ERROR,"修改config文件失败");
        }
    }
    public Object drawOutletShp(String shpFilePath, String lon, String lat){
        try{
            GeotoolsUtils.transformPointToShp(shpFilePath,lat,lon);
            return ResponseMessage.success(null);
        }catch(Exception e){
            throw new GeneralException(ErrorEnum.FILE_POINT_SHP_ERROR);
        }
    }
}
