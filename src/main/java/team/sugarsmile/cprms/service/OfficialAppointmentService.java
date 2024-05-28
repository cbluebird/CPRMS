package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.OfficialAppointmentDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.OfficialAppointment;

import java.util.ArrayList;
import java.util.List;

public class OfficialAppointmentService {
    private final OfficialAppointmentDao officialAppointmentDao = new OfficialAppointmentDao();

    public void addOfficialAppointment(OfficialAppointment appointment) {
        officialAppointmentDao.insert(appointment);
    }

    public OfficialAppointment getOfficialAppointmentById(long id) {
        OfficialAppointment appointment = officialAppointmentDao.findById(id);
        if (appointment == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + id + " 不存在");
        }
        return appointment;
    }

    public void updateOfficialAppointment(OfficialAppointment appointment) {
        if (officialAppointmentDao.findById(appointment.getId()) == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + appointment.getId() + " 不存在");
        }
        officialAppointmentDao.update(appointment);
    }

    public void deleteOfficialAppointment(long id) {
        if (officialAppointmentDao.findById(id) == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + id + " 不存在");
        }
        officialAppointmentDao.delete(id);
    }

    public PaginationDto<OfficialAppointment> findOfficialAppointmentList(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        int total = officialAppointmentDao.count();
        ArrayList<OfficialAppointment> list = officialAppointmentDao.findByPage(pageNum, pageSize);
        return PaginationDto.<OfficialAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }
}
