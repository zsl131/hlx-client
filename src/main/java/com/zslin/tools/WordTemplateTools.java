package com.zslin.tools;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.tools.docx4j.ImageAdd;
import com.zslin.tools.qr.BarcodeUtil;
import com.zslin.tools.qr.QrGenerateTools;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/11 19:42.
 * word文档工具类
 */
@Component
public class WordTemplateTools {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private QrGenerateTools qrGenerateTools;

    public File getTemplateFile(String fileName) {
//            File f = ResourceUtils.getFile("classpath:word-temp/"+fileName);
        File f = new File(configTools.getUploadPath("word-temp/")+fileName);
        return f;
    }

    /**
     * 获取压金单模板
     * @return
     */
    public File getBondTemplate() {
        return getTemplateFile("bond-template.docx");
    }

    public File getTicketTempate() {
        return getTemplateFile("ticket-template.docx");
    }

    public File getOutTemplate() {
        return getTemplateFile("out-template.docx");
    }

    public File buildOutFile(String shopName, String commodity, Float money, String date, String orderNo,
                              String phone, String address) {
        File targetFile = new File(configTools.getUploadPath("tickets/")+"out-"+(UUID.randomUUID().toString())+".docx");
        try {
            File f = getOutTemplate();
            // 载入模板文件
            WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(f);
            // 提取正文
            MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
            HashMap<String, String> datas = new HashMap<>();
            datas.put("shopName", shopName);
            datas.put("commodity", commodity);
            datas.put("money", money+"");
            datas.put("code", orderNo);
            datas.put("date", date);
            datas.put("phone", phone);
            datas.put("address", address);
            mainDocumentPart.variableReplace(datas);

            byte[] barcode = qrGenerateTools.getBarcode(orderNo);
            ImageAdd.replaceImage(wPackage, "barcode", barcode, "test1", "haha1");
            wPackage.save(targetFile);
        } catch (Docx4JException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    public File buildTicketFile(String shopName, String level, String date, String orderNo,
                                String phone, String address, String type) {
        File targetFile = new File(configTools.getUploadPath("tickets/")+"ticket-"+(UUID.randomUUID().toString())+".docx");
        try {
            File f = getTicketTempate();
            // 载入模板文件
            WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(f);
            // 提取正文
            MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
            HashMap<String, String> datas = new HashMap<>();
            datas.put("shopName", shopName);
            datas.put("level", level);
            datas.put("date", date);
            datas.put("phone", phone);
//            datas.put("address", address);
            datas.put("code", orderNo);
            datas.put("type", buildOrderType(type));
            mainDocumentPart.variableReplace(datas);

//            InputStream is = new FileInputStream(getTemplateFile("qrcode.jpg"));
//            byte[] bytes = IOUtils.toByteArray(is);
//            ImageAdd.replaceImage(wPackage, "qrcode", bytes, "test", "haha");
//            byte[] barcode = BarcodeUtil.generate(orderNo);
            byte[] barcode = qrGenerateTools.getBarcode(orderNo);
            ImageAdd.replaceImage(wPackage, "barcode", barcode, "test1", "haha1");
            wPackage.save(targetFile);
        } catch (Docx4JException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    private String buildOrderType(String type) {
        String res = "";
        if("1".equals(type)) {
            res = "店内订单";
        } else if("2".equals(type)) {
            res = "微信订单";
        } else if("3".equals(type)) {
            res = "美团订单";
        } else if("4".equals(type)) {
            res = "亲情订单";
        } else if("5".equals(type)) {
            res = "会员订单";
        } else if("6".equals(type)) {
            res = "卡券订单";
        } else if("8".equals(type)) {
            res = "商场订单";
        } else if("9".equals(type)) {
            res = "飞凡订单";
        } else if("7".equals(type)) {
            res = "货到付款";
        }
        return res;
    }

    public File buildBondFile(String shopName, Integer peopleCount, Float bondMoney, String date, String orderNo,
                              String phone, String address, String payType, String bondPayType, String type, String endTime) {
        File targetFile = new File(configTools.getUploadPath("tickets/")+"bond-"+(UUID.randomUUID().toString())+".docx");
        try {
            File f = getBondTemplate();
            // 载入模板文件
            WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(f);
            // 提取正文
            MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
            HashMap<String, String> datas = new HashMap<>();
            datas.put("shopName", shopName);
            datas.put("peopleCount", peopleCount+"");
            datas.put("bondMoney", bondMoney+"");
            datas.put("code", orderNo);
            datas.put("date", date);
            datas.put("phone", phone);
            datas.put("address", address);
            datas.put("payType", buildPayType(payType));
            datas.put("bondPayType", buildPayType(bondPayType));
            datas.put("type", buildOrderType(type));
            datas.put("haveTime", endTime==null?"2小时内":endTime);
            mainDocumentPart.variableReplace(datas);

//            byte[] bytes = qrGenerateTools.genQr(orderNo);
//            InputStream is = new FileInputStream(getTemplateFile("qrcode.jpg"));
//            byte[] bytes = IOUtils.toByteArray(is);
//            ImageAdd.replaceImage(wPackage, "qrcode", bytes, "test", "haha");
//            byte[] barcode = BarcodeUtil.generate(orderNo);
            byte[] barcode = qrGenerateTools.getBarcode(orderNo);
            ImageAdd.replaceImage(wPackage, "barcode", barcode, "test1", "haha1");
            wPackage.save(targetFile);
        } catch (Docx4JException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    private String buildPayType(String payType) {
        StringBuffer sb = new StringBuffer();
        sb.append("（");
        //1-现金；2-刷卡；3-微信支付；4-支付宝支付；5-商场支付
        if("1".equals(payType)) {
            sb.append("现");
        } else if("2".equals(payType)) {
            sb.append("卡");
        } else if ("3".equals(payType)) {
            sb.append("微");
        } else if("4".equals(payType)) {
            sb.append("支");
        } else if("5".equals(payType)) {
            sb.append("商");
        } else {
            sb.append("其他");
        }
        sb.append("）");
        return sb.toString();
    }
/*
    public File buildBondFile(String shopName, Integer peopleCount, Float bondMoney, String date) {
        File outFile = new File(configTools.getUploadPath("bond")+"/123.doc");
        try {
            Map<String, Object> datas = new HashMap<>();
            datas.put("shopName", shopName);
            datas.put("peopleCount", peopleCount);
            datas.put("bondMoney", bondMoney);
            datas.put("date", date);
            build(getBondTemplate(), datas, outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outFile;
    }

    private void build(File tmpFile, Map<String, Object> contentMap, String exportFile) throws Exception {
        FileInputStream tempFileInputStream = new FileInputStream(tmpFile);
        HWPFDocument document = new HWPFDocument(tempFileInputStream);
        // 读取文本内容
        Range bodyRange = document.getRange();
        // 替换内容
        for (Map.Entry<String, Object> entry : contentMap.entrySet()) {
            bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue().toString());
        }

        //导出到文件
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);
        OutputStream outputStream = new FileOutputStream(exportFile);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.close();
    }*/
}
