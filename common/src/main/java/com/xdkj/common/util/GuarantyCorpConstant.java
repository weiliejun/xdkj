package com.xdkj.common.util;

/**
 * 2015-02-02
 *
 * @author Jxy
 */
public class GuarantyCorpConstant {
    /**
     * 河北融投担保公司ID
     */
    public static final String HEBEIRONGTOU = "'14082216310376793131','14081915205953537066'";
    /**
     * 河北融投担保的借款人
     */
    public static final String HEBEIRONGTOU_BORROWER = "14080718113636293722,14080718000517441125,14082216172470165159,14081913302565581028";

    /**
     * 判断是否是河北融投公司
     *
     * @param userId 当前用户的ID
     * @return
     */
    public static boolean isHeBeiRongTou(String userId) {
        return HEBEIRONGTOU.contains(userId);
    }

    /**
     * 判断是否是河北融投担保的借款人
     *
     * @param userId 当前用户的ID
     * @return
     */
    public static boolean isHeBeiRongTouBorrower(String userId) {
        return HEBEIRONGTOU_BORROWER.contains(userId);
    }
}
