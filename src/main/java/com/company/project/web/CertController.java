package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Cert;
import com.company.project.service.CertService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Leon Lai on 2018/01/18.
 */
@RestController
@RequestMapping("/cert")
public class CertController {
    @Resource
    private CertService certService;

    @PostMapping("/detail")
    public Result detail(@RequestParam String cert) {
        Cert getCert = certService.findBy("cert", cert);
        if (getCert != null) {
            return ResultGenerator.genSuccessResult(true);
        } else {
            return ResultGenerator.genFailResult(null);
        }
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Cert> list = certService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
