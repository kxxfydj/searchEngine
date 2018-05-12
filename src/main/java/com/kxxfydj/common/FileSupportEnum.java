package com.kxxfydj.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/5/10.
 */
public enum FileSupportEnum {
    Java("Java",".java"),
    C("C",".c;.h"),
    C__("C++",".cpp;.cc;.hpp"),
    C_("C#",".cs"),
    Asp("Asp",".asp"),
    Asp_net("Asp.net",".aspx"),
    PHP("PHP",".php"),
    JSP("JSP",".jsp"),
    Perl("Perl",".pl;prl"),
    Python("Python",".py"),
    Json("Json",".json"),
    JavaScript("JavaScript",".js;vue"),
    Ruby("Ruby",".RB;.RBW;.rb"),
    Lua("Lua",".lua"),
    Portable_object("Portable",".po"),
    Marckdown("Markdown",".md;.markdown;.mdown"),
    Patch_File("Patch",".patch"),
    TeX("TeX",".tex;.sty"),
    Racket("Racket",".scm;.rkt"),
    Config("Config",".conf;.cfg"),
    Qt("Qt",".ui"),
    R("R",".r;.R"),
    XHTML("XHTML",".xhtml"),
    Prolog("Prolog",".pro"),
    XML("XML",".xml"),
    Maven("Maven",".pom"),
    Windows_Resource_File("Windows Resource File",".rc"),
    PowerShell("PowerShell",".ps1"),
    TypeScript("TypeScript",".ts"),
    INI("INI",".ini"),
    WiX_source("WiX source",".wxs"),
    AspectJ("AspectJ",".aj"),
    Freemarker_Template("Freemarker",".ftl"),
    QML("QML",".qml"),
    Kotlin("Kotlin",".kt"),
    XQuery("XQuery",".xq;xquery"),
    Swift("Swift",".swift"),
    XMI("XMI",".xmi"),
    CSS("CSS",".scss;.css"),
    HTML("HTML",".html;htm"),
    HAML("MAML",".haml;ham"),
    YML("YML",".yml"),
    GO("GO",".go"),

    //未知文件格式
    Other("Other","***"),

    //不支持的文件格式
    UnsupportFile("***",".img;.png;.jpg;.jpeg;.gif;.svg");

    private static final Map<String,FileSupportEnum> suffixToLanguageMap = new HashMap<>();

    static {
        suffixToLanguageMap.put(".java",Java);
        suffixToLanguageMap.put(".c",C);
        suffixToLanguageMap.put(".h",C);
        suffixToLanguageMap.put(".cpp",C__);
        suffixToLanguageMap.put(".cc",C__);
        suffixToLanguageMap.put(".hpp",C__);
        suffixToLanguageMap.put(".cs",C_);
        suffixToLanguageMap.put(".asp",Asp);
        suffixToLanguageMap.put(".aspx",Asp_net);
        suffixToLanguageMap.put(".php",PHP);
        suffixToLanguageMap.put(".jsp",JSP);
        suffixToLanguageMap.put(".pl",Perl);
        suffixToLanguageMap.put("prl",Perl);
        suffixToLanguageMap.put(".py",Python);
        suffixToLanguageMap.put(".json",Json);
        suffixToLanguageMap.put(".js",JavaScript);
        suffixToLanguageMap.put("vue",JavaScript);
        suffixToLanguageMap.put(".RB",Ruby);
        suffixToLanguageMap.put(".RBW",Ruby);
        suffixToLanguageMap.put(".rb",Ruby);
        suffixToLanguageMap.put(".lua",Lua);
        suffixToLanguageMap.put(".po",Portable_object);
        suffixToLanguageMap.put(".md",Marckdown);
        suffixToLanguageMap.put(".markdown",Marckdown);
        suffixToLanguageMap.put(".mdown",Marckdown);
        suffixToLanguageMap.put(".patch",Patch_File);
        suffixToLanguageMap.put(".tex",TeX);
        suffixToLanguageMap.put(".sty",TeX);
        suffixToLanguageMap.put(".scm",Racket);
        suffixToLanguageMap.put(".rkt",Racket);
        suffixToLanguageMap.put(".conf",Config);
        suffixToLanguageMap.put(".cfg",Config);
        suffixToLanguageMap.put(".ui",Qt);
        suffixToLanguageMap.put(".r",R);
        suffixToLanguageMap.put(".R",R);
        suffixToLanguageMap.put(".xhtml",XHTML);
        suffixToLanguageMap.put(".pro",Prolog);
        suffixToLanguageMap.put(".xml",XML);
        suffixToLanguageMap.put(".pom",Maven);
        suffixToLanguageMap.put(".rc",Windows_Resource_File);
        suffixToLanguageMap.put(".ps1",PowerShell);
        suffixToLanguageMap.put(".ts",TypeScript);
        suffixToLanguageMap.put(".ini",INI);
        suffixToLanguageMap.put(".wxs",WiX_source);
        suffixToLanguageMap.put(".aj",AspectJ);
        suffixToLanguageMap.put(".ftl",Freemarker_Template);
        suffixToLanguageMap.put(".qml",QML);
        suffixToLanguageMap.put(".kt",Kotlin);
        suffixToLanguageMap.put(".xq",XQuery);
        suffixToLanguageMap.put("xquery",XQuery);
        suffixToLanguageMap.put(".swift",Swift);
        suffixToLanguageMap.put(".xmi",XMI);
        suffixToLanguageMap.put(".scss",CSS);
        suffixToLanguageMap.put(".css",CSS);
        suffixToLanguageMap.put(".html",HTML);
        suffixToLanguageMap.put(".htm",HTML);
        suffixToLanguageMap.put(".haml",HAML);
        suffixToLanguageMap.put(".ham",HAML);
        suffixToLanguageMap.put(".yml",YML);
        suffixToLanguageMap.put(".go",GO);

        //不支持的文件格式
        suffixToLanguageMap.put(".img",UnsupportFile);
        suffixToLanguageMap.put(".png",UnsupportFile);
        suffixToLanguageMap.put(".jpg",UnsupportFile);
        suffixToLanguageMap.put(".jpeg",UnsupportFile);
        suffixToLanguageMap.put(".gif",UnsupportFile);
        suffixToLanguageMap.put(".svg",UnsupportFile);
    }

    private String language;

    private String suffix;

    FileSupportEnum(String language, String suffix) {
        this.language = language;
        this.suffix = suffix;
    }

    public String getLanguage() {
        return language;
    }

    public static FileSupportEnum getLanguage(String filePath) {
        //获取文件后缀，判断文件格式
        int suffixIndexOf = filePath.lastIndexOf(".");
        int lastSeparatorIndexof = filePath.lastIndexOf("\\");
        //没有文件后缀的不支持
        if(suffixIndexOf == -1 || lastSeparatorIndexof >= suffixIndexOf){
            return UnsupportFile;
        }
        String fileSuffix = filePath.substring(suffixIndexOf, filePath.length());
        FileSupportEnum obj = suffixToLanguageMap.get(fileSuffix);
        if(obj == null){
            return Other;
        }
        return obj;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
