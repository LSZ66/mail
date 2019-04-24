package cn.szlee.mail.algorithm;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;

/**
 * A basic wrapper reflecting a classification. It will store both featureSet
 * and resulting classification.
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 * @author Philipp Nolte
 */
@AllArgsConstructor
@EqualsAndHashCode
public class Classification<T, K> implements Serializable {

    /**
     * The classified featureSet.
     */
    @Getter
    private Collection<T> featureSet;

    /**
     * The category as which the featureSet was classified.
     */
    @Getter
    private K category;

    /**
     * The probability that the featureSet belongs to the given category.
     */
    @Getter
    private float probability;

    /**
     * Constructs a new Classification with the parameters given and a default
     * probability of 1.
     *
     * @param featureSet The featureSet.
     * @param category   The category.
     */
    Classification(Collection<T> featureSet, K category) {
        this(featureSet, category, 1.0f);
    }
}
