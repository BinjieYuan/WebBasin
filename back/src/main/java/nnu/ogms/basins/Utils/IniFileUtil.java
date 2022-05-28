package nnu.ogms.basins.Utils;

import java.io.*;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniFileUtil {
    /**
     * 创建新的ini文件
     * @param filePath 文件路径
     * @param fileContent 如果要保存存储顺序，建议使用LinkedHashMap
     * @return
     * @throws IOException
     */
    public static boolean createIniFile(String filePath, Map<String,Map<String,Object>> fileContent) throws IOException {
        File file = new File(filePath);
        if(file.exists()){
            return false;
        }
        Set<String> keys = null;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));){
            for (String section : fileContent.keySet()){
                bufferedWriter.write("["+section+"]"+"\r\n");
                keys = fileContent.get(section).keySet();
                for (String key : keys){
                    bufferedWriter.write(key+" = "+fileContent.get(section).get(key)+"\r\n");
                }
                bufferedWriter.newLine();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new IOException("文件写出异常");
        }
        return true;
    }

    /**
     * 从ini配置文件中读取变量的值
     * @param file         配置文件的路径
     * @param section      要获取的变量所在段名称
     * @param variable     要获取的变量名称
     * @param defaultValue 变量名称不存在时的默认值
     * @return 变量的值
     * @throws IOException 抛出文件操作可能出现的io异常
     */
    public static String readCfgValue(String file, String section, String variable, String defaultValue)
            throws IOException {
        String strLine, value = "";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(URLDecoder.decode(file, "UTF-8"))); //解决中文路径乱码
        boolean isInSection = false;
        try {
            while ((strLine = bufferedReader.readLine()) != null) {
                strLine = strLine.trim();
                strLine = strLine.split("[;]")[0];
                Pattern p;
                Matcher m;
                p = Pattern.compile("\\[\\w+]");// Pattern.compile("file://[//s*.*//s*//]");
                m = p.matcher((strLine));
                if (m.matches()) {
                    p = Pattern.compile("\\[" + section + "\\]");// Pattern.compile("file://[//s*" + section +
                    // "file://s*//]");
                    m = p.matcher(strLine);
                    if (m.matches()) {
                        isInSection = true;
                    } else {
                        isInSection = false;
                    }
                }
                if (isInSection == true) {
                    strLine = strLine.trim();
                    String[] strArray = strLine.split("=");
                    if (strArray.length == 1) {
                        value = strArray[0].trim();
                        if (value.equalsIgnoreCase(variable)) {
                            value = "";
                            return value;
                        }
                    } else if (strArray.length == 2) {
                        value = strArray[0].trim();
                        if (value.equalsIgnoreCase(variable)) {
                            value = strArray[1].trim();
                            return value;
                        }
                    } else if (strArray.length > 2) {
                        value = strArray[0].trim();
                        if (value.equalsIgnoreCase(variable)) {
                            value = strLine.substring(strLine.indexOf("=") + 1).trim();
                            return value;
                        }
                    }
                }
            }
        } finally {
            bufferedReader.close();
        }
        return defaultValue;
    }

    /**
     * 修改ini配置文件中变量的值
     * @param file     配置文件的路径
     * @param section  要修改的变量所在段名称
     * @param variable 要修改的变量名称
     * @param value    变量的新值
     * @throws IOException 抛出文件操作可能出现的io异常
     */
    public static boolean writeCfgValue(String file, String section, String variable, String value) throws IOException {
        String fileContent, allLine, strLine, newLine;
        String getValue = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(URLDecoder.decode(file, "UTF-8"))); //解决中文路径乱码
        boolean isInSection = false;
        boolean canAdd = true;
        fileContent = "";
        try {

            while ((allLine = bufferedReader.readLine()) != null) {
                allLine = allLine.trim();
                strLine = allLine.split(";")[0];
                Pattern p;
                Matcher m;
                p = Pattern.compile("\\[\\w+]");
                m = p.matcher((strLine));
                if (m.matches()) {
                    p = Pattern.compile("\\[" + section + "\\]");
                    m = p.matcher(strLine);
                    if (m.matches()) {
                        isInSection = true;
                    } else {
                        isInSection = false;
                    }
                }
                if (isInSection == true) {
                    strLine = strLine.trim();
                    String[] strArray = strLine.split("=");
                    getValue = strArray[0].trim();
                    if (getValue.equalsIgnoreCase(variable)) {
                        newLine = getValue + "=" + value;
                        fileContent += newLine;
                        while ((allLine = bufferedReader.readLine()) != null) {
                            fileContent += "\r\n" + allLine;
                        }
                        bufferedReader.close();
                        canAdd = false;
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,false));
                        bufferedWriter.write(fileContent);
                        bufferedWriter.flush();
                        bufferedWriter.close();

                        return true;
                    }

                }
                fileContent += allLine + "\r\n";
            }
            if (canAdd) {
                String str = variable + "=" + value;
                fileContent += str + "\r\n";
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
                bufferedWriter.write(fileContent);
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            bufferedReader.close();
        }
        return false;
    }

//    public static void main(String[] args) throws IOException {

//        Map<String,Map<String,Object>> fileContent = new LinkedHashMap<>();
//        fileContent.put("LDAP",new LinkedHashMap(){
//            private static final long serialVersionUID = 1L;
//            {
//                put("ip","1.1.1.1");
//                put("port","8569");
//                put("pwd","");
//                put("user","javac");
//            }
//        });
//        fileContent.put("SAVE",new LinkedHashMap(){
//            private static final long serialVersionUID = 1L;
//            {
//                put("savefiledir","");
//                put("savefilesubdir","");
//                put("savemode","");
//            }
//        });
//        fileContent.put("LOG",new LinkedHashMap(){
//            private static final long serialVersionUID = 1L;
//            {
//                put("logDebugOpen","");
//                put("logFileName","");
//            }
//        });
//        fileContent.put("SEARCH",new LinkedHashMap(){
//            private static final long serialVersionUID = 1L;
//            {
//                put("searchbasedn","");
//                put("searchfilter","");
//                put("searchflag","");
//                put("maxcrls","");
//                put("postfix","");
//                put("base64check","");
//            }
//        });
//        fileContent.put("SHECA",new LinkedHashMap(){
//            private static final long serialVersionUID = 1L;
//            {
//                put("LibName","");
//            }
//        });
//        fileContent.put("TIMER",new LinkedHashMap(){
//            private static final long serialVersionUID = 1L;
//            {
//                put("timermode","");
//                put("timerspan","");
//            }
//        });
//        long t0 = System.currentTimeMillis();
//        IniFileUtil.createIniFile("D:\\WEB\\basins\\test\\test0.ini",fileContent);
//        long t1 = System.currentTimeMillis();
//        System.out.println("二："+(t1-t0));

//        try {
//			/*;文件事例
//			[Client]
//			;客户端版本号
//			version=0001
//			;设备号
//			devNum=6405*/
//            boolean value = IniFileUtil.writeCfgValue("D:\\WEB\\basins\\test\\test0.ini", "LDAP", "port", "8888");
//            System.out.println(value);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}

