package com.example.linebot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@Controller
public class LIFFController {

    @GetMapping("/liff")
    public String hello(Model model) {
        // [[${test}]] の部分を Hello... で書き換えて、liff.htmlを表示する
        model.addAttribute("test", "報告フォーム");
        return "liff";
    }


    /*
     *  次の画面で入力した報告の表示
     *  ※ここでサーバーに送信するなりDBに保存する
     *
     */

    /*
    * @param type 報告の種別
    * @param cat 報告の内容
    * @param det 詳細記入
    * @param fname 送信するファイル名
    * @param locate 送信する位置情報
    * @param mfile Base64変換するファイルのバイナリデータ?
    *
    * */
    @RequestMapping("/result")
    public String pic(ModelMap modelMap,
                      @RequestParam("type")String type,
                      @RequestParam("category")String cat,
                      @RequestParam("detail")String det,
                      @RequestParam("filename")String fname,
                      @RequestParam("location")String locate,
                      @RequestParam("file") MultipartFile mfile
    ) throws IOException {
        modelMap.addAttribute("type",type);
        modelMap.addAttribute("category",cat);
        modelMap.addAttribute("detail",det);
        modelMap.addAttribute("filename",fname);
        modelMap.addAttribute("location", locate);

        boolean exist = false;

        //nullチェック
        if(mfile.isEmpty()) {
            //画像がなかった場合は画像の変換がいらないのでそのままresultに遷移
            return "/result";
        }

        //画像をローカルに保存
        Optional<String> opt;
        opt = makeTmpFile(mfile, ".jpg");
        String path = opt.orElseGet(()->"ファイル書き込みNG");
        System.out.println("ファイルの保存先->" + path);

        //ローカルに保存した画像のBase64変換
        File f = new File(path);
        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long fileSize = f.length();
        boolean readable = false;

        do {
            byte[] bytes = new byte[1];
            fis.read(bytes);

            baos.write(bytes,0,bytes.length);

            long readSize = fis.getChannel().position();
            readable = readSize != fileSize;
        } while(readable);

        //base64にした画像データ
        String base64encodingStr = Base64.getEncoder().encodeToString(baos.toByteArray());
        modelMap.addAttribute("base64","data:image/png;base64," + base64encodingStr);

        //画像がNULLじゃないはずなので、trueにして送信
        exist = true;
        modelMap.addAttribute("exist",exist);
        //画像のリサイズ


        return "/result";
    }

    // MultipartFileのいんぷっとストリームを、拡張子を指定してファイルに書き込む
    // また保存先のファイルパスをOptional型で返す
    private Optional<String> makeTmpFile(MultipartFile multipartFile, String extention) {

        try(InputStream is = multipartFile.getInputStream()){
            Path tmpFilePath = Files.createTempFile("liff", extention);
            Files.copy(is, tmpFilePath,REPLACE_EXISTING);
            return Optional.ofNullable(tmpFilePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
