package nnu.ogms.basins.Utils;

import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtils {
    /**
     * 复制文件
     * @param sourcePath 复制文件源
     * @param destPath 复制文件目标
     */
    public static void copyFileUsingFileChannels(String sourcePath, String destPath) {
        File source = new File(sourcePath);
        File dest = new File(destPath);
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch(IOException e){
            e.printStackTrace();
            throw new GeneralException(ErrorEnum.FILE_COPY_ERROR,"文件复制失败");
        }finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<File> getAllFile(File dirFile){
        // 如果文件夹不存在或着不是文件夹，则返回 null
        if(Objects.isNull(dirFile) || !dirFile.exists() || dirFile.isFile())
            return null;

        File[] childrenFiles =  dirFile.listFiles();
        if(Objects.isNull(childrenFiles) || childrenFiles.length == 0)
            return null;

        List<File> files = new ArrayList<>();
        for(File childFile : childrenFiles) {

            // 如果时文件，直接添加到结果集合
            if(childFile.isFile()) {
                files.add(childFile);
            }else {
                // 如果是文件夹，则将其内部文件添加进结果集合
                List<File> cFiles =  getAllFile(childFile);
                if(Objects.isNull(cFiles) || cFiles.isEmpty()) continue;
                files.addAll(cFiles);
            }
        }
        return files;
    }

    public static void main(String[] args) {
        String dir = "K:\\Data\\SoilParameters\\SWG_china";
        List<File> soilParasPathList = FileUtils.getAllFile(new File(dir));
//        List file = getAllFile(new File(dir));
        System.out.println(soilParasPathList);
        for (Object soilPara : soilParasPathList) {
            System.out.println(soilPara.toString());
        }
    }
}