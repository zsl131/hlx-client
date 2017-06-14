package com.zslin.test;

import com.zslin.tools.WordTemplateTools;
import com.zslin.tools.docx4j.ImageAdd;
import com.zslin.tools.qr.BarcodeUtil;
import com.zslin.tools.qr.QrGenerateTools;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.finders.RangeFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.docx4j.wml.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/11 20:01.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class WordTest {

    @Autowired
    private WordTemplateTools wordTemplateTools;

    @Test
    public void test05() {
        qrGenerateTools.getBarcode("2017041210007");
    }

    @Test
    public void testBarcode() {
        BarcodeUtil.generateFile("2017041210007", "D:/temp111.png");
    }

    @Test
    public void test01() {
//        File f = wordTemplateTools.buildBondFile("汉丽轩·昭通店", 5, 30f, "2017-04-11 20:02:33");
//        System.out.println(f.getAbsoluteFile());
    }

    @Test
    public void testExport() {
        File f = wordTemplateTools.buildBondFile("汉丽轩·昭通店", 5, 30f, "2017-04-11 20:02:33", "20170412100005", "0870", "地址", "1", "3", "4");
        System.out.println(f.getAbsoluteFile());
        File f2 = wordTemplateTools.buildTicketFile("汉丽轩·昭通店", "午餐", "2017-04-12 12:30:22", "20170412100006", "0870", "地址", "3");
        System.out.println(f2);
    }

    @Test
    public void testReplaceString() throws Exception {
        // 模板文件路径
        String templatePath = "D:/temp123.docx";
        // 生成的文件路径
        String targetPath = "D:/target.docx";
        // 书签名
        String bookmarkName = "code";
        // 图片路径
        String imagePath = "D:/temp.jpg";

        // 载入模板文件
        WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(new FileInputStream(templatePath));
        // 提取正文
        MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
        HashMap<String, String> datas = new HashMap<>();
        datas.put("code", "123456");
        mainDocumentPart.variableReplace(datas);



        wPackage.save(new FileOutputStream(targetPath));
    }

    @Autowired
    private QrGenerateTools qrGenerateTools;

    @Test
    public void testQr() {
        qrGenerateTools.genQr("1234561");
    }

    @Test
    public void testReplaceImage() throws Exception {
        // 模板文件路径
        String templatePath = "D:/temp123.docx";
        // 生成的文件路径
        String targetPath = "D:/target3.docx";
        // 图片路径
        String imagePath = "D:/111.jpg";

        // 载入模板文件
        WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(new FileInputStream(templatePath));

        InputStream is = new FileInputStream(imagePath);
        byte[] bytes = IOUtils.toByteArray(is);
        ImageAdd.replaceImage(wPackage, "${image}", bytes, "test", "haha");
        wPackage.save(new FileOutputStream(targetPath));
    }

    //https://github.com/plutext/docx4j/tree/master/src/samples/docx4j/org/docx4j/samples
    //http://www.jianshu.com/p/4f91d1ad3a41
    @Test
    public void test02() throws Exception {
        // 模板文件路径
        String templatePath = "D:/temp123.docx";
        // 生成的文件路径
        String targetPath = "D:/target.docx";
        // 书签名
        String bookmarkName = "code";
        // 图片路径
        String imagePath = "D:/temp.jpg";

        // 载入模板文件
        WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(new FileInputStream(templatePath));
        // 提取正文
        MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
        Document wmlDoc = (Document) mainDocumentPart.getJaxbElement();
        Body body = wmlDoc.getBody();
        // 提取正文中所有段落
        List<Object> paragraphs = body.getContent();
        // 提取书签并创建书签的游标
        RangeFinder rt = new RangeFinder("code", "CTMarkupRange");
        //new TraversalUtil(paragraphs, rt);

        // 遍历书签
        for (CTBookmark bm:rt.getStarts()) {
            // 这儿可以对单个书签进行操作，也可以用一个map对所有的书签进行处理
            System.out.println("========"+bm.getName());
            if (bm.getName().equals(bookmarkName)){
                // 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
                InputStream is = new FileInputStream(imagePath);
                byte[] bytes = IOUtils.toByteArray(is);
                // 穿件一个行内图片
                BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage, bytes);

                // createImageInline函数的前四个参数我都没有找到具体啥意思，，，，
                // 最有一个是限制图片的宽度，缩放的依据
                Inline inline = imagePart.createImageInline(null, null, 0,1, false, 800);
                // 获取该书签的父级段落
                P p = (P)(bm.getParent());

                ObjectFactory factory = new ObjectFactory();
                // R对象是匿名的复杂类型，然而我并不知道具体啥意思，估计这个要好好去看看ooxml才知道
                R run = factory.createR();
                // drawing理解为画布？
                Drawing drawing = factory.createDrawing();
                drawing.getAnchorOrInline().add(inline);
                run.getContent().add(drawing);
                p.getContent().add(run);
            }
        }
        wPackage.save(new FileOutputStream(targetPath));
    }

    @Test
    public void addImage() throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

        // 生成的文件路径
        String targetPath = "D:/target.docx";
        // 图片路径
        String imagePath = "D:/temp.jpg";

        // The image to add
        File file = new File(imagePath);

        // Our utility method wants that as a byte array
        java.io.InputStream is = new java.io.FileInputStream(file );
        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "+file.getName());
        }
        is.close();

        String filenameHint = null;
        String altText = null;
        int id1 = 0;
        int id2 = 1;


        // Image 1: no width specified
        org.docx4j.wml.P p = ImageAdd.newImage( wordMLPackage, bytes,
                filenameHint, altText,
                id1, id2 );
        wordMLPackage.getMainDocumentPart().addObject(p);

        // Image 2: width 3000
        org.docx4j.wml.P p2 = ImageAdd.newImage( wordMLPackage, bytes,
                filenameHint, altText,
                id1, id2, 3000 );
        wordMLPackage.getMainDocumentPart().addObject(p2);

        // Image 3: width 6000
        org.docx4j.wml.P p3 = ImageAdd.newImage( wordMLPackage, bytes,
                filenameHint, altText,
                id1, id2, 6000 );
        wordMLPackage.getMainDocumentPart().addObject(p3);


        // Now save it
        wordMLPackage.save(new java.io.File(targetPath) );
    }
}
