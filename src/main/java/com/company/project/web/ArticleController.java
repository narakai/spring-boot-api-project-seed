package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Article;
import com.company.project.service.ArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leon Lai on 2018/03/30.
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    public static final String NHK = "KHNPJ";
    @Resource
    private ArticleService articleService;

    RestTemplate restTemplate = new RestTemplate();

    //每10分钟启动
    @Scheduled(cron = "0 0/10 * * * ?")
    public void timerToNow() {

        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String url = "http://www3.nhk.or.jp/news/easy/news-list.json";
        String result = restTemplate.getForObject(url, String.class, new HashMap<>());
        getArticleData(result);
//        System.out.println(result);
    }

    private void getArticleData(String article) {
        List<Article> articleListDB = articleService.findAll();
        try {
            String newStr1 = article.substring(3, article.length() - 3);
            String formatString = newStr1.replace("\":[", NHK);
            //date based
            String[] stringArray = formatString.split("],");
            Map<String, List<Article>> map = new HashMap<>();
            for (int j = 0; j < stringArray.length - 1; j++) {
                String[] dateArray = stringArray[j].split(NHK);
                //date
                String date = dateArray[0].replace("\"", "");
                String formatString2 = dateArray[1].replace("},{", NHK).replace("}", "").replace("{", "");
                String[] jsonArr = formatString2.split(NHK);
                System.out.println(jsonArr[0]);
                for (String aJsonArr : jsonArr) {
                    String jsonStr = "{" + aJsonArr + "}";
                    Article articleModel = new Gson().fromJson(jsonStr, Article.class);
//                    System.out.println(articleModel.getNews_creation_time());
                    if (!articleListDB.contains(articleModel)){
                        articleService.save(articleModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/add")
    public Result add(Article article) {
        articleService.save(article);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        articleService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Article article) {
        articleService.update(article);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Article article = articleService.findById(id);
        return ResultGenerator.genSuccessResult(article);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Article> list = articleService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
