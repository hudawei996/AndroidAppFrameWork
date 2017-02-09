package com.zenglb.framework.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * Created by zenglb on 2017/01/04.
 */
@Entity
public class Messages {

    /**
     * content :
     * action_type : task
     * image :
     * title : 任务发布
     * read : true
     * message : 【可抢单】一个新任务可以抢"uiuuiu"
     * created : 2017-01-22 18:23:29
     * id : 40130772
     * classify : 任务通知
     * action_id : 2017012200011482
     */

    private String content;
    private String action_type;
    private String image;
    private String title;
    private boolean read;
    private String message;
    private String created;
    private int id;
    private String classify;
    private String action_id;


    @Generated(hash = 999647109)
    public Messages(String content, String action_type, String image, String title,
            boolean read, String message, String created, int id, String classify,
            String action_id) {
        this.content = content;
        this.action_type = action_type;
        this.image = image;
        this.title = title;
        this.read = read;
        this.message = message;
        this.created = created;
        this.id = id;
        this.classify = classify;
        this.action_id = action_id;
    }

    @Generated(hash = 826815580)
    public Messages() {
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "content='" + content + '\'' +
                ", action_type='" + action_type + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", read=" + read +
                ", message='" + message + '\'' +
                ", created='" + created + '\'' +
                ", id=" + id +
                ", classify='" + classify + '\'' +
                ", action_id='" + action_id + '\'' +
                '}';
    }

    public boolean getRead() {
        return this.read;
    }
}