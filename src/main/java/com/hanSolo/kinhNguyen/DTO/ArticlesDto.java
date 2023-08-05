package com.hanSolo.kinhNguyen.DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ArticlesDto implements Serializable {
    private final Integer id;
    private final String name;
    private final String content;
    private final String description;
    private final String thumbnail;
    private final String author;
    private final boolean status;
    private final Date gmtCreate;
    private final Date gmtModify;

    public ArticlesDto(Integer id, String name, String content, String description, String thumbnail, String author, boolean status, Date gmtCreate, Date gmtModify) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.description = description;
        this.thumbnail = thumbnail;
        this.author = author;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public boolean getStatus() {
        return status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticlesDto entity = (ArticlesDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.content, entity.content) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.thumbnail, entity.thumbnail) &&
                Objects.equals(this.author, entity.author) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.gmtCreate, entity.gmtCreate) &&
                Objects.equals(this.gmtModify, entity.gmtModify);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, content, description, thumbnail, author, status, gmtCreate, gmtModify);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "content = " + content + ", " +
                "description = " + description + ", " +
                "thumbnail = " + thumbnail + ", " +
                "author = " + author + ", " +
                "status = " + status + ", " +
                "gmtCreate = " + gmtCreate + ", " +
                "gmtModify = " + gmtModify + ")";
    }
}
