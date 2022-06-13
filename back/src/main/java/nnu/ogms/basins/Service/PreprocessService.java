package nnu.ogms.basins.Service;

import cn.hutool.core.io.file.FileNameUtil;
import nnu.ogms.basins.Utils.*;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;
import nnu.ogms.basins.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service

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
            GDALUtil.clip_cmd_cutline(DEMPath,String.valueOf(targetDEMPath),String.valueOf(shpPath));//裁剪
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
    public Object simulationUnitSoil(String srcFile, String projectSavePath, String projectName,String type, String soilParasSrcPath){
        try{
            StringBuilder landUseSoilTypePath = new StringBuilder(projectSavePath);
            landUseSoilTypePath.append(projectName).append("/data_prepare/spatial/").append(type).append(".tif");
            StringBuilder shpPath = new StringBuilder(projectSavePath);
            shpPath.append(projectName).append("/data_prepare/spatial/basin_scope.shp");
            StringBuilder resampleFile = new StringBuilder(projectSavePath);
            resampleFile.append(projectName).append("/data_prepare/spatial/dem90m.tif");
            GDALUtil.resample_clip_cmd(srcFile,String.valueOf(landUseSoilTypePath),String.valueOf(shpPath),String.valueOf(resampleFile));
            // 裁剪soil参数文件
            List<File> soilParasPathList = FileUtils.getAllFile(new File(soilParasSrcPath));
            StringBuilder soilParaDstPath = new StringBuilder(projectSavePath);
            soilParaDstPath.append(projectName).append("/data_prepare/spatial/soil_paras/");
            File soilParaFile =new File(String.valueOf(soilParaDstPath));
            if  (!soilParaFile.exists() && !soilParaFile.isDirectory()) {soilParaFile.mkdir();}
            StringBuilder soilIniSrc = new StringBuilder(soilParasSrcPath);
            soilIniSrc.append("/SOL.ini");
            StringBuilder soilIniDst = new StringBuilder(soilParaDstPath);
            soilIniDst.append("/SOL.ini");
            FileUtils.copyFileUsingFileChannels(String.valueOf(soilIniSrc), String.valueOf(soilIniDst));
            for (File soilPara : soilParasPathList) {
                soilPara.toString();
                String ParaName = FileNameUtil.getName(soilPara);
                StringBuilder soilParaDstFile = new StringBuilder(soilParaDstPath);
                soilParaDstFile.append(ParaName);
                GDALUtil.resample_clip_cmd(soilPara.toString(),String.valueOf(soilParaDstFile),String.valueOf(shpPath), String.valueOf(resampleFile));
            }
            return ResponseMessage.success(null);
        }catch(Exception e){
            throw new GeneralException(ErrorEnum.FILE_SIMULATION_UNIT_ERROR);
        }
    }
    public Object simulationUnitLanduse(String srcFile, String projectSavePath, String projectName,String type, String landuseLookupTablePath){
        try{
            StringBuilder landUseSoilTypePath = new StringBuilder(projectSavePath);
            landUseSoilTypePath.append(projectName).append("/data_prepare/spatial/").append(type).append(".tif");
            StringBuilder shpPath = new StringBuilder(projectSavePath);
            shpPath.append(projectName).append("/data_prepare/spatial/basin_scope.shp");
            StringBuilder resampleFile = new StringBuilder(projectSavePath);
            resampleFile.append(projectName).append("/data_prepare/spatial/dem90m.tif");
            GDALUtil.resample_clip_cmd(srcFile,String.valueOf(landUseSoilTypePath),String.valueOf(shpPath),String.valueOf(resampleFile));
            // copy landuse lookup table
            StringBuilder landUsePreparePath = new StringBuilder(projectSavePath);
            landUsePreparePath.append(projectName).append("/data_prepare/lookup/landcover_initial_parameters.csv");
            FileUtils.copyFileUsingFileChannels(landuseLookupTablePath, String.valueOf(landUsePreparePath));

            return ResponseMessage.success(null);
        }catch(Exception e){
            throw new GeneralException(ErrorEnum.FILE_SIMULATION_UNIT_ERROR);
        }
    }

    public Object resampleClipLandUseSoilType(String srcFile, String projectSavePath, String projectName,String type){
        try{
            StringBuilder landUseSoilTypePath = new StringBuilder(projectSavePath);
            landUseSoilTypePath.append(projectName).append("/data_prepare/spatial/").append(type).append(".tif");
            StringBuilder shpPath = new StringBuilder(projectSavePath);
            shpPath.append(projectName).append("/data_prepare/spatial/basin_scope.shp");
            StringBuilder resampleFile = new StringBuilder(projectSavePath);
            resampleFile.append(projectName).append("/data_prepare/spatial/dem90m.tif");
            GDALUtil.resample_clip_cmd(srcFile,String.valueOf(landUseSoilTypePath),String.valueOf(shpPath),String.valueOf(resampleFile));
            return ResponseMessage.success(null);
        }catch(Exception e){
            throw new GeneralException(ErrorEnum.FILE_SIMULATION_UNIT_ERROR);
        }
    }

}
