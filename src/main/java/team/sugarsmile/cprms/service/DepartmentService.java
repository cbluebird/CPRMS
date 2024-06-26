package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.DepartmentDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Department;

import java.util.ArrayList;

/**
 * @author XiMo
 */

public class DepartmentService {
    private final DepartmentDao departmentDao = new DepartmentDao();

    public PaginationDto<Department> findDepartmentList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        return PaginationDto.<Department>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(departmentDao.count())
                .list(departmentDao.findByPage(pageNum, pageSize))
                .build();
    }

    public ArrayList<Department> getAll() {
        return departmentDao.getAll();
    }

    public void addDepartment(Department.DepartmentType type, String name, Boolean social, Boolean official) {
        if (departmentDao.findByName(name) != null) {
            throw new BizException(ErrorCode.DEPARTMENT_ALREADY_EXIST.getCode(), "部门 " + name + " 已存在");
        }
        departmentDao.insert(Department.builder()
                .departmentType(type)
                .name(name)
                .social(social)
                .official(official)
                .build());
    }

    public void deleteDepartment(int id) {
        if (departmentDao.findById(id) == null) {
            throw new BizException(ErrorCode.DEPARTMENT_NOT_EXIST.getCode(), "部门编号 " + id + " 不存在");
        }
        departmentDao.delete(id);
    }

    public void updateDepartmentNameAndType(int id, Department.DepartmentType type, String name, Boolean social, Boolean official) {
        if (departmentDao.findById(id) == null) {
            throw new BizException(ErrorCode.DEPARTMENT_NOT_EXIST.getCode(), "部门编号 " + id + " 不存在");
        }
        Department department = departmentDao.findByName(name);
        if (department != null && !department.getId().equals(id)) {
            throw new BizException(ErrorCode.DEPARTMENT_ALREADY_EXIST.getCode(), "部门 " + name + " 已存在");
        }
        departmentDao.updateNameAndType(Department.builder()
                .id(id)
                .departmentType(type)
                .name(name)
                .social(social)
                .official(official)
                .build());
    }
}
