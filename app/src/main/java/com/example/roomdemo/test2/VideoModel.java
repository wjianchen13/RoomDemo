package com.example.roomdemo.test2;

import org.json.JSONObject;

import java.io.Serializable;

public class VideoModel implements Serializable {

    private int id;
    private String anchor_id;
    private String url;
    private String android_url;
    private String is_paycall;
    private long ctime;
    private String first_photo;
    private int click;
    private int is_click;
    private int status;
    private String nickname;
    private String signature;
    private String head_image;
    private int is_follow;
    private int is_identity; // is_identity=0 待审核 1审核通过，2审核不通过，-1非主播
    private String video_price;
    private String level;
    private int is_recommend;
    private int is_show; // 0 模糊，需要vip或解锁才能看  1 正常观看
    private int is_supreme;
    private double duration;
    private int is_see;
    private String online;


    public VideoModel(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public VideoModel(JSONObject obj) {
        if(obj != null) {
            id = obj.optInt("id");
            anchor_id = obj.optString("anchor_id");
            url = obj.optString("url");
            android_url = obj.optString("android_url");
            is_paycall = obj.optString("is_paycall");
            ctime = obj.optLong("ctime");
            first_photo = obj.optString("first_photo");
            click = obj.optInt("click");
            is_click = obj.optInt("is_click");
            status = obj.optInt("status");
            is_recommend = obj.optInt("is_recommend");
            is_show = obj.optInt("is_show");
            is_supreme = obj.optInt("is_supreme");
            duration = obj.optDouble("duration");
            is_see = obj.optInt("is_see",0);
            online = obj.optString("online");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(String anchor_id) {
        this.anchor_id = anchor_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }

    public String getIs_paycall() {
        return is_paycall;
    }

    public void setIs_paycall(String is_paycall) {
        this.is_paycall = is_paycall;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getFirst_photo() {
        return first_photo;
    }

    public void setFirst_photo(String first_photo) {
        this.first_photo = first_photo;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getIs_click() {
        return is_click;
    }

    public void setIs_click(int is_click) {
        this.is_click = is_click;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getIs_identity() {
        return is_identity;
    }

    public void setIs_identity(int is_identity) {
        this.is_identity = is_identity;
    }

    public String getVideo_price() {
        return video_price;
    }

    public void setVideo_price(String video_price) {
        this.video_price = video_price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public int getIs_supreme() {
        return is_supreme;
    }

    public void setIs_supreme(int is_supreme) {
        this.is_supreme = is_supreme;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isIs_see() {
        return is_see==1;
    }

    public void setIs_see(int is_see) {
        this.is_see = is_see;
    }


    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "VideoModel{ " +
                "id=" + id +
                ", anchor_id=" + anchor_id +
                ", url=" + url +
                ", android_url='" + android_url +
                ", is_paycall='" + is_paycall +
                ", ctime=" + ctime +
                ", first_photo=" + first_photo +
                ", click=" + click +
                ", is_click=" + is_click +
                ", status=" + status +
                ", nickname=" + nickname +
                ", signature=" + signature +
                ", head_image=" + head_image +
                ", is_follow=" + is_follow +
                ", is_identity=" + is_identity +
                ", video_price=" + video_price +
                ", level=" + level +
                ", is_recommend=" + is_recommend +
                ", is_show=" + is_show +
                " }";
    }

}
