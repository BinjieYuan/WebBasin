package nnu.ogms.basins.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


public class Shp2Json {
    public static String transformShpToGeoJson(String shpPath, String geojsonPath) {
        try {
            File file = new File(shpPath);
            FileDataStore myData = FileDataStoreFinder.getDataStore(file);
            // 设置解码方式
            ((ShapefileDataStore) myData).setCharset(StandardCharsets.UTF_8);
            SimpleFeatureSource source = myData.getFeatureSource();
            SimpleFeatureType schema = source.getSchema();
            Query query = new Query(schema.getTypeName());

            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(query);
            FeatureJSON fjson = new FeatureJSON(new GeometryJSON(8));
            File geojson = new File(geojsonPath);
            try (FeatureIterator<SimpleFeature> featureIterator = collection.features();
                 StringWriter writer = new StringWriter();
                 BufferedWriter buffer = new BufferedWriter(Files.newBufferedWriter(geojson.toPath(), StandardCharsets.UTF_8))) {
                writer.write("{\"type\":\"FeatureCollection\",\"crs\":");
                fjson.writeCRS(schema.getCoordinateReferenceSystem(), writer);
                writer.write(",");
                writer.write("\"features\":");
                writer.write("[");
                while (featureIterator.hasNext()) {
                    SimpleFeature feature = featureIterator.next();
                    fjson.writeFeature(feature, writer);
                    if (featureIterator.hasNext())
                        writer.write(",");
                }
                writer.write("]");
                writer.write("}");
                buffer.write(writer.toString());
                return writer.toString();
            } catch (IOException e) {
                return e.toString();
            }
        } catch (IOException e) {
            return e.toString();
        }
    }

//    public static void main(String[] args) {
//        String shpFile = "D:\\Lisflood-fd\\show\\basin.shp";
//        String jsonFile = "D:\\Lisflood-fd\\show\\basin_test1.geojson";
//        transformShpToGeoJson(shpFile, jsonFile);
//
////        try {
////            shp2json(shpFile, jsonFile);
////        } catch (MalformedURLException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//    }

}
