package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.AdminDao;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.util.SM4;

public class AdminService {
    private final AdminDao adminDao=new AdminDao();
    public void addAdmin(Admin.AdminType type, String phone,String name,Integer departmentID) {
        if (adminDao.findByPhone(phone) != null) {
            throw new BizException(ErrorCode.ADMIN_ALREADY_EXIST.getCode(), "用户： " + phone + " 已存在");
        }

        String pass= SM4.encryptSm4("zjut123456");

        adminDao.insert(Admin.builder()
                .adminType(type)
                .phone(phone)
                .password(pass)
                .name(name)
                .departmentID(departmentID)
                .build());
    }

    public Admin checkAdminByPassword(String phone,String password){

        Admin admin=adminDao.findByPhone(phone);
        if (admin != null) {
            throw new BizException(ErrorCode.ADMIN_ALREADY_EXIST.getCode(), "用户： " + phone + " 已存在");
        }

        String pass=SM4.decryptSm4(admin.getPassword());

        if(!pass.equals(password)){
            throw new BizException(ErrorCode.ADMIN_LOGIN_ERROR.getCode(), "用户： " + phone + " 密码错误");
        }

        admin.setPassword(pass);
        
        return admin;
    }

}
