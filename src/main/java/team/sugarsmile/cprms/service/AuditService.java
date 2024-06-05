package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.AuditDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.util.HMACSM3Util;

import java.sql.Date;
import java.util.ArrayList;

public class AuditService {
    private final AuditDao auditDao = new AuditDao();

    public void createAudit(String operate, Audit.AuditType type, Integer adminID) {
        Audit audit = Audit.builder().adminId(adminID).type(type).operate(operate).createTime(new Date(System.currentTimeMillis())).build();
        audit.setHMAC(HMACSM3Util.generateHMACSM3(adminID.toString() + type.toString() + audit.getCreateTime().toString()));
        auditDao.insert(audit);
    }

    public Audit getAuditById(Long id) {
        return auditDao.findById(id);
    }

    public PaginationDto<Audit> findAuditList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;

        ArrayList<Audit> list = auditDao.findAll(pageNum, pageSize);
        for (Audit a : list) {
            if (HMACSM3Util.verifyHMACSM3(a.getAdminId().toString() + a.getType().toString() + a.getCreateTime().toString(), a.getHMAC()))
                a.setHMAC("正确");
            else a.setHMAC("错误");
        }

        return PaginationDto.<Audit>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(auditDao.count())
                .list(list)
                .build();
    }

    public void updateAudit(Audit audit) {
        auditDao.update(audit);
    }

    public void deleteAudit(Long id) {
        auditDao.delete(id);
    }

    public PaginationDto<Audit> searchAppointments(String operate, Integer type, Integer adminID, String createDate, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        int total = auditDao.countForSearch(operate, type, adminID, createDate);
        ArrayList<Audit> list = auditDao.searchAudit(operate, type, adminID, createDate, pageNum, pageSize);
        for (Audit a : list) {
            if (HMACSM3Util.verifyHMACSM3(a.getAdminId().toString() + a.getType().toString() + a.getCreateTime().toString(), a.getHMAC()))
                a.setHMAC("正确");
            else a.setHMAC("错误");
        }
        return PaginationDto.<Audit>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }
}
