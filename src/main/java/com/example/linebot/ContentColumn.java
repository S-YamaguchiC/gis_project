package com.example.linebot;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

import java.util.Arrays;
import java.util.List;
/*
* 種別選択で選ばれたものに対応する報告内容のカルーセルカラムたち
*
* */

public class ContentColumn {

    //からむ1
    public CarouselColumn carouselColumn1() {

        String title = "報告内容選択";
        String text = "種別「舗装」での内容を選択して";

        Action a = new PostbackAction("道路に穴","hole");
        Action b = new PostbackAction("陥没","subsidence");
        Action c = new PostbackAction("爆発","explosion");

        List<Action> actions = Arrays.asList(a, b, c);

        return new CarouselColumn("https://puu.sh/BbfNS/c2fa6e5411.jpg",title,text,actions);
    }

    //からむ2
    public CarouselColumn carouselColumn2() {

        String title = "報告内容選択";
        String text = "種別「舗装」での内容を選択して";

        Action a = new PostbackAction("見通しが悪い","unclear");
        Action b = new PostbackAction("歩道がない","walk");
        Action c = new PostbackAction("その他","other");

        List<Action> actions = Arrays.asList(a, b, c);

        return new CarouselColumn("https://puu.sh/BbfNS/c2fa6e5411.jpg",title,text,actions);
    }
}
