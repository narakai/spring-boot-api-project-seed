package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.Blog;
import com.company.project.service.BlogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2017/07/10.
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private BlogService blogService;

    @PostMapping("/add")
    public Result add(@RequestBody Blog blog) {
        try {
            blogService.save(blog);
            return ResultGenerator.genSuccessResult();
        } catch (DataAccessException e) {
            return ResultGenerator.genFailResult("Failed to add");
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        try {
            blogService.deleteById(id);
            return ResultGenerator.genSuccessResult();
        } catch (DataAccessException e) {
            return ResultGenerator.genFailResult("Failed to delete");
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Blog blog) {
        try {
            blogService.update(blog);
            return ResultGenerator.genSuccessResult();
        } catch (DataAccessException e) {
            return ResultGenerator.genFailResult("Failed to update");
        }
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            throw new ServiceException("can not find blog");
        } else {
            return ResultGenerator.genSuccessResult(blog);
        }
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        try {
            PageHelper.startPage(page, size);
            List<Blog> list = blogService.findAll();
            PageInfo pageInfo = new PageInfo(list);
            return ResultGenerator.genSuccessResult(pageInfo);
        } catch (DataAccessException e) {
            return ResultGenerator.genFailResult("Could not find blog list");
        }
    }
}
