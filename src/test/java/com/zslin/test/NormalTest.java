package com.zslin.test;

import com.zslin.basic.tools.SecurityUtil;
import com.zslin.card.dto.CardCheckDto;
import com.zslin.card.service.ICardCheckService;
import com.zslin.model.BuffetOrder;
import com.zslin.model.DiscountTime;
import com.zslin.service.IBuffetOrderService;
import com.zslin.service.IDiscountTimeService;
import com.zslin.service.IMemberChargeService;
import com.zslin.tools.OrderNoTools;
import com.zslin.tools.RestdayTools;
import com.zslin.tools.VersionTools;
import com.zslin.tools.WordTools;
import com.zslin.web.dto.MyTimeDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.*;
import java.awt.print.*;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 15:51.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class NormalTest {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IMemberChargeService memberChargeService;

    @Autowired
    private IDiscountTimeService discountTimeService;

    @Autowired
    private RestdayTools restdayTools;

    @Autowired
    private VersionTools versionTools;

    @Autowired
    private ICardCheckService cardCheckService;

    @Test
    public void test33() {
        List<CardCheckDto> list = cardCheckService.findCheckDtoByDay("20181016");
        for(CardCheckDto dto : list) {
            System.out.println("======"+dto);
        }

        List<CardCheckDto> list2 = cardCheckService.findCheckDtoByMonth("201810");
        for(CardCheckDto dto : list2) {
            System.out.println("======"+dto);
        }
    }

    @Test
    public void test32() {
        buffetOrderService.listByDate("2018-02-01");
    }

    @Test
    public void test31() throws Exception {
        String url = "D:\\temp\\project\\upgrade1.bat";
        Runtime.getRuntime().exec("cmd /c start "+url);
        versionTools.run_cmd(url, true);
    }

    @Test
    public void test30() {
        Integer a = 258;
        Integer b = 10;
        Integer c = a / b;
        System.out.println("==============="+c);
    }

    @Test
    public void test29() {
        System.out.println("========="+restdayTools.isWorkday());
    }

    @Test
    public void test28() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println("============"+sdf.format(cal.getTime()));
    }

    @Test
    public void test27() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 15);
        Float price = (new Date()).before(cal.getTime())?45f:55f;
        System.out.println("======="+price);
        System.out.println(sdf.format(cal.getTime())+"============="+sdf.format(new Date()));
    }


    @Test
    public void test26() {
        Integer [] array = new Integer[]{3,3,3,3,4,4,4,5,5,6,7};
//        Integer count = array[(int)(Math.random()*array.length)];
        for(int i=0;i<100;i++) {
            System.out.printf("========="+array[(int)(Math.random()*array.length)]);
        }
    }

    @Test
    public void test25() {
        Float f = buffetOrderService.queryDiscountMoneyByTime("2017-08-28");
        System.out.println("========"+f);
    }

    @Test
    public void test24() {
        DiscountTime dt = discountTimeService.findByTime(1720);
        System.out.println("======"+dt);

        DiscountTime dt1 = discountTimeService.findByTime(1700);
        System.out.println("======"+dt1);

        DiscountTime dt2 = discountTimeService.findByTime(1730);
        System.out.println("======"+dt2);

        DiscountTime dt3 = discountTimeService.findByTime(1731);
        System.out.println("======"+dt3);

        DiscountTime dt4 = discountTimeService.findByTime(1659);
        System.out.println("======"+dt4);

        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        System.out.println("time:"+sdf.format(new Date()));
    }

    @Test
    public void test23() {
        MyTimeDto mtd = new MyTimeDto("2017-08-23");
        Float ffanMoneyAM = buffetOrderService.queryMoneyByFfan(mtd.getStartTimeAM(), mtd.getEndTimeAM());
        System.out.println("========="+ffanMoneyAM);
    }

    @Test
    public void test22() {
        Float f1 = memberChargeService.queryMoneyByPayType("2017-07-08", "1");
        Float f2 = memberChargeService.queryMoneyByPayType("2017-07-08", "2");
        Float f3 = memberChargeService.queryMoneyByPayType("2017-07-08", "3");
        System.out.println("f1:"+f1+",f2:"+f2+",f3:"+f3);
    }

    @Test
    public void test21() throws Exception {
        Date d1 = new Date();
        Thread.sleep(1000);
        Date d2 = new Date();
        System.out.println(d2.before(d1));
    }

    @Test
    public void test20() {
        List<BuffetOrder> list = buffetOrderService.findByNoBond();
        System.out.println(list.size());
    }

    @Test
    public void test110() throws Exception {
        System.out.println(SecurityUtil.md5("zsl", "zsl131"));
    }

    @Test
    public void test01() {
        JFileChooser fileChooser = new JFileChooser(); //创建打印作业
        int state = fileChooser.showOpenDialog(null);
        System.out.println("============="+state);
        if(state == fileChooser.APPROVE_OPTION){
            File file = new File("D:/temp.txt"); //获取选择的文件
            //构建打印请求属性集
            HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            //设置打印格式，因为未确定类型，所以选择autosense
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            //查找所有的可用的打印服务
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            //定位默认的打印服务
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            //显示打印对话框
            PrintService service = ServiceUI.printDialog(null, 200, 200, printService,
                    defaultService, flavor, pras);
            if(service != null){
                try {
                    DocPrintJob job = service.createPrintJob(); //创建打印作业
                    FileInputStream fis = new FileInputStream(file); //构造待打印的文件流
                    DocAttributeSet das = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(fis, flavor, das);
                    job.print(doc, pras);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test03() {

        for(int i=0; i<=2; i++) {
            print();
        }
    }

    private void print() {
        Prient prient = new Prient();

        prient.setAddress("地址：金融中心·金池购物中心三楼F3-15~F3-16");
        prient.setCashierName("钟述林");
        prient.setChildCount(1);
        prient.setDay("2017-03-25");
        prient.setInfo("请保管好您的随身物品！");
        prient.setPeopleCount(3);
        prient.setPhone("18087021771");
        prient.setTel("0870-2127507");
        prient.setTime("14:35 - 16:40");
        prient.setTitle("汉丽轩昭通店（午餐）");
        prient.setType("微信下单-已付款");
        prient.setWaitCount(5);
        prient.setAboutTime("16:00");

//        int height = 175 + 3 * 15 + 20;
        float height = 250+WordTools.rebuildStr(prient.getAddress(), 11).length*20+WordTools.rebuildStr(prient.getInfo(), 11).length*20;

        System.out.println(height+"==============");

        // 通俗理解就是书、文档
        Book book = new Book();

        // 打印格式
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);

        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper p = new Paper();
        p.setSize(140, height);
        p.setImageableArea(0, -20, 140, height + 20);
        pf.setPaper(p);

        // 把 PageFormat 和 Printable 添加到书中，组成一个页面
        book.append(prient, pf);

        PrintService [] array = PrinterJob.lookupPrintServices();

        for(PrintService ps : array) {
            System.out.println(ps.getName());
        }
        System.out.println("=====size:" + array.length);

        // 获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(book);
        try {
            job.print();

        } catch (PrinterException e) {
            e.printStackTrace();
            System.out.println("================打印出现异常");
        }
    }

    @Test
    public void test04() {
        String str = "打印出现异常微信下单-已付款汉丽轩昭通店（午餐餐）";
        String [] array = WordTools.rebuildStr(str, 5);
        for(String s : array) {
            System.out.println(s);
        }
    }

    @Autowired
    private OrderNoTools orderNoTools;

    @Test
    public void test05() {
        // 测试多线程调用订单号生成工具
        try {
            for (int i = 0; i < 200; i++) {
                Thread t1 = new Thread(new Runnable() {
                    public void run() {
                        System.out.println("====="+orderNoTools.getOrderNo("2"));
                    }
                });
                t1.start();

               Thread t2 = new Thread(new Runnable() {
                    public void run() {
                        System.out.println("====="+orderNoTools.getOrderNo("1"));
                    }
                });
                t2.start();
                System.out.println("====="+orderNoTools.getOrderNo("3"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
