package nnu.ogms.basins.Service;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSGeoTIFFDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSShapefileDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import lombok.extern.slf4j.Slf4j;
import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;
import nnu.ogms.basins.vo.PublishInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

@Service
@Slf4j
public class GeoServerPublisher {

    private static String DATA_DIR;
    private static String STYLE_DIR;
    private static Resource resource;

    static {
        try {
            DATA_DIR = ResourceUtils.getURL("classpath:").getPath() + "\\data\\";
            STYLE_DIR = ResourceUtils.getURL("classpath:").getPath() + "\\style\\";

        } catch (FileNotFoundException e) {
            log.error("获取用户目录失败:{}",e);
        }
    }


    public void publish(PublishInfoVo publishInfoVo) {

//        File file = new File(DATA_DIR + publishInfoVo.getFileName());
        System.out.println(publishInfoVo.getFileName());
        File file = new File(publishInfoVo.getFileName());

        if (!file.exists())
        {
            throw new GeneralException(ErrorEnum.FILE_NOT_EXIST_ERROR,file.getName());
        }
        // geoServer信息
        String url = "http://223.2.40.37:7070/geoserver";
        String user = "admin";
        String password = "gis321";

        // 连接geoServer
        GeoServerRESTManager geoServerRESTManager = null;
        try {
            geoServerRESTManager = new GeoServerRESTManager(new URL(url), user, password);
        } catch (Exception e) {
            log.error("failed to connect GeoServer,reason is:{}",e);
        }

        // shp读写和发布
        assert geoServerRESTManager != null;
        GeoServerRESTReader restReader = geoServerRESTManager.getReader();
        GeoServerRESTPublisher restPublisher = geoServerRESTManager.getPublisher();

        // 工作区
        String workSpace = publishInfoVo.getWorkSpace();
        // 存在相应的工作区
        if (!restReader.existsWorkspace(workSpace)) {
            log.info("create workspace_name: {}" + workSpace);
            restPublisher.createWorkspace(workSpace);
        }else {
            log.info("workspace {} already exists." + publishInfoVo.getWorkSpace());
        }
        if (publishInfoVo.getDataType() == 1){
            // 发布矢量图层
            publishShape(geoServerRESTManager,publishInfoVo,file);
        }else if (publishInfoVo.getDataType() == 2){
            // 发布栅格图层
            publishTiff(geoServerRESTManager,publishInfoVo,file);
        }

        // style样式
        String styleName = publishInfoVo.getStyleName();
        // 如果未指定style 或 未发布指定的style，则使用默认style
        if (StringUtils.isEmpty(styleName) || !restReader.existsStyle(workSpace, styleName)) {
            // 如果没有传style，就选择默认style
            String styleFilePath = STYLE_DIR + "mt_city_cite.sld";
            File styleFile = new File(styleFilePath);
            restPublisher.publishStyleInWorkspace(workSpace, styleFile, styleName);
        }

    }

