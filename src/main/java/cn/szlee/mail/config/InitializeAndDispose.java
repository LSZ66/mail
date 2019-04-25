package cn.szlee.mail.config;

import cn.szlee.mail.utils.BayesUtil;
import org.apdplat.word.WordSegmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <b><code>InitializeAndDispose</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-25 11:23.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@Component
class InitializeAndDispose implements CommandLineRunner, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeAndDispose.class);

    @Override
    public void run(String... args) {
        LOGGER.info("开始初始化分词器");
        WordSegmenter.seg("你好");
        try {
            LOGGER.info("开始加载朴素贝叶斯分类器");
            BayesUtil.load();
            LOGGER.info("初始化工作完成");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("朴素贝叶斯分类器初始化加载失败", e);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("程序开始退出了");
        try {
            BayesUtil.save();
        } catch (IOException e) {
            LOGGER.error("保存贝叶斯分类器出错了", e);
        }
        LOGGER.info("保存贝叶斯分类器数据完毕，程序正常退出");
    }
}
