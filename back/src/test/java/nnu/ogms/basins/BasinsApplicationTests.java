package nnu.ogms.basins;

import cn.hutool.core.date.DateUtil;
import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class BasinsApplicationTests {

    @Test
    public static void main(String[] args) {
        System.out.println( System.getProperty("java.library.path"));
        ogr.RegisterAll();
        int count =ogr.GetDriverCount();
        System.out.println(count);
        for(int i=0; i<count; i++){
            System.out.println(ogr.GetDriver(i).GetName());
        }
        gdal.GDALDestroyDriverManager();
    }

}
