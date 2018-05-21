package com.zslin.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.service.ICompanyService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2018/5/20.
 * 版本处理工具
 */
@Component
public class VersionTools {

    @Autowired
    private ICompanyService companyService;

    private File getBaseJarPath() {
        ApplicationHome home = new ApplicationHome(getClass());
        File jarFile = home.getSource();
        return jarFile.getParentFile();
    }

    private String getBaseJarDirectory() {
        return getBaseJarPath().getAbsolutePath()+File.separator;
    }

    private void downloadNewVersion(String versionUrl) {
        String clientName = "hlx-client-1.0-SNAPSHOT-new.jar";
        downLoadFromUrl(versionUrl, clientName, getBaseJarPath().getAbsolutePath());
    }

    /**
     * 版本更新
     * 流程：
     * 1、备份
     * 2、下载新版本，名称不能重复
     * 3、结束当前运行的系统
     * 4、重全名新版本名称与当前运行名称一致
     * 5、重新启动程序
     * @Param version 最新版本号
     * @Param versionUrl 程序下载地址
     */
    public void upgradeVersion(String version, String versionUrl) {
        copyJarFile(); //备份
        downloadNewVersion(versionUrl); //下载
        setVersion(version); //更新数据库防止重复更新
        run_cmd(generateCmd(), false);
    }

    private void setVersion(String version) {
        companyService.updateVersion(version);
    }

    /** 备份原始程序jar文件  */
    public void copyJarFile() {
        try {
            String clientName = "hlx-client-1.0-SNAPSHOT.jar";
            File file = getBaseJarPath();
            for(File f : file.listFiles()) {
                if(f.getName().equals(clientName)) { //写死文件名
                    //D:\temp\project;hlx-client-1.0-SNAPSHOT.jar-2018052023:45
                    String backFileName = file.getAbsolutePath()+File.separator+clientName+"-"+ DateTools.date2Str(new Date(), "yyyyMMddHHmm");
                    FileUtils.copyFile(f, new File(backFileName));
                    f.deleteOnExit();
                }
            }
        } catch (Exception e) {
            System.out.println("======更新版本异常======");
            e.printStackTrace();
        }
    }

    private String generateCmd() {
        StringBuffer sb = new StringBuffer();
        sb.append("ping -n 5 127.0.0.1").append("\n");
        sb.append("tasklist /V").append("\n");
        return generateCmd(sb.toString());
    }

    private String generateCmd(String cmd) {
        String cmdShortName = "upgrade.bat";
        String cmdName = getBaseJarDirectory()+cmdShortName;
        BufferedWriter bw ;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cmdName)));
            bw.write(cmd);
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cmdName;
    }

    public void run_cmd(String strcmd, boolean newWin) {
        Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
        try {
            //cmd /c start
            strcmd = (newWin?"cmd /c start ":"") + strcmd;
            ps = rt.exec(strcmd);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String line = null;
            while((line = br.readLine())!=null) {
                if(line.toLowerCase().replace(" ", "").indexOf("java-jar")>=0) {
//                    System.out.println("my:" + line);
                    killJava(line);
                }
            }
            ps.waitFor();  //等待子进程完成再往下执行。
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int i = ps.exitValue();  //接收执行完毕的返回值
        if (i == 0) {
            System.out.println("执行完成.");
        } else {
            System.out.println("执行失败.");
        }

        ps.destroy();  //销毁子进程
        ps = null;
    }

    private void killJava(String line) {
        String [] array = line.split(" ");
        List<String> list = new ArrayList<>();
        for(String str : array) {
            if(str!=null && !"".equals(str.trim())) {
                list.add(str);
            }
        }
        try {
            Integer pid = Integer.parseInt(list.get(1));
            StringBuffer sb = new StringBuffer();
            sb.append("taskkill /F /PID ").append(pid).append(" /T \n");
            sb.append("ping -n 3 127.0.0.1").append("\n");
            sb.append("del /F ").append(getBaseJarDirectory()).append("hlx-client-1.0-SNAPSHOT.jar").append("\n");
            sb.append("ping -n 3 127.0.0.1").append("\n");
            sb.append("rename ").append(getBaseJarDirectory()).append("hlx-client-1.0-SNAPSHOT-new.jar").append(" hlx-client-1.0-SNAPSHOT.jar").append("\n");
            sb.append("java -jar ").append(getBaseJarDirectory()).append("hlx-client-1.0-SNAPSHOT.jar").append("\n");
            sb.append("del ").append(getBaseJarDirectory()+"upgrade.bat").append("\n");

//            System.out.println(sb.toString());
            run_cmd(generateCmd(sb.toString()), true);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    private void  downLoadFromUrl(String urlStr,String fileName,String savePath) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(savePath);
            if(!saveDir.exists()){
                saveDir.mkdir();
            }
            File file = new File(saveDir+File.separator+fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
