package cn.szlee.mail.algorithm;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

/**
 * 类别
 *
 * @param <T> 特征的类型.
 * @param <K> 类别的类型.
 * @author Philipp Nolte
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Classification<T, K> implements Serializable {

    private static final long serialVersionUID = -1210981535415341283L;

    /**
     * 特征集合.
     */
    @Getter
    private Collection<T> featureSet;

    /**
     * 特征所属类别.
     */
    @Getter
    private K category;

    /**
     * 该特征集属于该类别的概率.
     */
    @Getter
    private float probability;

    /**
     * 构造一个新的分类.
     *
     * @param featureSet 特征集合.
     * @param category   特征所属类别.
     */
    Classification(Collection<T> featureSet, K category) {
        this(featureSet, category, 1.0f);
    }
}
