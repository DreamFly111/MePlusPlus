package com.meplus.speech.event;

/**
 * Created by GleasonK on 6/25/15.
 */
public class Speech {
    public static final String ACTION_SPEECH_BEGIN = "speech_begin";        // speech begin!
    public static final String ACTION_SPEECH_END = "speech_end";            // speech end!
    public static final String ACTION_SPEECH_ERROR = "speech_error";        // speech error

    public static final String ACTION_UNDERSTAND_BEGINE = "understand_begin";       // understand begin
    public static final String ACTION_UNDERSTAND_END = "understand_end";            // understand end
    public static final String ACTION_UNDERSTAND_ERROR = "understand_error";        // understand error

    public static final String ACTION_STOP = "stop";        // stop

    private String question;
    private String answer;
    private String action;
    private String error;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Speech(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
