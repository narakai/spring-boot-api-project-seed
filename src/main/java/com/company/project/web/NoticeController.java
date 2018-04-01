package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Notice;
import com.company.project.service.NoticeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by Leon Lai on 2018/04/01.
*/
@RestController
@RequestMapping("/notice")
public class NoticeController {
@Resource
private NoticeService noticeService;

@PostMapping("/add")
public Result add(Notice notice) {
noticeService.save(notice);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/delete")
public Result delete(@RequestParam Integer id) {
noticeService.deleteById(id);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/update")
public Result update(Notice notice) {
noticeService.update(notice);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/detail")
public Result detail(@RequestParam Integer id) {
Notice notice = noticeService.findById(id);
return ResultGenerator.genSuccessResult(notice);
}

@PostMapping("/list")
public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<Notice> list = noticeService.findAll();
PageInfo pageInfo = new PageInfo(list);
return ResultGenerator.genSuccessResult(pageInfo);
}
}
