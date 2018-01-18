package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.dao.CertMapper;
import com.company.project.model.Cert;
import com.company.project.service.CertService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by Leon Lai on 2018/01/18.
 */
@Service
@Transactional
public class CertServiceImpl extends AbstractService<Cert> implements CertService {
    @Resource
    private CertMapper certMapper;

}
