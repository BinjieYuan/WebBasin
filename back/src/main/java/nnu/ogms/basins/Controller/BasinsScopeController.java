package nnu.ogms.basins.Controller;

import nnu.ogms.basins.Service.BasinsScopeService;
import nnu.ogms.basins.Service.UpstreamBasinService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RestController
@RequestMapping("/basins")
public class BasinsScopeController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/queryScope",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public Object queryScope(@RequestParam("lon") double lon, @RequestParam("lat") double lat){
        BasinsScopeService scopeService = new BasinsScopeService(mongoTemplate);
        return scopeService.queryScopeByLoc(lon, lat);
    }

    @RequestMapping(value = "/querySubLevel/{level}",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public Object querySubLevel(@PathVariable Integer level,@RequestParam("lon") double lon, @RequestParam("lat") double lat){
        BasinsScopeService scopeService = new BasinsScopeService(mongoTemplate);
        return scopeService.querySubLevelByLoc(level, lon, lat);
    }

    @RequestMapping(value = "/queryUpstreamBasin/{level}",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public Object queryUpstreamBasin(@PathVariable Integer level,@RequestParam("lon") double lon, @RequestParam("lat") double lat,
                                     @RequestParam("dir") Boolean dir,@RequestParam("upa") Boolean upa,@RequestParam("elv") Boolean elv,
                                     @RequestParam("timeStamp") String timeStamp){
        UpstreamBasinService upstreamBasinService = new UpstreamBasinService(mongoTemplate);
        return upstreamBasinService.queryUpstreamBasinByLoc(level, lon, lat, dir, upa, elv,timeStamp);
    }
}
