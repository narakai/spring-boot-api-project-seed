package com.company.project.schedule;

import com.company.project.model.Article;
import com.company.project.service.ArticleService;
import com.google.gson.Gson;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class ScheduledTasks {
    private static final String NHK = "KHNPJ";
    @Resource
    private ArticleService articleService;

    private RestTemplate restTemplate = new RestTemplate();

    //test 1 min
    @Scheduled(cron = "0 35 10 * * 1-5")
    public void timerToNow1() {

        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String url = "http://www3.nhk.or.jp/news/easy/news-list.json";
        String result = restTemplate.getForObject(url, String.class, new HashMap<>());
        getArticleData(result);
//        runShell("k10011351191000");
//        runShell("k10011382561000");
//        System.out.println(result);
    }

    //test 1 min
    @Scheduled(cron = "0 35 14 * * 1-5")
    public void timerToNow2() {

        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String url = "http://www3.nhk.or.jp/news/easy/news-list.json";
        String result = restTemplate.getForObject(url, String.class, new HashMap<>());
        getArticleData(result);
//        runShell("k10011351191000");
//        runShell("k10011382561000");
//        System.out.println(result);
    }

    private void getArticleData(String article) {
        List<Article> articleListDB = articleService.findAll();
        System.out.println("DB " + articleListDB.size());
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
                System.out.println("NET " + jsonArr.length);
                for (String aJsonArr : jsonArr) {
                    String jsonStr = "{" + aJsonArr + "}";
                    Article articleModel = new Gson().fromJson(jsonStr, Article.class);
                    System.out.println("NewsID: " + articleModel.getNews_id());
                    if (!articleListDB.contains(articleModel)) {
                        articleList.add(articleModel);
                        articleService.save(articleModel);
                    }
                }
            }
            if (articleList.size() > 0) {
                for (Article article1 : articleList) {
                    System.out.println(" article1 " + article1.getNews_id());
                    runShell(article1.getNews_id());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private synchronized void runShell(String newsId) {
        int returnCode = -1;
        try {
//            /Users/laileon/Documents/test.sh
//            Process process = Runtime.getRuntime().exec("chmod 755 /home/ffmpeg.sh");
//            process.waitFor();
            // test2.sh是要执行的shell文件，param1参数值，test2.sh和param1之间要有空格
            // 多个参数可以在param1后面继续增加，但不要忘记空格！！
            System.out.println("IM IN");
//            String cmd = "/home/ffmpeg.sh" + " " + newsId;
//            ProcessBuilder builder = new ProcessBuilder("bash", "-c",cmd);
            String[] command = {"/home/ffmpeg.sh", newsId};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = builder.start();
            returnCode = process.waitFor();
            System.out.println("code " + returnCode);
//            Thread.sleep(30000);
//            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            br.close();
//            printMessage(process.getInputStream());
//            printMessage(process.getErrorStream());
//            returnCode = process.waitFor();
//            System.out.println("code " + returnCode);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
