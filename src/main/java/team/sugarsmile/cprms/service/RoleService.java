package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.AdminDao;
import team.sugarsmile.cprms.dao.RoleDao;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Role;
import team.sugarsmile.cprms.util.URLMatcher;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoleService {
    private final RoleDao roleDao=new RoleDao();
    public void checkPermission(String uri, Admin admin){
        ArrayList<String> role=roleDao.getRoleByType(admin.getAdminType());
        Boolean flag=false;
        for(String r:role){
           String reg=URLMatcher.convertPatternToRegex(r);
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(uri);
            if(matcher.matches()){
                flag=true;
                break;
            }
        }
        if(!flag){
            throw new BizException(ErrorCode.ADMIN_ALREADY_EXIST.getCode(), "用户" + admin.getName() + " 不存权限:"+uri);
        }
    }
}
