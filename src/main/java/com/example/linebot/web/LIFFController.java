package com.example.linebot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
     */
    @RequestMapping("/result")
    public String pic(ModelMap modelMap,
                      @RequestParam("type")String type,
                      @RequestParam("category")String cat,
                      @RequestParam("detail")String det,
                      @RequestParam("filename")String fname,
                      @RequestParam("file") MultipartFile mfile
    ){
        modelMap.addAttribute("type",type);
        modelMap.addAttribute("category",cat);
        modelMap.addAttribute("detail",det);
        modelMap.addAttribute("filename",fname);

        //画像をローカルに保存
        Optional<String> opt;
        opt = makeTmpFile(mfile, ".jpg");
        String path = opt.orElseGet(()->"ファイル書き込みNG");
        System.out.println("ファイルの保存先->" + path);

        //ローカルに保存した画像のBase64変換
        File f = new File(path);

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
