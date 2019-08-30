package com.company.project.schedule;

import com.company.project.model.Article;
import com.company.project.service.ArticleService;
import com.google.api.client.util.DateTime;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.cloud.storage.*;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@Component
@EnableAsync
public class ScheduledTasks {
    private static final String NHK = "KHNPJ";
    @Resource
    private ArticleService articleService;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

//    @Scheduled(cron = "0/10 * * * * 1-7")
    public void getAudioFile() throws Exception {
//        uploadFile2("/Users/laileon/Documents/ftp/test2.flac", "test2.flac");
        asyncRecognizeGcs("gs://nhknews-8bad0.appspot.com/test.flac");
    }

    public static String uploadFile2(String uri, String fileName) throws IOException {
        Path path = Paths.get(uri);
        String contentType = "audio/x-flac";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(fileName,
                fileName, contentType, content);


        Storage storage = StorageOptions.getDefaultInstance().getService();
        //文件名
//        final String fileName = result.getOriginalFilename().substring(result.getOriginalFilename().indexOf("."));
//        System.err.println("fileName:" + fileName);

        //上传文件
        BlobInfo blobInfo =
                storage.create(
                        BlobInfo
                                .newBuilder("nhknews-8bad0.appspot.com", fileName)
                                // Modify access list to allow all users with link to read file
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .setContentType("audio/x-flac")
                                .build(),
                        result.getInputStream());

        //System.out.println("下载连接："+blobInfo.getMediaLink());
        String fileUrl = "https://storage.googleapis.com/" + "nhknews-8bad0.appspot.com" + "/" + fileName;
        System.out.println(fileUrl);
        return fileUrl;
    }


    /**
     * Performs non-blocking speech recognition on remote FLAC file and prints the transcription.
     *
     * @param gcsUri the path to the remote LINEAR16 audio file to transcribe.
     */
    public static void asyncRecognizeGcs(String gcsUri) throws Exception {
        // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
        try (SpeechClient speech = SpeechClient.create()) {

            // Configure remote file request for Linear16
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                            .setLanguageCode("ja-JP")
                            .setEnableAutomaticPunctuation(true)
                            .setSampleRateHertz(16000)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

            // Use non-blocking call for getting file transcription
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                    speech.longRunningRecognizeAsync(config, audio);
            while (!response.isDone()) {
                System.out.println("Waiting for response...");
                Thread.sleep(10000);
            }

            List<SpeechRecognitionResult> results = response.get().getResultsList();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Users/laileon/Documents/trans.txt"))) {
                for (SpeechRecognitionResult result : results) {
                    // There can be several alternative transcripts for a given chunk of speech. Just use the
                    // first (most likely) one here.
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    System.out.printf("Transcription: %s\n", alternative.getTranscript());
                    writer.write(alternative.getTranscript());
                    writer.write("\n");
                }
            } catch (IOException e) {
            }
        }
}

    //test 1 min
    @Scheduled(cron = "0 0/10 10-18 * * 1-5")
    public void timerToNow1() throws InterruptedException, ExecutionException, TimeoutException {
        Future<Object> result = doSomething();
        result.get(3000000, TimeUnit.MILLISECONDS);
    }

//    @Scheduled(cron = "0 0/30 * * * *")
//    public void scrapyAsahi() {
//        runShell2("asahi.sh");
//    }
//
//    @Scheduled(cron = "0 0/45 * * * *")
//    public void scrapyAsahiEditorial() {
//        runShell2("asahi_editorial.sh");
//    }

    //test 1 min
//    @Scheduled(cron = "0 35 14 * * 1-5")
//    public void timerToNow2() throws InterruptedException, ExecutionException, TimeoutException {
//        Future<Object> result = doSomething();
//        result.get(3000000, TimeUnit.MILLISECONDS);
//    }

    @Async
    Future<Object> doSomething() {
        return (Future<Object>) executor.submit(() -> {
            runTask();
        });
    }

    private int runTask() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String url = "https://www3.nhk.or.jp/news/easy/news-list.json";
        String result = restTemplate.getForObject(url, String.class, new HashMap<>());
        return getArticleData(result);
    }

    private int getArticleData(String article) {
        int returnCode = -1;
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
                    returnCode += runShell(article1.getNews_id());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return returnCode;
    }

    private synchronized int runShell(String newsId) {
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
        return returnCode;
    }

//    private synchronized int runShell2(String shell) {
//        int returnCode = -1;
//        try {
//            String[] command = {"/home/python/" + shell};
//            ProcessBuilder builder = new ProcessBuilder(command);
//            builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
//            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
//            Process process = builder.start();
//            returnCode = process.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return returnCode;
//    }
}
