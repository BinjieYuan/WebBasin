package nnu.ogms.basins.Controller;

import nnu.ogms.basins.common.ResponseMessage;
import nnu.ogms.basins.service.GeoServerPublisher;
import nnu.ogms.basins.vo.PublishInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geoserver")
public class GeoServerContoller {

    @Autowired
    GeoServerPublisher geoServerPublisher;

    @PostMapping("/shapefile/publish")
    public ResponseMessage publishGeoserverMap(@RequestBody PublishInfoVo publishInfoVo){
        geoServerPublisher.publish(publishInfoVo);
        return ResponseMessage.success();
    }
}
