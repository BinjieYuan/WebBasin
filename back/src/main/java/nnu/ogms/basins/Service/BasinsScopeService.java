package nnu.ogms.basins.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import nnu.ogms.basins.Entity.BasinsScopeEntity;
import nnu.ogms.basins.Entity.SubBasinsScopeEntity;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BasinsScopeService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public BasinsScopeService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public Object queryScopeByLoc(double lon, double lat){
        // 从前端拿到值以后，一般要先进行数据正确性的校验
        // 经度范围是0-180°，纬度范围是0-90°
        if (lon < 0.0 || lon > 180.0){
            throw new GeneralException(ErrorEnum.LONGITUDE_SCOPE_ERROR);
        }
        if (lat < 0.0 || lat > 90.0){
            throw new GeneralException(ErrorEnum.LATITUDE_SCOPE_ERROR);
        }
        try {
            double[] point = new double[]{lon, lat};

            DBObject queryCmd = new BasicDBObject();
            queryCmd.put("geometry", new BasicDBObject("$geoIntersects",
                    new BasicDBObject("$geometry",
                            new BasicDBObject("type", "Point")
                                    .append("coordinates", point))));
            Query query = new BasicQuery(queryCmd.toString());
            return mongoTemplate.findOne(query, BasinsScopeEntity.class);

        }catch (Exception e){
            // 1. 出错时一般要对异常信息进行描述，并抛给前端
            log.error("****时报错:",e);
            throw new GeneralException(ErrorEnum.QUERY_SCOPE_ERROR,"****出错，请检查****");
//            return 0;
        }
    }

    public Object querySubLevelByLoc(Integer level, double lon, double lat){
        try {
            double[] point = new double[]{lon, lat};
            String collection = "Basin_lv" + level;

            DBObject queryCmd = new BasicDBObject();
            queryCmd.put("features.geometry", new BasicDBObject("$geoIntersects",
                    new BasicDBObject("$geometry",
                            new BasicDBObject("type", "Point")
                                    .append("coordinates", point))));
            Query query = new BasicQuery(queryCmd.toString());
            SubBasinsScopeEntity sbse = mongoTemplate.findOne(query, SubBasinsScopeEntity.class, collection);
            System.out.println(query);
            return sbse;

        }catch (Exception e){
            return 0;
        }
    }

    public Object queryUpstreamBasinByLoc(Integer level, double lon, double lat){
        try {
            double[] point = new double[]{lon, lat};
            String collection = "Basin_lv" + level;

            DBObject queryCmd = new BasicDBObject();
            queryCmd.put("features.geometry", new BasicDBObject("$geoIntersects",
                    new BasicDBObject("$geometry",
                            new BasicDBObject("type", "Point")
                                    .append("coordinates", point))));
            Query query = new BasicQuery(queryCmd.toString());
            SubBasinsScopeEntity sbse = mongoTemplate.findOne(query, SubBasinsScopeEntity.class, collection);
            return sbse.getFeatures().get(0).getProperties().get("PFAF_ID");

        }catch (Exception e){
            return 0;
        }
    }


}
