//package nnu.ogms.basins.Utils;
//
//import java.io.File;
//import java.io.IOException;
//
//public class Ini4jUtils {
//    public static boolean creatIniFile(String filePath,List<IniFileEntity> filecontent) throws IOException {
//        File file = new File(filePath);
//        if(file.exists()){
//            return false;
//        }
//        file.createNewFile();
//        Ini ini = new Ini();
//        ini.load(file);
//
//        //将文件内容保存到ini对象中
//        filecontent.stream().forEach((entity)->{
//            ini.add(entity.getSection(),entity.getKey(),entity.getValue()== null ? "": entity.getValue());
//        });
//        //将文件内容保存到文件中
//        ini.store(file);
//        return true;
//    }
//}
