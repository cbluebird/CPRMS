package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.RoleDao;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.util.URLMatcherUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoleService {
    private final RoleDao roleDao = new RoleDao();

    public void checkPermission(String url, Admin admin) {
        ArrayList<String> role = roleDao.getRoleByType(admin.getAdminType());
        boolean flag = false;
        for (String r : role) {
            String reg = URLMatcherUtil.convertPatternToRegex(r);
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new BizException(ErrorCode.PERMISSION_DENIED.getCode(), "用户 " + admin.getName() + " 不存在权限: " + url);
        }
    }
}
