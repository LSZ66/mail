package cn.szlee.mail.utils;

import cn.szlee.mail.algorithm.BayesClassifier;
import cn.szlee.mail.config.Constant;
import lombok.Getter;

import java.io.*;
import java.util.Arrays;

/**
 * <b><code>BayesUtil</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/4/24 23:53.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
public class BayesUtil {

    @Getter
    private static BayesClassifier<String, String> bayes;

    @SuppressWarnings("unchecked")
    public static void load() throws IOException, ClassNotFoundException {
        // 文件的读取
        File file = new File("/Users/lsz/data.bayes");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        //在输入流中读取对象
        bayes = (BayesClassifier<String, String>) ois.readObject();
    }

    public static void save() throws IOException {
        // 文件的读取
        File file = new File("/Users/lsz/data.bayes");
        // 把对象写入到文件中，使用ObjectOutputStream
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        // 把对象写入到文件中
        oos.writeObject(bayes);
    }

    public static void main(String[] args) throws IOException {
        bayes = new BayesClassifier<>();
        String[] spam = "线上 赌场 上线 性感 荷官 在线 发牌 博彩 体验 点击 网站 进入 发票 免税 低价".split("\\s");
        String[] ham = "梁朝铭 教授 您好 李尚哲 今年 报考 研究生 暨南 大学 成绩 英语 数学 专业 感兴趣 深造 感谢 今日 我虽死 但 还是 西楚 霸王".split("\\s");
        bayes.learn(Constant.SPAM, Arrays.asList(spam));
        bayes.learn(Constant.HAM, Arrays.asList(ham));
        save();
    }
}
