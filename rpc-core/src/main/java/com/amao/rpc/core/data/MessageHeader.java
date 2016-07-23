package com.amao.rpc.core.data;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Created by 阿毛 on 2016/6/16.
 */
@Message
public class MessageHeader implements Serializable{

    private String sessionId;
    private int length;
    private int type;
    private byte priority;
    private String interfaceName;
    private String methodName;

    public MessageHeader(String sessionId) {
        this.sessionId = sessionId;
    }

    public MessageHeader() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "sessionId='" + sessionId + '\'' +
                ", length=" + length +
                ", type=" + type +
                ", priority=" + priority +
                ", interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
