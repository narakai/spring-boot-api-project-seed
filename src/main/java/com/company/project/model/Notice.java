package com.company.project.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "major_update")
    private Boolean majorUpdate;

    private String version;

    @Column(name = "desc_en")
    private String descEn;

    @Column(name = "desc_cn")
    private String descCn;

    @Column(name = "desc_tw")
    private String descTw;

    @Column(name = "notice_en")
    private String noticeEn;

    @Column(name = "notice_id")
    private Integer noticeId;

    @Column(name = "notice_cn")
    private String noticeCn;

    @Column(name = "notice_tw")
    private String noticeTw;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return major_update
     */
    public Boolean getMajorUpdate() {
        return majorUpdate;
    }

    /**
     * @param majorUpdate
     */
    public void setMajorUpdate(Boolean majorUpdate) {
        this.majorUpdate = majorUpdate;
    }

    /**
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return desc_en
     */
    public String getDescEn() {
        return descEn;
    }

    /**
     * @param descEn
     */
    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    /**
     * @return desc_cn
     */
    public String getDescCn() {
        return descCn;
    }

    /**
     * @param descCn
     */
    public void setDescCn(String descCn) {
        this.descCn = descCn;
    }

    /**
     * @return desc_tw
     */
    public String getDescTw() {
        return descTw;
    }

    /**
     * @param descTw
     */
    public void setDescTw(String descTw) {
        this.descTw = descTw;
    }

    /**
     * @return notice_en
     */
    public String getNoticeEn() {
        return noticeEn;
    }

    /**
     * @param noticeEn
     */
    public void setNoticeEn(String noticeEn) {
        this.noticeEn = noticeEn;
    }

    /**
     * @return notice_id
     */
    public Integer getNoticeId() {
        return noticeId;
    }

    /**
     * @param noticeId
     */
    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    /**
     * @return notice_cn
     */
    public String getNoticeCn() {
        return noticeCn;
    }

    /**
     * @param noticeCn
     */
    public void setNoticeCn(String noticeCn) {
        this.noticeCn = noticeCn;
    }

    /**
     * @return notice_tw
     */
    public String getNoticeTw() {
        return noticeTw;
    }

    /**
     * @param noticeTw
     */
    public void setNoticeTw(String noticeTw) {
        this.noticeTw = noticeTw;
    }
}