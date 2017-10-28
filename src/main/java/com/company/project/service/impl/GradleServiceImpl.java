package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.dao.GradleMapper;
import com.company.project.model.Gradle;
import com.company.project.service.GradleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by Leon Lai on 2017/10/28.
 */
@Service
@Transactional
public class GradleServiceImpl extends AbstractService<Gradle> implements GradleService {
    @Resource
    private GradleMapper gradleMapper;

}
