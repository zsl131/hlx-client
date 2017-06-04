package com.zslin.test;

import com.zslin.basic.tools.SecurityUtil;
import com.zslin.tools.OrderNoTools;
import com.zslin.tools.WordTools;
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

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 15:51.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class NormalTest {

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
