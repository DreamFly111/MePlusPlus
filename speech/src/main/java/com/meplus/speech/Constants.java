package com.meplus.speech;

/**
 * Created by dandanba on 4/7/16.
 */
public class Constants {
public static String TANS_APP_ID = "20160412000018503";
public static String TANS_KEY = "s8NOOwyo2zOHi9pU7F2W";
    // lang
    public static String LANG = "zh_cn";// "en_us"

    //avoid obj
    public static boolean FLAG_OBJ = true;

    // lang
    public final static String ZH_LANG = "zh_cn";// "en_us"
    public final static String EN_LANG = "en_us";// "en_us"
    // 默认发音人
    public final static String ZH_VOICER = "nannan";
    public final static String EN_VOICER = "vimary";
    // app_id
    public static String SPEECH_APP_ID = "55d7d8a9";
    // 没有结果，但是包含了service的回答
    public final static String I_DO_NOT_KNOW = "I do not know ";

    //启动多我的欢迎语  嗨_我是多我
    public static String getHelloMeplus() {
        return LANG.equals(ZH_LANG) ? "嗨,我是多我 " : LANG.equals(EN_LANG) ? "Hi, I'm me plus " : "嗨,我是多我 ";
    }

    // 没有结果的回答 我真的不知道啊
    public static String getNoAnswer() {
        return LANG.equals(ZH_LANG) ? "我真的不知道啊 " : LANG.equals(EN_LANG) ? "I really don't know " : "我真的不知道啊 ";
    }

    // －－－－－－－－－特别语音指令－－－－－－－－－
    // 别说了
    public static String getShutp() {
        return LANG.equals(ZH_LANG) ? "别说了。" : LANG.equals(EN_LANG) ? "Don't say it." : "别说了。";
    }

}
