package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.PublicAppointmentDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.dto.PasscodeDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.PublicAppointment;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public PaginationDto<PublicAppointment> findPublicAppointmentList(String name, String idCard, String phone, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        int total = publicAppointmentDao.count(name, idCard, phone);
        ArrayList<PublicAppointment> list = publicAppointmentDao.findByPage(name, idCard, phone, pageNum, pageSize);
        return PaginationDto.<PublicAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    public int getTotal(String name, String idCard, String phone) {
        return publicAppointmentDao.count(name, idCard, phone);
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

    public PasscodeDto getPasscode(int appointmentID, String name, String idCard, String phone) {
        PublicAppointment appointment = getPublicAppointmentById(appointmentID);
        if (!appointment.getName().equals(name) || !appointment.getIdCard().equals(idCard) || !appointment.getPhone().equals(phone)) {
            throw new BizException(ErrorCode.PASSCODE_NOT_BELONG.getCode(), "社会公众进校预约id" + appointmentID + "的通行码不属于该用户");
        }

        if (name.length() < 3) {
            name = name.charAt(0) + "*" + name.charAt(1);
        } else {
            name = new StringBuilder(name).replace(1, name.length() - 1, "*").toString();
        }
        idCard = new StringBuilder(idCard).replace(6, 14, "********").toString();

        return PasscodeDto.builder()
                .name(name)
                .idCard(idCard)
                .startTime(appointment.getStartTime().toLocalDateTime())
                .endTime(appointment.getEndTime().toLocalDateTime().plusHours(23).plusMinutes(59).plusSeconds(59))
                .createTime(LocalDateTime.now())
                .build();
    }
}
