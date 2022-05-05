package nnu.ogms.basins.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import nnu.ogms.basins.Entity.BasinsScopeEntity;
import nnu.ogms.basins.Entity.SubBasinsScopeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class BasinsScopeService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public BasinsScopeService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public Object queryScopeByLoc(double lon, double lat){
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
            return 0;
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
