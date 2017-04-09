package com.zslin.test;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.JobName;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/8 23:36.
 */
public class PrintTools {

    public static void printFile(String filePath) throws Exception {
        File pdfFile = new File(filePath);
        // 构建打印请求属性集
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new JobName(pdfFile.getName(), null));

        HashAttributeSet has = new HashAttributeSet();
//                has.add(new PrinterName("Officejet J5500 series", null)); // 添加打印机名称

        // 设置打印格式，因为未确定文件类型，这里选择AUTOSENSE
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

        // 查找所有的可用打印服务
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, has);

        if (printService[1] != null) {
            //获得打印服务的文档打印作业
            DocPrintJob job = printService[1].createPrintJob(); // 创建打印任务

            DocAttributeSet das=new HashDocAttributeSet();

            InputStream fis = new FileInputStream(pdfFile); // 构造待打印的文件流
            Doc doc = new SimpleDoc(fis, flavor, das); // 建立打印文件格式
            job.print(doc, pras); // 进行文件的打印
        }
    }
}
