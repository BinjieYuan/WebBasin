package nnu.ogms.basins.Utils;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeotoolsUtils {
    public static List ShpExtentUtil(String shpFilePath) {
        File file = new File(shpFilePath);
        List<Double> extentList = new ArrayList();
        Map<String, URL> map = new HashMap<String, URL>();
        try {
            map.put("url", file.toURI().toURL());
            DataStore dataStore = DataStoreFinder.getDataStore(map);
            // SimpleFeatureSource featureSource = dataStore.getFeatureSource(shpName); this works too
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
            SimpleFeatureCollection collection = featureSource.getFeatures();
            ReferencedEnvelope env = collection.getBounds();
            double left = env.getMinX();
            double right = env.getMaxX();
            double top = env.getMaxY();
            double bottom = env.getMinY();
            extentList.add(left);
            extentList.add(right);
            extentList.add(top);
            extentList.add(bottom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extentList;
    }

    /**
     *
     * @param shpFile shp路径
     * @param lat 坐标lat
     * @param lon 坐标lon
     */
    public static void transformPointToShp(String shpFile, String lat, String lon) {

//        File file = JFileDataStoreChooser.showOpenFile("csv", null);
//        if (file == null) {
//            return;
//        }

        /**
         * We use the DataUtilities class to create a FeatureType that will describe the data in our
         * shapefile.
         * See also the createFeatureType method below for another, more flexible approach.
         */
        try {
        final SimpleFeatureType TYPE =
                DataUtilities.createType(
                        "Location",
                        "the_geom:Point:srid=4326,"
                                + // <- the geometry attribute: Point type
                                "name:String,"
                                + // <- a String attribute
                                "number:Integer" // a number attribute
                );
//        System.out.println("TYPE:" + TYPE);

        /*
         * A list to collect features as we create them.
         */
        List<SimpleFeature> features = new ArrayList<>();

        /*
         * GeometryFactory will be used to create the geometry attribute of each feature,
         * using a Point object for the location.
         */
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

//        try () {
//            /* First line of the data file is the header */
//            String line = reader.readLine();
//            System.out.println("Header: " + line);
//
//            for (line = reader.readLine(); line != null; line = reader.readLine()) {
//                if (line.trim().length() > 0) { // skip blank lines
//                    String tokens[] = line.split("\\,");

        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        String name = "outlet";
        int number = Integer.parseInt("1");

        /* Longitude (= x coord) first ! */
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        featureBuilder.add(point);
        featureBuilder.add(name);
        featureBuilder.add(number);
        SimpleFeature feature = featureBuilder.buildFeature(null);
        features.add(feature);
//                }
//            }
//        }

        /*
         * Get an output file name and create the new shapefile
         */
        File newFile = new File(shpFile);

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", newFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore =
                (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        /**
         * TYPE is used as a template to describe the file contents
         */
        newDataStore.createSchema(TYPE);

        /**
         * Write the features to the shapefile
         */
        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
        /**
         * The Shapefile format has a couple limitations:
         * - "the_geom" is always first, and used for the geometry attribute name
         * - "the_geom" must be of type Point, MultiPoint, MuiltiLineString, MultiPolygon
         * - Attribute names are limited in length
         * - Not all data types are supported (example Timestamp represented as Date)
         *
         * Each data store has different limitations so check the resulting SimpleFeatureType.
         */

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            /**
             * SimpleFeatureStore has a method to add features from a
             * SimpleFeatureCollection object, so we use the ListFeatureCollection
             * class to wrap our list of features.
             */
            SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
            try {
                featureStore.addFeatures(collection);
            } catch (Exception problem) {
                problem.printStackTrace();
            }
        } else {
            System.out.println(typeName + " does not support read/write access");
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) {
//        String shpFile = "D:\\WEB\\basins\\test\\outlet.shp";
//        List extentList = new ArrayList();
//        try {
//            transformPointToShp(shpFile,"31.728","105.8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

