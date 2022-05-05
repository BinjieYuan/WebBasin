package nnu.ogms.basins.Entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class SubBasinsScopeEntity {

    private String type;

    private JSONObject crs;

    private List<BasinJsonFeature> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getCrs() {
        return crs;
    }

    public void setCrs(JSONObject crs) {
        this.crs = crs;
    }

    public List<BasinJsonFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<BasinJsonFeature> features) {
        this.features = features;
    }

}
