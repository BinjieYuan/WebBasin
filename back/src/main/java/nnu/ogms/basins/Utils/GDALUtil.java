package nnu.ogms.basins.Utils;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.WarpOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

import java.util.List;
import java.util.Vector;

/**
 * notes：
 * 使用时如果出现错误 gcs.csv 错误。在"gdal.AllRegister();"之后添加：
 * "gdal.SetConfigOption("GDAL_DATA","G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal-data");"
 */
public class GDALUtil {
    /**
     * clip image by shapefile using gdal
     *
     * @param srcFile source image
     * @param dstFile target image
     * @param shpFile shapefile
     */
    public static void clip(String srcFile, String dstFile, String shpFile) {
        gdal.AllRegister();
        gdal.SetConfigOption("GDAL_DATA","G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal-data");
        // 读取要切的原图
        Dataset srcDs = gdal.Open(srcFile, gdalconstConstants.GA_ReadOnly);
        if (srcDs == null) {
            System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            return;
        }
        //获取四至范围，适当扩大一点
        List shpExtent = GeotoolsUtils.ShpExtentUtil(shpFile);
        double shpExtentLeft = Double.parseDouble(shpExtent.get(0).toString())-0.05;
        double shpExtentRight = Double.parseDouble(shpExtent.get(1).toString())+0.05;
        double shpExtentTop = Double.parseDouble(shpExtent.get(2).toString())+0.05;
        double shpExtentBottom = Double.parseDouble(shpExtent.get(3).toString())-0.05;
        // 构建warp参数
        Vector options = new Vector();
        options.add("-of");
        options.add("GTiff");
//        options.add("-cutline");//按照矢量图像线裁剪
//        options.add(shpFile);
//        options.add("-crop_to_cutline"); //裁剪后图层范围使用矢量图层大小
        options.add("-te");
        options.add(String.valueOf(shpExtentLeft));
        options.add(String.valueOf(shpExtentBottom));
        options.add(String.valueOf(shpExtentRight));
        options.add(String.valueOf(shpExtentTop));
        options.add("-dstnodata");
        options.add("-9999"); //nulldata的值
        options.add("-s_srs");
        options.add("EPSG:4326");
        options.add("-t_srs");
        options.add("EPSG:4326");

        WarpOptions wo = new WarpOptions(options);
        Dataset warp = gdal.Warp(dstFile, new Dataset[]{srcDs}, wo);
        // 获取切图的原点坐标信息
//        double[] warpTransform = warp.GetGeoTransform();
//        System.out.println("warp原点坐标   = " + warpTransform[0] + "," + warpTransform[3]);
//        System.out.println("warp像素坐标差 = " + warpTransform[1] + "," + warpTransform[5]);
        srcDs.delete();
        gdal.GDALDestroyDriverManager();
    }
//    public static void main(String[] args) {
//        gdal.AllRegister();
//        gdal.SetConfigOption("GDAL_DATA","G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal-data");
//        clip("K:\\Data\\DEM\\Adjusted_Elevation\\data\\DEM_merge.vrt",
//                "D:\\WEB\\basins\\test\\clip1.tif",
//                "D:\\WEB\\basins\\test\\shp\\1650454815812_bound.shp");
//        gdal.GDALDestroyDriverManager();
//    }

}
