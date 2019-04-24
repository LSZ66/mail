package cn.szlee.mail.utils;

import cn.szlee.mail.algorithm.BayesClassifier;
import lombok.Getter;

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
    private static BayesClassifier<String, String> bayes = new BayesClassifier<>();
}
