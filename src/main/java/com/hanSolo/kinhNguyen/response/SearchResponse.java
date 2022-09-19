package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.Member;

public class SearchResponse extends BaseResponse{

    private String type;
    private String  name;
    private String  id;
    private String  thumbnail;

    public SearchResponse(String type,String name,String id,String thumbnail, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
