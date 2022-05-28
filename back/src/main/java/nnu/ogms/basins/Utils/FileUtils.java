package nnu.ogms.basins.Utils;

import nnu.ogms.basins.common.ErrorEnum;
import nnu.ogms.basins.common.GeneralException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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
}