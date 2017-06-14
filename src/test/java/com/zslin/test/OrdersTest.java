package com.zslin.test;

import com.zslin.service.IBuffetOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 15:51.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class OrdersTest {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Test
    public void test01() {
        Float f1 = buffetOrderService.queryMemberDiscount("2017-06-14 09:00", "2017-06-14 15:30");
        Float f2 = buffetOrderService.queryMemberRepair("2017-06-14 09:00", "2017-06-14 15:30", "3");
        Float f3 = buffetOrderService.queryBondByType("2017-06-14 09:00", "2017-06-14 15:30", "3");
        System.out.println(f1+"============"+f2+"=========="+f3);
    }
}
