package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.dao.NoticeMapper;
import com.company.project.model.Notice;
import com.company.project.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by Leon Lai on 2018/04/01.
 */
@Service
@Transactional
public class NoticeServiceImpl extends AbstractService<Notice> implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

}
