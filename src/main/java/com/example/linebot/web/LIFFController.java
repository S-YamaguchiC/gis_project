package com.example.linebot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LIFFController {

    @GetMapping("/liff")
    public String hello(Model model) {
        // [[${test}]] の部分を Hello... で書き換えて、liff.htmlを表示する
        model.addAttribute("test", "報告フォーム");
        return "liff";
    }

    //pop.html表示
    /*
    @RequestMapping("/pop")
    public String repoted() {
        return "/pop";
    }
    */

    /*
    *   報告の入力
    */
    /*
    @RequestMapping("/liff")
    public String reportInput() {
        return "/liff";
    }
    */

    /*
     *  次の画面で入力した報告の表示
     *  ※ここでサーバーに送信するなりDBに保存する
     */
    @RequestMapping("/result")
    public String pic(ModelMap modelMap,
                      @RequestParam("type")String type,
                      @RequestParam("category")String cat,
                      @RequestParam("detail")String det

    ){
        modelMap.addAttribute("type",type);
        modelMap.addAttribute("category",cat);
        modelMap.addAttribute("detail",det);
        return "/result";
    }


}
