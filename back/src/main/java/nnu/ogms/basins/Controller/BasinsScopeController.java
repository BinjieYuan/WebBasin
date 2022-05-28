package nnu.ogms.basins.Controller;

import nnu.ogms.basins.Service.BasinsScopeService;
import nnu.ogms.basins.Service.UpstreamBasinService;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;
import nnu.ogms.basins.common.ResponseMessage;
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
        return ResponseMessage.success(scopeService.queryScopeByLoc(lon, lat));
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

    @GetMapping("/test")
    public ResponseMessage responseTest(@RequestParam int num){
        if (num < 0) {
            throw new GeneralException(ErrorEnum.QUERY_SCOPE_ERROR);
        }else {
            return ResponseMessage.success("your data");
        }
    }

    // 看了彬杰的后端代码，写了一些公共类，对代码规范性提了几点建议
}
