package cn.szlee.mail.utils;


import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <b><code>SeparateWords</code></b>
 * <p/>
 * 中文分词器
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 10:58.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@Component
public class WordsUtil {

    public static List<String> separate(String htmlMail) {
        String text = HtmlUtil.getTextFromHtml(htmlMail);
        List<Word> seg = WordSegmenter.seg(text);
        List<String> words = new ArrayList<>();
        seg.forEach(item -> words.add(item.getText()));
        return words;
    }
}
