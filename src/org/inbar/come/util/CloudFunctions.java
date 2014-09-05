package org.inbar.come.util;

/**
 * Created by inbar on 03/09/14.
 */
public enum CloudFunctions {
    SEND_FOLLOW_REQUEST("sendFollowRequest");

    private String functionName;

    private CloudFunctions(String functionName) {

        this.functionName = functionName;
    }


    public String getFunctionName() {

        return functionName;
    }


}