    private void publishShape(GeoServerRESTManager geoServerRESTManager, PublishInfoVo publishInfoVo, File file){
        if (!geoServerRESTManager.getReader().existsDatastore(publishInfoVo.getWorkSpace(), publishInfoVo.getDataSetName())) {

            //创建shape文件存储
            try {
                //shp文件所在的位置
                String urlDataStorePath = file.getPath();
                // 数据存储需要的文件
                String shpFilePath = String.format("file://%s", urlDataStorePath);
                URL urlShapeFile = new URL(shpFilePath);
                // 创建矢量数据集
                GSShapefileDatastoreEncoder datastoreEncoder = new GSShapefileDatastoreEncoder(publishInfoVo.getDataSetName(), urlShapeFile);
                datastoreEncoder.setCharset(Charset.forName("GBK"));
                geoServerRESTManager.getStoreManager().create(publishInfoVo.getWorkSpace(), datastoreEncoder);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        // 图层layer
        String layerName = publishInfoVo.getLayerName();
        if (!geoServerRESTManager.getReader().existsLayer(publishInfoVo.getWorkSpace(), layerName)) {
            try {
                GSFeatureTypeEncoder gsFeatureTypeEncoder = new GSFeatureTypeEncoder();
                gsFeatureTypeEncoder.setTitle(layerName);
                gsFeatureTypeEncoder.setName(layerName);
                gsFeatureTypeEncoder.setSRS(GeoServerRESTPublisher.DEFAULT_CRS);

                GSLayerEncoder gsLayerEncoder = new GSLayerEncoder();
//                gsLayerEncoder.addStyle(styleSld);

                boolean layer = geoServerRESTManager.getPublisher().publishDBLayer(publishInfoVo.getWorkSpace(), publishInfoVo.getDataSetName(), gsFeatureTypeEncoder, gsLayerEncoder);
                log.info("publish layer:{}",layer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void publishTiff(GeoServerRESTManager geoServerRESTManager, PublishInfoVo publishInfoVo, File file) {

        if (!geoServerRESTManager.getReader().existsDatastore(publishInfoVo.getWorkSpace(), publishInfoVo.getDataSetName())) {

            //创建shape文件存储
            try {
                String urlDataStorePath = file.getPath();
                // 数据存储需要的文件
                String shpFilePath = String.format("file://%s", urlDataStorePath);
                URL urlFile = new URL(shpFilePath);
                GSGeoTIFFDatastoreEncoder gsGeoTIFFDatastoreEncoder = new GSGeoTIFFDatastoreEncoder(publishInfoVo.getDataSetName());
                gsGeoTIFFDatastoreEncoder.setWorkspaceName(publishInfoVo.getWorkSpace());
                gsGeoTIFFDatastoreEncoder.setUrl(urlFile);
                boolean createStore = geoServerRESTManager.getStoreManager().create(publishInfoVo.getWorkSpace(), gsGeoTIFFDatastoreEncoder);
                log.info("create store status: {}" , createStore);
                boolean publish = geoServerRESTManager.getPublisher().publishGeoTIFF(publishInfoVo.getWorkSpace(), publishInfoVo.getDataSetName(), file);
                log.info("tiff publish status: {}" , publish);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            throw new GeneralException(ErrorEnum.DATASTORE_EXIST_ERROR);
        }

    }



//    private void publishRaster(GeoServerRESTManager geoServerRESTManager,PublishInfoVo publishInfoVo,File file){
//        //shp文件所在的位置
//        String urlDataStorePath = file.getPath();
//        // 数据存储需要的文件
//        String filePath = String.format("file://%s", urlDataStorePath);
//        // 创建栅格数据集
////        GSCoverageEncoder gsCoverageEncoder = new GSCoverageEncoder();
////        gsCoverageEncoder.setName(publishInfoVo.getLayerName());
////        gsCoverageEncoder.setTitle(publishInfoVo.getLayerName());
////        gsCoverageEncoder.setSRS("EPSG");
////        gsCoverageEncoder.setNativeFormat("GeoTIFF");
////        gsCoverageEncoder.addSupportedFormats("GEOTIFF");
////        gsCoverageEncoder.setNativeCRS("EPSG");
////        gsCoverageEncoder.setRequestSRS("EPSG");
////        gsCoverageEncoder.setResponseSRS("EPSG");
////        gsCoverageEncoder.addKeyword("WCS");
////        gsCoverageEncoder.setProjectionPolicy(GSResourceEncoder.ProjectionPolicy.REPROJECT_TO_DECLARED);
////        gsCoverageEncoder.setLatLonBoundingBox(-180, -90, 180, 90, GeoServerRESTPublisher.DEFAULT_CRS);
////        gsCoverageEncoder.addKeyword("geoTiff");
////        gsCoverageEncoder.addKeyword("WCS");
//        if (geoServerRESTManager.getReader().getDatastore(publishInfoVo.getWorkSpace(),publishInfoVo.getDataSetName()) == null) {
//            GSGeoTIFFDatastoreEncoder gsGeoTIFFDatastoreEncoder = new GSGeoTIFFDatastoreEncoder(publishInfoVo.getDataSetName());
//            gsGeoTIFFDatastoreEncoder.setWorkspaceName(publishInfoVo.getWorkSpace());
//            gsGeoTIFFDatastoreEncoder.setUrl(new URL(filePath));
//            boolean createStore = geoServerRESTManager.getStoreManager().create(workspace_name, gsGeoTIFFDatastoreEncoder);
//            System.out.println("create store (TIFF文件创建状态) : " + createStore);
//
//            boolean publish = geoServerRESTManager.getPublisher().publishGeoTIFF(workspace_name, store_name, new File(file_name));
//            System.out.println("publish (TIFF文件发布状态) : " + publish);
//
//        } else {
//            System.out.println("数据存储已经存在了,store:" + store_name);
//        }
//        geoServerRESTManager.getStoreManager().create(publishInfoVo.getWorkSpace(), gsCoverageEncoder);
//        boolean createStore = geoServerRESTManager.getPublisher().createCoverage(publishInfoVo.getWorkSpace(),publishInfoVo.getDataSetName(),gsCoverageEncoder);
//        System.out.println("Coverage store " + createStore);
//
//        boolean publish = false;
//        try {
//            publish = geoServerRESTManager.getPublisher().publishGeoTIFF(publishInfoVo.getWorkSpace(), publishInfoVo.getLayerName(),file);
//        } catch (FileNotFoundException e) {
//            throw new GeneralException(ErrorEnum.FILE_NOT_EXIST_ERROR,file.getName());
//        }
//        System.out.println("publish Coverage " + publish);
//
//    }
}
