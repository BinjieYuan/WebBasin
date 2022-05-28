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
}
