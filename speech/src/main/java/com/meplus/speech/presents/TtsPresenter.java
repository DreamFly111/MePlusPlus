package com.meplus.speech.presents;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.meplus.events.EventUtils;
import com.meplus.speech.Constants;
import com.meplus.speech.event.Speech;
import com.meplus.speech.event.SpeechEvent;

public class TtsPresenter {
    private static String TAG = TtsPresenter.class.getSimpleName();

    // 初始化监听。
    private final InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "ttsInitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "onInit：" + code);
                final Speech speech = new Speech(Speech.ACTION_SPEECH_ERROR);
                speech.setError(String.valueOf(code));
                EventUtils.postEvent(new SpeechEvent(speech));
            }
        }
    };

    // 合成回调监听。
    private final SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Log.d(TAG, "onSpeakBegin");
            final Speech speech = new Speech(Speech.ACTION_SPEECH_BEGIN);
            EventUtils.postEvent(new SpeechEvent(speech));
        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG, "onSpeakPaused");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG, "onSpeakResumed");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
            mPercentForBuffering = percent;
            Log.d(TAG, String.format("缓冲进度为%d%%，播放进度为%d%%", mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            Log.d(TAG, String.format("缓冲进度为%d%%，播放进度为%d%%", mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                EventUtils.postEvent(new SpeechEvent(new Speech(Speech.ACTION_SPEECH_END)));
            } else if (error != null) {
                Log.d(TAG, "onCompleted error: " + error.getErrorDescription());
                final Speech speech = new Speech(Speech.ACTION_SPEECH_ERROR);
                speech.setError(error.getPlainDescription(true));
                EventUtils.postEvent(new SpeechEvent(speech));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
            Log.d(TAG, "onEvent: " + eventType);
        }
    };

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    public void create(Context context) {
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener); // 初始化合成对象
        setParam();   // 设置参数
    }

    public void destroy() {
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
    }

    // 开始合成
    // 收到onCompleted 回调时，合成结束、生成合成音频
    // 合成的音频格式：只支持pcm格式
    public void startSpeaking(String text) {
        Log.d(TAG, "startSpeaking: " + text);
        int code = mTts.startSpeaking(text, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            Log.d(TAG, "startSpeaking error: " + code);
            final Speech speech = new Speech(Speech.ACTION_SPEECH_ERROR);
            speech.setError(String.valueOf(code));
            EventUtils.postEvent(new SpeechEvent(speech));
        }
    }

    // 取消合成
    public void stopSpeaking() {
        mTts.stopSpeaking();
    }

    // 暂停播放
    public void pauseSpeaking() {
        mTts.pauseSpeaking();
    }

    // 继续播放
    public void resumeSpeaking() {
        mTts.resumeSpeaking();
    }

    public void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {


            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, Constants.LANG.equals(Constants.ZH_LANG) ? Constants.ZH_VOICER : Constants.LANG.equals(Constants.EN_LANG) ? Constants.EN_VOICER : Constants.ZH_VOICER);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }
}
