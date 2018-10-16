package com.zslin.tools;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.IOException;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/8 23:42.
 */
public class PrintTools {

    public static void print(String path) {
        try {
            new PrintTools().printWord(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printWord(String path) {
        ComThread.InitSTA();
        ActiveXComponent word=new ActiveXComponent("Word.Application");
        Dispatch doc=null;
        Dispatch.put(word, "Visible", new Variant(false));
        Dispatch docs=word.getProperty("Documents").toDispatch();
        doc=Dispatch.call(docs, "Open", path).toDispatch();
//        System.out.println("++++++++"+word.getProgramId()+"+==="+docs.getProgramId());
//        System.out.println("=========="+doc.getProgramId()+"===="+doc.m_pDispatch+"+++");
        try {
            Dispatch.call(doc, "PrintOut");//打印
            word.invoke("Quit", new Variant[] {});
            ComThread.Release();
            killWordTask();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("打印失败");
        }finally{
            try {
                if(doc!=null){
                    Dispatch.call(doc, "Close",new Variant(0));
                }
                doc = null;
//                killWordTask();
                ComThread.Release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            //释放资源
            ComThread.Release();
        }

        ComThread.Release();
        ComThread.quitMainSTA();
    }

    private static void killWordTask() {
        try {
            Runtime rt = Runtime.getRuntime();
            String command = "taskkill /F /IM WINWORD.EXE";
            rt.exec(command);
        } catch (IOException e) {
        }
    }
}
