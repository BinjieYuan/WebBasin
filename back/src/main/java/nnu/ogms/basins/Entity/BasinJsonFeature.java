package nnu.ogms.basins.Entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class BasinJsonFeature {
    private String type;
    private JSONObject properties;
    private JSONObject geometry;
}
