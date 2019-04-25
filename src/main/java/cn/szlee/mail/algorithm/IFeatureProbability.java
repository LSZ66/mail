package cn.szlee.mail.algorithm;

/**
 * Simple interface defining the method to calculate the feature probability.
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 * @author Philipp Nolte
 */
public interface IFeatureProbability<T, K> {

    /**
     * 返回某一已经学习并分类的<code>类别</code>中<code>特征</code>的概率
     *
     * @param feature  特征的概率
     * @param category 属于的类别
     * @return 概率 <code>p(特征|类别)</code>
     */
    float featureProbability(T feature, K category);
}
