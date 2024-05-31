package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.PublicAppointmentDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.model.PublicAppointment;

import java.util.ArrayList;
import java.util.List;

public class PublicAppointmentService {
    private final PublicAppointmentDao publicAppointmentDao = new PublicAppointmentDao();

    public void addPublicAppointment(PublicAppointment appointment) {
        publicAppointmentDao.insert(appointment);
    }

    public PublicAppointment getPublicAppointmentById(long id) {
        PublicAppointment appointment = publicAppointmentDao.findById(id);
        if (appointment == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + id + " 不存在");
        }
        return appointment;
    }

    public void updatePublicAppointment(PublicAppointment appointment) {
        if (publicAppointmentDao.findById(appointment.getId()) == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + appointment.getId() + " 不存在");
        }
        publicAppointmentDao.update(appointment);
    }

    public void deletePublicAppointment(long id) {
        if (publicAppointmentDao.findById(id) == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + id + " 不存在");
        }
        publicAppointmentDao.delete(id);
    }

    public PaginationDto<PublicAppointment> findPublicAppointmentList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        int total = publicAppointmentDao.count();
        ArrayList<PublicAppointment> list = publicAppointmentDao.findByPage(pageNum, pageSize);
        return PaginationDto.<PublicAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    public PaginationDto<PublicAppointment> searchAppointments(String applyDate, String appointmentDate, Integer campus, String unit, String name, String idCard, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        int total = publicAppointmentDao.countForSearch(applyDate, appointmentDate, campus, unit, name, idCard);
        ArrayList<PublicAppointment> list = publicAppointmentDao.searchAppointments(applyDate, appointmentDate, campus, unit, name, idCard, pageNum, pageSize);
        return PaginationDto.<PublicAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

}
