package nnu.ogms.basins;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class BasinsApplicationTests {

    @Test
    void contextLoads() {
        long date = DateUtil.current();
        Date date2 = DateUtil.date(Calendar.getInstance());
//当前时间
        Date date3 = DateUtil.date(System.currentTimeMillis());
        System.out.println(date);
        System.out.println(date2);
        System.out.println(date3);
    }

}
