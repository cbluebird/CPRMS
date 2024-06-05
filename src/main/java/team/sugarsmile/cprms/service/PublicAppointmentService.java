package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.PublicAppointmentDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.dto.PasscodeDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.PublicAppointment;
import team.sugarsmile.cprms.util.DesensitizedUtil;
import team.sugarsmile.cprms.util.SM4Util;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PublicAppointmentService {
    private final PublicAppointmentDao publicAppointmentDao = new PublicAppointmentDao();

    public void addPublicAppointment(PublicAppointment appointment) {
        encrypt(appointment);
        publicAppointmentDao.insert(appointment);
    }

    public PublicAppointment getPublicAppointmentById(long id) {
        PublicAppointment appointment = publicAppointmentDao.findById(id);
        if (appointment == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + id + " 不存在");
        }
        decrypt(appointment);
        return appointment;
    }

    public void updatePublicAppointment(PublicAppointment appointment) {
        if (publicAppointmentDao.findById(appointment.getId()) == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + appointment.getId() + " 不存在");
        }
        encrypt(appointment);
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
        for (PublicAppointment appointment : list) {
            decryptAndDesensitized(appointment);
        }
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
        if (idCard != null && !idCard.isEmpty()) {
            idCard = SM4Util.encrypt(idCard);
        }
        if (phone != null && !phone.isEmpty()) {
            phone = SM4Util.encrypt(phone);
        }

        int total = publicAppointmentDao.count(name, idCard, phone);
        ArrayList<PublicAppointment> list = publicAppointmentDao.findByPage(name, idCard, phone, pageNum, pageSize);
        for (PublicAppointment appointment : list) {
            decryptAndDesensitized(appointment);
        }
        return PaginationDto.<PublicAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    public int getTotal(String name, String idCard, String phone) {
        idCard = SM4Util.encrypt(idCard);
        phone = SM4Util.encrypt(phone);
        return publicAppointmentDao.count(name, idCard, phone);
    }

    public PaginationDto<PublicAppointment> searchAppointments(String applyDate, String appointmentDate, Integer campus, String unit, String name, String idCard, String countApplyDate, String countAppointmentDate, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        if (idCard != null && !idCard.isEmpty()) {
            idCard = SM4Util.encrypt(idCard);
        }
        int total = publicAppointmentDao.countForSearch(applyDate, appointmentDate, campus, unit, name, idCard, countApplyDate, countAppointmentDate);
        ArrayList<PublicAppointment> list = publicAppointmentDao.searchAppointments(applyDate, appointmentDate, campus, unit, name, idCard, countApplyDate, countAppointmentDate, pageNum, pageSize);
        for (PublicAppointment appointment : list) {
            decryptAndDesensitized(appointment);
        }
        return PaginationDto.<PublicAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    private void encrypt(PublicAppointment appointment) {
        appointment.setIdCard(SM4Util.encrypt(appointment.getIdCard()));
        appointment.setPhone(SM4Util.encrypt(appointment.getPhone()));
    }

    private void decrypt(PublicAppointment appointment) {
        appointment.setIdCard(SM4Util.decrypt(appointment.getIdCard()));
        appointment.setPhone(SM4Util.decrypt(appointment.getPhone()));
    }

    private void decryptAndDesensitized(PublicAppointment appointment) {
        appointment.setIdCard(DesensitizedUtil.idCard(SM4Util.decrypt(appointment.getIdCard())));
        appointment.setPhone(DesensitizedUtil.phone(SM4Util.decrypt(appointment.getPhone())));
    }

    public PasscodeDto getPasscode(int appointmentID, String name, String idCard, String phone) {
        PublicAppointment appointment = getPublicAppointmentById(appointmentID);
        if (!appointment.getName().equals(name) || !appointment.getIdCard().equals(idCard) || !appointment.getPhone().equals(phone)) {
            throw new BizException(ErrorCode.PASSCODE_NOT_BELONG.getCode(), "社会公众进校预约id" + appointmentID + "的通行码不属于该用户");
        }
        return PasscodeDto.builder()
                .name(DesensitizedUtil.name(name))
                .idCard(DesensitizedUtil.idCard(idCard))
                .startTime(appointment.getStartTime().toLocalDateTime())
                .endTime(appointment.getEndTime().toLocalDateTime().plusHours(23).plusMinutes(59).plusSeconds(59))
                .createTime(LocalDateTime.now())
                .build();
    }
}
