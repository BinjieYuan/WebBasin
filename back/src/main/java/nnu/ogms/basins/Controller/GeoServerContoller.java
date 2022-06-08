package nnu.ogms.basins.Controller;

import nnu.ogms.basins.Service.GeoServerPublisher;
import nnu.ogms.basins.common.ResponseMessage;
import nnu.ogms.basins.vo.PublishInfoListVo;
import nnu.ogms.basins.vo.PublishInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/geoserver")
public class GeoServerContoller {

    @Autowired
    GeoServerPublisher geoServerPublisher;

    /**
     * 发布单一图层
     * @param publishInfoVo
     * @return
     */
    @PostMapping("/publish")
    public ResponseMessage publishGeoserverMap(@RequestBody PublishInfoVo publishInfoVo){
        geoServerPublisher.publish(publishInfoVo);
        return ResponseMessage.success();
    }

    /**
     * 发布图层组
     * @param publishInfoVoList
     * @return
     */
    @PostMapping("/publish/layers")
    public ResponseMessage publishGeoserverMap(@RequestBody PublishInfoListVo publishInfoVoList){
        geoServerPublisher.publishLayerGroup(publishInfoVoList);
        return ResponseMessage.success();
    }

    @GetMapping("/publish/layers/test")
    public ResponseMessage publishTest(){
        geoServerPublisher.publishLayersTest();
        return ResponseMessage.success();
    }
}
