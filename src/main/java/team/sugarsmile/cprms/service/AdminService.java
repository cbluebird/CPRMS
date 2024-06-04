package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.AdminDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.util.SM3Util;

public class AdminService {
    private final AdminDao adminDao = new AdminDao();

    public void addAdmin(Admin.AdminType type, String phone, String name, String userName, Integer departmentID) {
        if (adminDao.findByUserName(userName) != null) {
            throw new BizException(ErrorCode.ADMIN_ALREADY_EXIST.getCode(), "用户： " + userName + " 已存在");
        }

        if (adminDao.findByUserPhone(phone) != null) {
            throw new BizException(ErrorCode.ADMIN_ALREADY_EXIST.getCode(), "用户手机： " + phone + " 已存在");
        }

        adminDao.insert(Admin.builder()
                .adminType(type)
                .phone(phone)
                .password(SM3Util.encrypt("zjut" + phone.substring(phone.length() - 6)))
                .name(name)
                .departmentID(departmentID)
                .userName(userName)
                .build());
    }

    public Admin checkAdminByPassword(String userName, String password) {
        Admin admin = adminDao.findByUserName(userName);
        if (admin == null) {
            throw new BizException(ErrorCode.ADMIN_ALREADY_EXIST.getCode(), "用户" + userName + "不存在");
        }

        if (!admin.getPassword().equals(SM3Util.encrypt(password))) {
            throw new BizException(ErrorCode.ADMIN_LOGIN_ERROR.getCode(), "用户：" + userName + " 密码错误");
        }

        return admin;
    }

    public void updatePasswordByID(Integer id, String password) {
        Admin admin = adminDao.findByID(id);
        if (admin == null) {
            throw new BizException(ErrorCode.ADMIN_NOT_EXIST.getCode(), "用户编号 " + id + " 不存在");
        }

        password = SM3Util.encrypt(password);
        if (admin.getPassword().equals(password)) {
            throw new BizException(ErrorCode.PASSWORD_UPDATE_SAME);
        }

        adminDao.updatePasswordByID(id, password);
    }

    public void updateAdmin(Integer id, Admin.AdminType type, String phone, String name, String userName, Integer departmentID) {
        if (adminDao.findByID(id) == null) {
            throw new BizException(ErrorCode.ADMIN_NOT_EXIST.getCode(), "用户编号 " + id + " 不存在");
        }

        adminDao.updateAdminInfo(Admin.builder()
                .id(id)
                .adminType(type)
                .phone(phone)
                .name(name)
                .departmentID(departmentID)
                .userName(userName)
                .build());
    }

    public PaginationDto<Admin> findAdminList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        return PaginationDto.<Admin>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(adminDao.count())
                .list(adminDao.findByPage(pageNum, pageSize))
                .build();
    }

    public PaginationDto<Admin> findAdminListByType(int pageNum, int pageSize, Admin.AdminType type) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;

        return PaginationDto.<Admin>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(adminDao.countByType(type))
                .list(adminDao.findByPageAndType(pageNum, pageSize, type))
                .build();
    }

    public PaginationDto<Admin> getSystemList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;

        return PaginationDto.<Admin>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(adminDao.countAuditAndSchoolAdmin())
                .list(adminDao.findAuditAndSchoolAdminList(pageNum, pageSize))
                .build();
    }

    public void deleteAdminByID(int id) {
        if (adminDao.findByID(id) == null) {
            throw new BizException(ErrorCode.ADMIN_NOT_EXIST.getCode(), "管理员编号 " + id + " 不存在");
        }
        adminDao.delete(id);
    }

    public Admin getAdminByID(int id) {
        Admin admin = adminDao.findByID(id);
        if (admin == null) {
            throw new BizException(ErrorCode.ADMIN_NOT_EXIST.getCode(), "管理员编号 " + id + " 不存在");
        }
        return admin;
    }
}
