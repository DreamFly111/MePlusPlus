package com.meplus.speech.presents;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.meplus.events.EventUtils;
import com.meplus.speech.Constants;
import com.meplus.speech.api.ApiService;
import com.meplus.speech.event.Speech;
import com.meplus.speech.event.SpeechEvent;
import com.meplus.speech.model.Answer;
import com.meplus.speech.model.Result;
import com.meplus.speech.model.Trans;
import com.meplus.speech.model.TransResult;
import com.meplus.speech.utils.JsonUtils;
import com.meplus.speech.utils.RetrofitUtil;

import java.util.List;

import cn.trinea.android.common.util.DigestUtils;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UnderstandPersenter {
    private static String TAG = UnderstandPersenter.class.getSimpleName();
    // translate url
    public final String BASE_URL = "http://api.fanyi.baidu.com/api/trans/vip/";
    private Retrofit mRetrofit;
    private ApiService mApiService;
    //初始化监听器（语音到语义）。
    private final InitListener mSpeechUdrInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "speechUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "onInit：" + code);
                final Speech speech = new Speech(Speech.ACTION_UNDERSTAND_ERROR);
                speech.setError(String.valueOf(code));
                EventUtils.postEvent(new SpeechEvent(speech));
            }
        }
    };

    // 语义理解回调。
    private final SpeechUnderstanderListener mSpeechUnderstanderListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult understanderResult) {
            final String text = null == understanderResult ? "" : understanderResult.getResultString();
            Log.d(TAG, "onResult: " + text);
            Result result = null;
            if (!TextUtils.isEmpty(text)) {// text 有效
                result = JsonUtils.readValue(text, Result.class);
            }

            final Speech speech = new Speech(Speech.ACTION_UNDERSTAND_END);
            String answerText;
            final String questionText;
            if (result == null) {
                questionText = TextUtils.isEmpty(text) ? "null" : text;
                answerText = Constants.getNoAnswer();// 我真的不知道啊;
            } else {// result 有效
                questionText = result.getText();

                final Answer answer = result.getAnswer();
                final List<Result> moreResults = result.getMoreResults();
                final Answer moreAnswer = moreResults != null && moreResults.size() > 0 ? moreResults.get(0).getAnswer() : null;
                final String moreText = moreAnswer == null ? "" : moreAnswer.getText();
                String service = TextUtils.isEmpty(service = result.getService()) ? "" : Constants.I_DO_NOT_KNOW + service;

                answerText = answer == null ? moreText : answer.getText(); // moreResults
                answerText = TextUtils.isEmpty(answerText) ? service : answerText; // service
            }

            // 处理特别的语音指令
            if (questionText.equals(Constants.getShutp())) { // 特别的指令
                speech.setAction(Speech.ACTION_STOP);
            } else {
                if (!TextUtils.isEmpty(answerText)) {
                    if (Constants.LANG.equals(Constants.ZH_LANG)) {
                        postSpeechEvent(speech, answerText, questionText);
                    } else if (Constants.LANG.equals(Constants.EN_LANG)) {
                        // TODO translate
                        final long salt = System.currentTimeMillis();
                        mApiService.translate(Constants.TANS_APP_ID, answerText, "zh", "en", salt, sign(Constants.TANS_APP_ID, answerText, salt, Constants.TANS_KEY))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        new Action1<TransResult>() {
                                            @Override
                                            public void call(TransResult transResult) {
                                                final List<Trans> results = transResult.getTrans_result();
                                                final int size = results == null ? 0 : results.size();
                                                final String answerText = size > 0 ? results.get(0).getDst() : "";
                                                postSpeechEvent(speech, answerText, questionText);
                                            }
                                        }, new Action1<Throwable>() {

                                            @Override
                                            public void call(Throwable throwable) {
                                                final String answerText = Constants.getNoAnswer();
                                                postSpeechEvent(speech, answerText, questionText);
                                            }
                                        }

                                );
                    }

                } else {
                    answerText = Constants.getNoAnswer();
                    postSpeechEvent(speech, answerText, questionText);
                }

            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Log.d(TAG, "onVolumeChanged: " + volume);
            if (volume > 10) {
                final Speech speech = new Speech(Speech.ACTION_UNDERSTAND_BEGINE);
                EventUtils.postEvent(new SpeechEvent(speech));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG, "onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError error) {
            final String plainDescription = error == null ? "null" : error.getPlainDescription(true);
            Log.d(TAG, "onError: " + plainDescription);
            final Speech speech = new Speech(Speech.ACTION_UNDERSTAND_ERROR);
            speech.setError(plainDescription);
            EventUtils.postEvent(new SpeechEvent(speech));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
            Log.d(TAG, "onEvent: " + eventType);
        }
    };

    //    appid+q+salt+密钥 的MD5值
    public String sign(String appid, String q, long salt, String key) {
        final String sign = DigestUtils.md5(appid + q + salt + key);
        return sign;
    }

    private SpeechUnderstander mSpeechUnderstander;  // 语义理解对象（语音到语义）。

    public void create(Context cnotext) {
        /**
         * 申请的appid时，我们为开发者开通了开放语义（语义理解）
         * 由于语义理解的场景繁多，需开发自己去开放语义平台：http://www.xfyun.cn/services/osp
         * 配置相应的语音场景，才能使用语义理解，否则文本理解将不能使用，语义理解将返回听写结果。
         */
        // 初始化对象
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(cnotext, mSpeechUdrInitListener);
        mRetrofit = RetrofitUtil.initClient(BASE_URL);
        mApiService = mRetrofit.create(ApiService.class);
        // 设置参数
        setParam();
    }

    public void destroy() {
        // 退出时释放连接
        cancelUnderstanding();
        mSpeechUnderstander.destroy();
    }

    // 开始语音理解
    public void startUnderstanding() {
        if (mSpeechUnderstander.isUnderstanding()) {// 开始前检查状态
            mSpeechUnderstander.stopUnderstanding();
            Log.d(TAG, "stopUnderstanding");
        }
        int code = mSpeechUnderstander.startUnderstanding(mSpeechUnderstanderListener);
        if (code != 0) {
            Log.d(TAG, "startUnderstanding error: " + code);
            final Speech speech = new Speech(Speech.ACTION_UNDERSTAND_ERROR);
            speech.setError(String.valueOf(code));
            EventUtils.postEvent(new SpeechEvent(speech));
        }
    }

    public boolean isUnderstanding() {
        return mSpeechUnderstander.isUnderstanding();
    }

    // 取消语义理解
    public void cancelUnderstanding() {
        Log.d(TAG, "cancelUnderstanding");
        mSpeechUnderstander.cancel();
    }

    // 停止语音理解
    public void stopUnderstanding() {
        Log.d(TAG, "stopUnderstanding");
        mSpeechUnderstander.stopUnderstanding();
    }

    public void setParam() {
        // 清空参数
        mSpeechUnderstander.setParameter(SpeechConstant.PARAMS, null);
        if (Constants.LANG.equals(Constants.ZH_LANG)) {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, Constants.ZH_LANG);
            // 设置语言区域
            mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, "mandarin");
        } else if (Constants.LANG.equals(Constants.EN_LANG)) {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, Constants.EN_LANG);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号，默认：1（有标点）
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/sud.wav");
    }

    private void postSpeechEvent(Speech speech, String answerText, String questionText) {
        answerText = TextUtils.isEmpty(answerText) ? Constants.getNoAnswer() : answerText;
        speech.setQuestion(questionText);
        speech.setAnswer(answerText);
        Log.d(TAG, "postSpeechEvent: " + questionText + "," + answerText);
        EventUtils.postEvent(new SpeechEvent(speech));
    }

}
