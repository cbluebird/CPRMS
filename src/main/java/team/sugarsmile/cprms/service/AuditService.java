package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.AuditDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.util.HMACSM3Util;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AuditService {
    private final AuditDao auditDao = new AuditDao();

    public void createAudit(String operate, Audit.AuditType type, Integer adminID) {
        Audit audit = Audit.builder()
                .adminId(adminID)
                .type(type)
                .operate(operate)
                .createTime(LocalDateTime.now().withNano(0))
                .build();
        audit.setHMAC(HMACSM3Util.generateHMACSM3(adminID.toString() + type.toString() + audit.getCreateTime().toString() + audit.getType().toString()));
        auditDao.insert(audit);
    }

    public Audit getAuditById(Long id) {
        return auditDao.findById(id);
    }

    public PaginationDto<Audit> findAuditList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;

        ArrayList<Audit> list = auditDao.findByPage(pageNum, pageSize);
        for (Audit a : list) {
            if (HMACSM3Util.verifyHMACSM3(a.getAdminId().toString() + a.getType().toString() + a.getCreateTime().toString() + a.getType().toString(), a.getHMAC())) {
                a.setHMAC("正确");
            } else {
                a.setHMAC("错误");
            }
        }

        return PaginationDto.<Audit>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(auditDao.count())
                .list(list)
                .build();
    }

    public ArrayList<Audit> findAll() {
        ArrayList<Audit> list = auditDao.findAll();
        for (Audit a : list) {
            if (HMACSM3Util.verifyHMACSM3(a.getAdminId().toString() + a.getType().toString() + a.getCreateTime().toString() + a.getType().toString(), a.getHMAC())) {
                a.setHMAC("正确");
            } else {
                a.setHMAC("错误");
            }
        }
        return list;
    }

    public void updateAudit(Audit audit) {
        auditDao.update(audit);
    }

    public void deleteAudit(Long id) {
        auditDao.delete(id);
    }

    public PaginationDto<Audit> searchAudit(String operate, Integer type, Integer adminID, String operateDate, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        int total = auditDao.countForSearch(operate, type, adminID, operateDate);
        ArrayList<Audit> list = auditDao.searchAudit(operate, type, adminID, operateDate, pageNum, pageSize);
        for (Audit a : list) {
            if (HMACSM3Util.verifyHMACSM3(a.getAdminId().toString() + a.getType().toString() + a.getCreateTime().toString() + a.getType().toString(), a.getHMAC())) {
                a.setHMAC("正确");
            } else {
                a.setHMAC("错误");
            }
        }
        return PaginationDto.<Audit>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    public PaginationDto<Audit> searchAuditWithStatus(String operate, Integer type, Integer adminID, String operateDate, Integer status, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        ArrayList<Audit> list = auditDao.searchAll(operate, type, adminID, operateDate);
        ArrayList<Audit> searchList = new ArrayList<>();
        if (status == 1) {
            for (Audit a : list) {
                if (HMACSM3Util.verifyHMACSM3(a.getAdminId().toString() + a.getType().toString() + a.getCreateTime().toString() + a.getType().toString(), a.getHMAC())) {
                    a.setHMAC("正确");
                    searchList.add(a);
                }
            }
        } else {
            for (Audit a : list) {
                if (!HMACSM3Util.verifyHMACSM3(a.getAdminId().toString() + a.getType().toString() + a.getCreateTime().toString() + a.getType().toString(), a.getHMAC())) {
                    a.setHMAC("错误");
                    searchList.add(a);
                }
            }
        }

        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        end = Math.min(end, searchList.size());

        ArrayList<Audit> ansList = new ArrayList<>(searchList.subList(start, end));

        return PaginationDto.<Audit>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(searchList.size())
                .list(ansList)
                .build();
    }
}
