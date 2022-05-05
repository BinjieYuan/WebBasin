package nnu.ogms.basins.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class RunExe {

    public static void openExe(String cmd) {
        BufferedReader br = null;
        BufferedReader brError = null;

        try {
            //执行exe  cmd可以为字符串(exe存放路径)也可为数组，调用exe时需要传入参数时，可以传数组调用(参数有顺序要求)
            Process p = Runtime.getRuntime().exec(cmd);
            String line = null;
            //获得子进程的输入流。
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            //获得子进程的错误流。
            brError = new BufferedReader(new InputStreamReader(p.getErrorStream(),Charset.forName("GBK")));
            while ((line = br.readLine()) != null  || (line = brError.readLine()) != null) {
                //输出exe输出的信息以及错误信息
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
