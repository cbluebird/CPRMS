package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.AuditDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.model.Audit;

import java.util.ArrayList;

public class AuditService {
    private final AuditDao auditDao = new AuditDao();

    public void createAudit(String operate, Audit.AuditType type, Integer adminID) {
        Audit audit = Audit.builder().adminId(adminID).type(type).operate(operate).build();
        auditDao.insert(audit);
    }

    public Audit getAuditById(Long id) {
        return auditDao.findById(id);
    }

    public PaginationDto<Audit> findAuditList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;

        return PaginationDto.<Audit>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(auditDao.count())
                .list(auditDao.findAll(pageNum, pageSize))
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
        return PaginationDto.<Audit>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }
}
