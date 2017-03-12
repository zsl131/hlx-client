package com.zslin.upload.tools;

import com.zslin.basic.tools.ConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 23:51.
 */
@Component
public class UploadFileTools {

    private static final String GET_FILE = "client-put.txt";

    @Autowired
    private ConfigTools configTools;

    private File getChangeFile() {
        return getFile(GET_FILE);
    }

    public void setChangeContext(String content, boolean isAppend) {
        if(isAppend) { //如果是追加则需要修改里面的内容
            String con = getChangeContext();
            if(con!=null && !"".equals(con.trim())) {
                int minus =2;
                if(con.endsWith("\n")) {minus+=1;}
                String temp = "{status:1,info:\"ok\",data:[";
                content = con.substring(0, con.length() - minus) + "," + content.replace(temp, "");
            }
        }
        setFileContext(getChangeFile(), content);
    }

    public String getChangeContext() {
        return getFileContext(getChangeFile());
    }

    private File getFile(String fileName) {
        File file = new File(configTools.getUploadPath("/upload/")+fileName);
        if(!file.exists()) {createFile(fileName);}
        return file;
    }

    private void createFile(String fileName) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            bw.write("");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private String getFileContext(File file) {
        BufferedReader br = null;
//        String res = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String str = null;
            while((str=br.readLine())!=null) {
                sb.append(str).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br!=null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    private void setFileContext(File file, String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
