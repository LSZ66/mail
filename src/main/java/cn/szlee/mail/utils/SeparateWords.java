package cn.szlee.mail.utils;


import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

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
public class SeparateWords {

    public static List<String> separate(String mail) {
        List<Word> seg = WordSegmenter.seg(mail);
        List<String> words = new ArrayList<>();
        seg.forEach(item -> words.add(item.getText()));
        return words;
    }

    public static void main(String[] args) {
        System.out.println(SeparateWords.separate("你好，本期彩票开猴，你猜中了吗，我们连中四环，加微信，低价给你下期开什么"));
    }
}
