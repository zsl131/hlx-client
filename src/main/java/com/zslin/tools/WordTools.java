package com.zslin.tools;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/11 0:16.
 * 字符处理工具类
 */
public class WordTools {

    /**
     * 获取中文的个数
     * @param str
     * @return
     */
    public static int getChineseLen(String str) {
        int len = 0;
        char [] chars = str.toCharArray();
        for(char c : chars) {
            if(isChinese(c)) {len++;}
        }
        return len;
    }

    public static float buildLeftMargin(String str) {
        //一个汉字占位：12.7
        //一个字母或数字占位：6.35
        int chineseLen = getChineseLen(str);
        int numLen = str.length()-chineseLen;
        float res = (float)(140-chineseLen*12.7-numLen*6.35)/2;
        if(res<0) {return 0;}
        return res;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 将较长字符串分成len的数组
     * @param str
     * @param len
     * @return
     */
    public static String [] rebuildStr(String str, int len) {
        int length = str.length();
        String [] res = new String[(length/len+(length%len==0?0:1))];
        for(int i=0;i<res.length; i++) {
            res[i] = str.substring(0, str.length()>len?len:str.length());
            if(str.length()>len) {
                str = str.substring(len, str.length());
            }
        }
        return res;
    }
}
