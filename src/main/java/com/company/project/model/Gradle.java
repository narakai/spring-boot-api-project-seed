package com.company.project.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Gradle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fragment;

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
     * @return fragment
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * @param fragment
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}