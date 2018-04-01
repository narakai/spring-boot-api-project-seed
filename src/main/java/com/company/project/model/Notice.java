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

    private String notice;

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
     * @return notice
     */
    public String getNotice() {
        return notice;
    }

    /**
     * @param notice
     */
    public void setNotice(String notice) {
        this.notice = notice;
    }
}