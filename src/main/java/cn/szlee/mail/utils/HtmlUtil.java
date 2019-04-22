package cn.szlee.mail.utils;

import java.util.regex.Pattern;

/**
 * <b><code>HtmlUtil</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/4/22 21:30.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
class HtmlUtil {

    /**
     * script的正则表达式
     * style的正则表达式
     * HTML标签的正则表达式
     * 转义字符正则表达式
     */
    private static final String[] REGEX = {
            "<script[^>]*?>[\\s\\S]*?<script>",
            "<style[^>]*?>[\\s\\S]*?<style>",
            "<[^>]+>",
            "\\s*|\t|\r|\n"
    };

    /**
     * 删除Html标签
     *
     * @param htmlStr 带 html标签的字符串
     * @return 删除Html标签后的字符串
     */
    private static String delHTMLTag(String htmlStr) {
        Pattern pattern;
        for (String regex : REGEX) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            htmlStr = pattern.matcher(htmlStr).replaceAll("");
        }

        return htmlStr.trim();
    }

    /**
     * 删除字符串中的 html 标签
     *
     * @param htmlStr 带 html标签的字符串
     * @return 去掉 html标签的字符串
     */
    static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        return htmlStr;
    }
}