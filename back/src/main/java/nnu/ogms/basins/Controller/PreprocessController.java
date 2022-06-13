package nnu.ogms.basins.Controller;

import nnu.ogms.basins.Service.PreprocessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RestController
@RequestMapping("/basins")
public class PreprocessController {
    @Value("${file.project-save-path}")
    private String projectSavePath;
    @Value("${basin.DEMPath}")
    private String DEMPath;
    @Value("${basin.SoilType}")
    private String soilTypeDir;
    @Value("${basin.Landuse}")
    private String landUseDir;
    @Value("${basin.SoilParameters}")
    private String soilParaDir;
    @RequestMapping(value = "/preprocess/geojson2shp",method = RequestMethod.POST)
    public Object transformGeoJson2Shp(@RequestParam("projectName") String projectName){
        PreprocessService preprocessService = new PreprocessService();
        return preprocessService.basinScopeShp(projectSavePath,DEMPath,projectName);
    }
    @RequestMapping(value = "/preprocess/streamNetworkValue",method = RequestMethod.POST)
    public Object streamNetworkValueCommit(@RequestParam("projectName") String projectName,@RequestParam("streamNetValue") String streamNetValue){
        PreprocessService preprocessService = new PreprocessService();
        StringBuilder projectConfigFileSource = new StringBuilder(projectSavePath);
        projectConfigFileSource.append(projectName).append("/model_configs/preprocess.ini");
        return preprocessService.streamNetWorkValueDef(String.valueOf(projectConfigFileSource),streamNetValue);
    }
    @RequestMapping(value = "/preprocess/outletShp",method = RequestMethod.POST)
    public Object outletShp(@RequestParam("projectName") String projectName,@RequestParam("lat") String lat,@RequestParam("lon") String lon){
        PreprocessService preprocessService = new PreprocessService();
        StringBuilder projectConfigFileSource = new StringBuilder(projectSavePath);
        projectConfigFileSource.append(projectName).append("/data_prepare/spatial/outlet.shp");
        return preprocessService.drawOutletShp(String.valueOf(projectConfigFileSource),lon,lat);
    }
    @RequestMapping(value = "/preprocess/simulationUnitLanduse",method = RequestMethod.POST)
    public Object simulationUnitLanduse(@RequestParam("projectName") String projectName, @RequestParam("selectedData") String landUseTypeSelected){
        PreprocessService preprocessService = new PreprocessService();
        StringBuilder landUsePath = new StringBuilder(landUseDir);
        landUsePath.append(landUseTypeSelected).append("/tif_block/landuse_index.vrt");
        StringBuilder landUseLookupTablePath = new StringBuilder(landUseDir);
        landUseLookupTablePath.append(landUseTypeSelected).append("/tif_block/landcover_initial_parameters.csv");
        return preprocessService.simulationUnitLanduse(String.valueOf(landUsePath),projectSavePath,projectName,"landuse",String.valueOf(landUseLookupTablePath));
    }
    @RequestMapping(value = "/preprocess/simulationUnitSoiltype",method = RequestMethod.POST)
    public Object simulationUnitSoiltype(@RequestParam("projectName") String projectName, @RequestParam("selectedData") String soilTypeSelected,
                                            @RequestParam("soilParaSelectedData") String soilParaSelected){
        PreprocessService preprocessService = new PreprocessService();
        StringBuilder soilTypePath = new StringBuilder(soilTypeDir);
        soilTypePath.append(soilTypeSelected).append("/soiltype.tif");
        StringBuilder soilParasSrcPath = new StringBuilder(soilParaDir);
        soilParasSrcPath.append(soilParaSelected);
        return preprocessService.simulationUnitSoil(String.valueOf(soilTypePath),projectSavePath,projectName,"soiltype",String.valueOf(soilParasSrcPath));
    }
}
