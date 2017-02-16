package com.zenglb.framework.http.result;

/**
 * Created by zenglb on 2017/2/9.
 */
public class AreuSleepResult {
    private String topic;
    private String start_time;
    private String id;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AreuSleepBean{" +
                "topic='" + topic + '\'' +
                ", start_time='" + start_time + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
