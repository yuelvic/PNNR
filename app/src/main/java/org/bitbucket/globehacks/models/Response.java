package org.bitbucket.globehacks.models;

/**
 * Created by Emmanuel  Victor Garcia on 23/07/2017.
 */

public class Response {

    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
