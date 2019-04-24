package cn.szlee.mail.algorithm;

import java.util.Arrays;

public class RunnableExample {

    public static void main(String[] args) {

        /*
         * Create a new classifier instance. The context features are
         * Strings and the context will be classified with a String according
         * to the featureset of the context.
         */
        final AbstractClassifier<String, String> bayes = new BayesClassifier<>();

        /*
         * The classifier can learn from classifications that are handed over
         * to the learn methods. Imagin a tokenized text as follows. The tokens
         * are the text's features. The category of the text will either be
         * positive or negative.
         */
        final String[] positiveText = "澳门 首家 线上 赌场 上线 性感 荷官 在线 发牌 不一样 游戏 体验".split("\\s");
        bayes.learn("spam", Arrays.asList(positiveText));

        final String[] negativeText = "崔教授 您好 李尚哲 报考 研究生 暨南 大学 上线 数学 专业课 计算机 网络".split("\\s");
        bayes.learn("ham", Arrays.asList(negativeText));

        /*
         * Now that the classifier has "learned" two classifications, it will
         * be able to classify similar sentences. The classify method returns
         * a Classification Object, that contains the given featureset,
         * classification probability and resulting category.
         */
        final String[] unknownText1 = "亚洲 顶级 线上 博彩 公司".split("\\s");
        final String[] unknownText2 = "刘波 教授 您好 梁朝铭 上线 专业课 深造".split("\\s");

        System.out.println( // will output "positive"
                bayes.classify(Arrays.asList(unknownText1)).getCategory());
        System.out.println( // will output "negative"
                bayes.classify(Arrays.asList(unknownText2)).getCategory());
    }

}
