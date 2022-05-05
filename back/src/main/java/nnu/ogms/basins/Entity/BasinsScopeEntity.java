package nnu.ogms.basins.Entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BasinsScopeEntity {

    private String type;

    private JSONObject properties;

    private JSONObject geometry;

    public String getType() {
        return type;
    }

    public void setGeometry(JSONObject geometry) {
        this.geometry = geometry;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }

    public JSONObject getGeometry() {
        return geometry;
    }

    public JSONObject getProperties() {
        return properties;
    }
}
