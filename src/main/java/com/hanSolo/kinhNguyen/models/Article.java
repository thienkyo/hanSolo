package com.hanSolo.kinhNguyen.models;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable=false,length = 200)
    private String name;

    @Column(nullable=true,columnDefinition = "TEXT")
    private String content;

    @Column(nullable=true,length = 1000)
    private String description;

    @Column(nullable=true,length = 100)
    private String thumbnail;

    @Column(nullable=true,length = 30)
    private String author;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModify;

    public Integer getId() {
        return id;
    }

    public void setId(Integer articleId) {
        this.id = articleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String articleName) {
        this.name = articleName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "Article [articleId=" + id + ", articleName=" + name + ", content=" + content
                + ", description=" + description + ", thumbnail=" + thumbnail + ", author=" + author + ", status=" + status
                + ", gmtCreate=" + gmtCreate + ", gmtModify=" + gmtModify + "]";
    }
}