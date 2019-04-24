package cn.szlee.mail.config;

import cn.szlee.mail.utils.BayesUtil;
import cn.szlee.mail.utils.WordsUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <b><code>CommandLineRunnerImpl</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/4/24 23:50.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Override
    public void run(String... args) {
        String init1 = "吉祥岛首家线上赌场上线啦，性感荷官在线发牌";
        String init2 = "今日，我虽死，但还是，西楚霸王！！！";
        List<String> spam = WordsUtil.separate(init1);
        List<String> ham = WordsUtil.separate(init2);
        var bayes = BayesUtil.getBayes();
        bayes.learn("spam", spam);
        bayes.learn("ham", ham);
        System.out.println("初始化完毕");
    }
}
