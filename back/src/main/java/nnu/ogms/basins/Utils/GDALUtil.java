package nnu.ogms.basins.Utils;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.WarpOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.geometry.Envelope2D;
import org.geotools.util.factory.Hints;

import java.awt.image.RenderedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public static void clip_cmd(String srcFile, String dstFile, String shpFile) {
        //获取四至范围
        List shpExtent = GeotoolsUtils.ShpExtentUtil(shpFile);
        StringBuilder extent = new StringBuilder();
        double shpExtentLeft = Double.parseDouble(shpExtent.get(0).toString());
        double shpExtentRight = Double.parseDouble(shpExtent.get(1).toString());
        double shpExtentTop = Double.parseDouble(shpExtent.get(2).toString());
        double shpExtentBottom = Double.parseDouble(shpExtent.get(3).toString());
        extent.append(shpExtentLeft+" ").append(shpExtentBottom+" ").append(shpExtentRight+" ").append(shpExtentTop);
//        System.out.println(shpExtent);
        // 构建warp参数
        StringBuilder cmd = new StringBuilder("G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal\\apps\\gdalwarp.exe");
        String[] cmd_options = new String[5];
        cmd_options[0] = " -of GTiff -overwrite -te ";
        cmd_options[1] = String.valueOf(extent);
        cmd_options[2] = " -dstnodata -9999 ";
        cmd_options[3] = srcFile;
        cmd_options[4] = dstFile;
        cmd.append(cmd_options[0]).append(cmd_options[1]).append(cmd_options[2]).append(cmd_options[3]).append(" ").append(cmd_options[4]);
        RunExe.openExe(String.valueOf(cmd));
    }
    /**
     * clip image by shapefile using gdal (use cutline)
     *
     * @param srcFile source image
     * @param dstFile target image
     * @param shpFile shapefile
     */
    public static void clip_cmd_cutline(String srcFile, String dstFile, String shpFile) {
        //获取四至范围
        List shpExtent = GeotoolsUtils.ShpExtentUtil(shpFile);
        StringBuilder extent = new StringBuilder();
        double shpExtentLeft = Double.parseDouble(shpExtent.get(0).toString());
        double shpExtentRight = Double.parseDouble(shpExtent.get(1).toString());
        double shpExtentTop = Double.parseDouble(shpExtent.get(2).toString());
        double shpExtentBottom = Double.parseDouble(shpExtent.get(3).toString());
        extent.append(shpExtentLeft+" ").append(shpExtentBottom+" ").append(shpExtentRight+" ").append(shpExtentTop);
//        System.out.println(shpExtent);
        // 构建warp参数
        StringBuilder cmd = new StringBuilder("G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal\\apps\\gdalwarp.exe");
        String[] cmd_options = new String[6];
        cmd_options[0] = " -of GTiff -overwrite -te ";
        cmd_options[1] = String.valueOf(extent);
        cmd_options[2] = " -dstnodata -9999 -cutline ";
        cmd_options[3] = shpFile;
        cmd_options[4] = srcFile;
        cmd_options[5] = dstFile;
        cmd.append(cmd_options[0]).append(cmd_options[1]).append(cmd_options[2]).append(cmd_options[3]).append(" ").append(cmd_options[4]).append(" ").append(cmd_options[5]);
        RunExe.openExe(String.valueOf(cmd));
    }
    /**
     * 针对landuse和soiltype的裁剪和重采样
     * @param srcFile 裁剪源文件
     * @param dstFile 裁剪和重采样目标文件
     * @param shpFile 裁剪边界
     * @param resampleFile 重采样单元大小文件
     */
    public static void resample_clip_cmd(String srcFile, String dstFile, String shpFile, String resampleFile){
        List shpExtent = GeotoolsUtils.ShpExtentUtil(shpFile);

        double shpExtentLeft = Double.parseDouble(shpExtent.get(0).toString());
        double shpExtentRight = Double.parseDouble(shpExtent.get(1).toString());
        double shpExtentTop = Double.parseDouble(shpExtent.get(2).toString());
        double shpExtentBottom = Double.parseDouble(shpExtent.get(3).toString());
        GridCoverage2D gridCoverage = GetGridCoverage2D(resampleFile);
        //获取像素大小
        RenderedImage sourceImage = gridCoverage.getRenderableImage(0, 1).createDefaultRendering();
        double rasterXSize = sourceImage.getWidth();
        double rasterYSize = sourceImage.getHeight();
        //地理瓦片长宽
        double geoTileWidth = (shpExtentRight - shpExtentLeft) / rasterXSize;
        double geoTileHeight = (shpExtentTop - shpExtentBottom) /rasterYSize;
        StringBuilder extent = new StringBuilder();
        extent.append(shpExtentLeft+" ").append(shpExtentBottom+" ").append(shpExtentRight+" ").append(shpExtentTop);
//        System.out.println(shpExtent);
        // 构建warp参数
        StringBuilder cmd = new StringBuilder("G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal\\apps\\gdalwarp.exe");
        String[] cmd_options = new String[5];
        cmd_options[0] = " -of GTiff -overwrite -te ";
        cmd_options[1] = String.valueOf(extent);
        cmd_options[2] = " -dstnodata 255 -tr ";
        cmd_options[3] = srcFile;
        cmd_options[4] = dstFile;
        cmd.append(cmd_options[0]).append(cmd_options[1]).append(cmd_options[2]).append(geoTileWidth).append(" ")
                .append(geoTileHeight).append(" ").append(cmd_options[3]).append(" ").append(cmd_options[4]);
        RunExe.openExe(String.valueOf(cmd));
    }

    private static GridCoverage2D GetGridCoverage2D(String geoTifPath) {
        File geoTifFile = new File(geoTifPath);
        if (!geoTifFile.exists()) {
            throw new RuntimeException("geoTif文件不存在");
        }
        AbstractGridFormat format = GridFormatFinder.findFormat(geoTifFile);

        if (!(format instanceof GeoTiffFormat)) {
            throw new RuntimeException(String.format("传入文件\"%s\"不是geoTif格式文件", geoTifPath));
        }
        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);

        AbstractGridCoverage2DReader geoTifReader = format.getReader(geoTifFile, hints);
        try {
            return geoTifReader.read(null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
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
        double[] gt = new double[6];
        srcDs.GetGeoTransform(gt);
        //获取四至范围，适当扩大一点
        List shpExtent = GeotoolsUtils.ShpExtentUtil(shpFile);
        StringBuilder extent = new StringBuilder();
        double shpExtentLeft = Double.parseDouble(shpExtent.get(0).toString())-(gt[1]*10);
        double shpExtentRight = Double.parseDouble(shpExtent.get(1).toString())+(gt[1]*10);
        double shpExtentTop = Double.parseDouble(shpExtent.get(2).toString())+(gt[1]*10);
        double shpExtentBottom = Double.parseDouble(shpExtent.get(3).toString())-(gt[1]*10);
        extent.append(shpExtentLeft+" ").append(shpExtentBottom+" ").append(shpExtentRight+" ").append(shpExtentTop);
//        System.out.println(shpExtent);
        // 构建warp参数
        String[] cmd = new String[7];
        cmd[0] = "gdalwarp";
        cmd[1] = "-of GTiff";
        cmd[2] = "-te";
        cmd[3] = String.valueOf(extent);
        cmd[4] = "-dstnodata -9999";
        cmd[5] = "-s_srs EPSG:4326";
        cmd[6] = "-t_srs EPSG:4326";
        cmd[7] = srcFile;
        cmd[8] = dstFile;
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
        System.out.println(options);
        WarpOptions wo = new WarpOptions(options);
        Dataset warp = gdal.Warp(dstFile, new Dataset[]{srcDs}, wo);
        // 获取切图的原点坐标信息
//        double[] warpTransform = warp.GetGeoTransform();
//        System.out.println("warp原点坐标   = " + warpTransform[0] + "," + warpTransform[3]);
//        System.out.println("warp像素坐标差 = " + warpTransform[1] + "," + warpTransform[5]);
        srcDs.delete();
        gdal.GDALDestroyDriverManager();
    }

    /**
     * 针对landuse和soiltype的裁剪和重采样
     * @param srcFile 裁剪源文件
     * @param dstFile 裁剪和重采样目标文件
     * @param shpFile 裁剪边界
     * @param resampleFile 重采样单元大小文件
     */
    public static void resample_clip(String srcFile, String dstFile, String shpFile, String resampleFile){
        gdal.AllRegister();
        gdal.SetConfigOption("GDAL_DATA","G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal-data");
        // 读取要切的原图
        Dataset srcDs = gdal.Open(srcFile, gdalconstConstants.GA_ReadOnly);
        if (srcDs == null) {
            System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            return;
        }
        Dataset resampleData = gdal.Open(resampleFile, gdalconstConstants.GA_ReadOnly);
        double[] gt = new double[6];
        resampleData.GetGeoTransform(gt);
        //获取四至范围，适当扩大一点
        List shpExtent = GeotoolsUtils.ShpExtentUtil(shpFile);
        double shpExtentLeft = Double.parseDouble(shpExtent.get(0).toString())-(gt[1]*10);
        double shpExtentRight = Double.parseDouble(shpExtent.get(1).toString())+(gt[1]*10);
        double shpExtentTop = Double.parseDouble(shpExtent.get(2).toString())+(gt[1]*10);
        double shpExtentBottom = Double.parseDouble(shpExtent.get(3).toString())-(gt[1]*10);
//        System.out.println(shpExtent);
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
        options.add("255"); //nulldata的值
        options.add("-s_srs");
        options.add("EPSG:4326");
        options.add("-t_srs");
        options.add("EPSG:4326");
        options.add("-tr");
        options.add(String.valueOf(gt[1]));
        options.add(String.valueOf(-gt[5]));
        System.out.println(options);
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
////        gdal.AllRegister();
////        gdal.SetConfigOption("GDAL_DATA","G:\\ProgramFiles(x86)\\GDAL-2.4.0\\bin\\gdal-data");
////        clip_cmd("K:\\Data\\DEM\\Adjusted_Elevation\\data\\DEM_merge.vrt",
////                "D:\\WEB\\basins\\test\\clip_dem.tif",
////                "D:\\WEB\\basins\\test\\shp\\basin.shp");
//        resample_clip_cmd("K:\\Data\\soiltype\\ChinaSoilType1km\\soiltype_1km_wgs84.tif",
//                "D:\\WEB\\basins\\test\\clip_soil1.tif",
//                "D:\\WEB\\basins\\test\\shp\\basin.shp",
//                "D:\\WEB\\basins\\test\\shp\\clip_dem.tif");
////        gdal.GDALDestroyDriverManager();
//    }

}
