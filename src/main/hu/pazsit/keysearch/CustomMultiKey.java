package hu.pazsit.keysearch;

/**
 * CustomMultiKey.java
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class CustomMultiKey extends SimpleMultiKey<String> implements MultiKey<String> {
    private KeyLengthRule<MultiKey<String>> keyRule;
    private KeyComparisonRelation<String> keyComparison;
    private String any;

    public CustomMultiKey(KeyLengthRule<MultiKey<String>> keyRule, KeyComparisonRelation<String> keyComparison, String any) {
        this.keyRule = keyRule;
        this.keyComparison = keyComparison;
        this.any = any;
    }

    @Override
    public String any() {
        return any;
    }

    @Override
    public KeyLengthRule<MultiKey<String>> getKeyRule() {
        return keyRule;
    }

    @Override
    public KeyComparisonRelation<String> getComparisonRelation() {
        return keyComparison;
    }
}
