package team.sugarsmile.cprms.service;

import team.sugarsmile.cprms.dao.DepartmentDao;
import team.sugarsmile.cprms.dao.OfficialAppointmentDao;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.dto.PasscodeDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.util.DesensitizedUtil;
import team.sugarsmile.cprms.util.SM4Util;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OfficialAppointmentService {
    private final OfficialAppointmentDao officialAppointmentDao = new OfficialAppointmentDao();
    private final DepartmentDao departmentDao = new DepartmentDao();

    public void addOfficialAppointment(OfficialAppointment appointment) {
        if (departmentDao.findById(appointment.getDepartmentId()) == null) {
            throw new BizException(ErrorCode.DEPARTMENT_NOT_EXIST.getCode(), "部门编号" + appointment.getDepartmentId() + "不存在");
        }
        encrypt(appointment);
        officialAppointmentDao.insert(appointment);
    }

    public OfficialAppointment getOfficialAppointmentById(long id) {
        OfficialAppointment appointment = officialAppointmentDao.findById(id);
        if (appointment == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + id + " 不存在");
        }
        decrypt(appointment);
        return appointment;
    }

    public void updateOfficialAppointment(OfficialAppointment appointment) {
        if (officialAppointmentDao.findById(appointment.getId()) == null) {
            throw new BizException(ErrorCode.APPOINTMENT_NOT_EXIST.getCode(), "预约编号 " + appointment.getId() + " 不存在");
        }
        encrypt(appointment);
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
        for (OfficialAppointment appointment : list) {
            decryptAndDesensitized(appointment);
        }
        return PaginationDto.<OfficialAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    public PaginationDto<OfficialAppointment> findOfficialAppointmentList(String name, String idCard, String phone, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        idCard = SM4Util.encrypt(idCard);
        phone = SM4Util.encrypt(phone);
        int total = officialAppointmentDao.count(name, idCard, phone);
        ArrayList<OfficialAppointment> list = officialAppointmentDao.findByPage(name, idCard, phone, pageNum, pageSize);
        for (OfficialAppointment appointment : list) {
            decryptAndDesensitized(appointment);
        }
        return PaginationDto.<OfficialAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    public int getTotal(String name, String idCard, String phone) {
        idCard = SM4Util.encrypt(idCard);
        phone = SM4Util.encrypt(phone);
        return officialAppointmentDao.count(name, idCard, phone);
    }

    public PaginationDto<OfficialAppointment> searchAppointments(String applyDate, String appointmentDate, Integer campus, String unit, String name, String idCard, String receptionist, Integer status, Integer departmentId, String countApplyDate, String countAppointmentDate, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        if (idCard != null && !idCard.isEmpty()) {
            idCard = SM4Util.encrypt(idCard);
        }
        int total = officialAppointmentDao.countForSearch(applyDate, appointmentDate, campus, unit, name, idCard, receptionist, status, departmentId, countApplyDate, countAppointmentDate);
        ArrayList<OfficialAppointment> list = officialAppointmentDao.searchAppointments(applyDate, appointmentDate, campus, unit, name, idCard, receptionist, status, departmentId, countApplyDate, countAppointmentDate, pageNum, pageSize);
        for (OfficialAppointment appointment : list) {
            decryptAndDesensitized(appointment);
        }
        return PaginationDto.<OfficialAppointment>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .list(list)
                .build();
    }

    private void encrypt(OfficialAppointment appointment) {
        appointment.setIdCard(SM4Util.encrypt(appointment.getIdCard()));
        appointment.setPhone(SM4Util.encrypt(appointment.getPhone()));
    }

    private void decrypt(OfficialAppointment appointment) {
        appointment.setIdCard(SM4Util.decrypt(appointment.getIdCard()));
        appointment.setPhone(SM4Util.decrypt(appointment.getPhone()));
    }

    private void decryptAndDesensitized(OfficialAppointment appointment) {
        appointment.setIdCard(DesensitizedUtil.idCard(SM4Util.decrypt(appointment.getIdCard())));
        appointment.setPhone(DesensitizedUtil.phone(SM4Util.decrypt(appointment.getPhone())));
    }

    public void reviewAppointment(Integer id, OfficialAppointment.Status status) {
        officialAppointmentDao.updateAppointmentStatus(id, status);
    }

    public PasscodeDto getPasscode(int appointmentID, String name, String idCard, String phone) {
        OfficialAppointment appointment = getOfficialAppointmentById(appointmentID);
        if (!appointment.getName().equals(name) || !appointment.getIdCard().equals(idCard) || !appointment.getPhone().equals(phone)) {
            throw new BizException(ErrorCode.PASSCODE_NOT_BELONG.getCode(), "公务进校预约id" + appointmentID + "的通行码不属于该用户");
        }
        if (appointment.getStatus() != OfficialAppointment.Status.APPROVED) {
            throw new BizException(ErrorCode.PASSCODE_OFFICIAL_NOT_APPROVED.getCode(), "公务进校预约id" + appointmentID + "的通行码未通过审批");
        }
        return PasscodeDto.builder()
                .name(DesensitizedUtil.name(name))
                .idCard(DesensitizedUtil.idCard(idCard))
                .appointmentTime(appointment.getAppointmentTime())
                .createTime(LocalDateTime.now())
                .build();
    }
}

