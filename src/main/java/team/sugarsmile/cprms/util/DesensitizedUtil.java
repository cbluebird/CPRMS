package team.sugarsmile.cprms.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author XiMo
 */

public class DesensitizedUtil {
    public static String name(String name) {
        if (name.length() == 2) {
            return name.charAt(0) + "*" + name.charAt(1);
        }
        return StrUtil.hide(name, 1, name.length() - 1);
    }

    public static String idCard(String idCard) {
        return cn.hutool.core.util.DesensitizedUtil.idCardNum(idCard, 6, 4);
    }

    public static String phone(String phone) {
        return cn.hutool.core.util.DesensitizedUtil.mobilePhone(phone);
    }
}
