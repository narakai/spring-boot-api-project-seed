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
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    //test 1 min
    @Scheduled(cron = "0 0/5 * * * 1-5")
    public void timerToNow() {

//        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String url = "http://www3.nhk.or.jp/news/easy/news-list.json";
        String result = restTemplate.getForObject(url, String.class, new HashMap<>());
        getArticleData(result);
//        runShell("k10011351191000");
//        runShell("k10011382561000");
//        System.out.println(result);
    }

    private void getArticleData(String article) {
        List<Article> articleListDB = articleService.findAll();
        List<Article> articleList = new ArrayList<>();
        try {
            String newStr1 = article.substring(3, article.length() - 3);
            String formatString = newStr1.replace("\":[", NHK);
            //date based
            String[] stringArray = formatString.split("],");
            for (int j = 0; j < stringArray.length - 1; j++) {
                String[] dateArray = stringArray[j].split(NHK);
                String formatString2 = dateArray[1].replace("},{", NHK).replace("}", "").replace("{", "");
                String[] jsonArr = formatString2.split(NHK);
                for (String aJsonArr : jsonArr) {
                    String jsonStr = "{" + aJsonArr + "}";
                    Article articleModel = new Gson().fromJson(jsonStr, Article.class);
                    if (!articleListDB.contains(articleModel)) {
                        articleList.add(articleModel);
                        articleService.save(articleModel);
                    }
                }
            }
            if (articleList.size() > 0) {
                for (Article article1 : articleList) {
//                    System.out.println(" article1 " + article1.getNews_id());
                    runShell(article1.getNews_id());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runShell(String newsId) {
        String returnCode = "";
        try {
//            /Users/laileon/Documents/test.sh
//            Process process = Runtime.getRuntime().exec("chmod 755 /home/ffmpeg.sh");
//            process.waitFor();
            // test2.sh是要执行的shell文件，param1参数值，test2.sh和param1之间要有空格
            // 多个参数可以在param1后面继续增加，但不要忘记空格！！
            System.out.println("IM IN");
            Process process = Runtime.getRuntime().exec("/home/ffmpeg.sh" + " " + newsId);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            printMessage(process.getInputStream());
            printMessage(process.getErrorStream());
            returnCode = process.waitFor() + "";
            System.out.println("code " + returnCode);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void printMessage(final InputStream input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Reader reader = new InputStreamReader(input);
                BufferedReader bf = new BufferedReader(reader);
                String line = null;
                try {
                    while ((line = bf.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
