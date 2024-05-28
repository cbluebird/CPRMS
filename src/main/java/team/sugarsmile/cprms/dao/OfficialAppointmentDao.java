package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfficialAppointmentDao {
    public void insert(OfficialAppointment appointment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO official_appointment (name, id_card, phone, campus, create_time, start_time, end_time, unit, transportation, license_plate, department_id, receptionist, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, appointment.getName());
            stmt.setString(2, appointment.getIdCard());
            stmt.setString(3, appointment.getPhone());
            stmt.setInt(4, appointment.getCampus().getValue());
            stmt.setTimestamp(5, new Timestamp(appointment.getCreateTime().getTime()));
            stmt.setTimestamp(6, new Timestamp(appointment.getStartTime().getTime()));
            stmt.setTimestamp(7, new Timestamp(appointment.getEndTime().getTime()));
            stmt.setString(8, appointment.getUnit());
            stmt.setInt(9, appointment.getTransportation().getValue());
            stmt.setString(10, appointment.getLicensePlate());
            stmt.setLong(11, appointment.getDepartmentId());
            stmt.setString(12, appointment.getReceptionist());
            stmt.setString(13, appointment.getReason());
            stmt.setInt(14, appointment.getStatus().getValue());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public OfficialAppointment findById(long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM official_appointment WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return OfficialAppointment.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .idCard(rs.getString("id_card"))
                        .phone(rs.getString("phone"))
                        .campus(OfficialAppointment.Campus.getType(rs.getInt("campus")))
                        .createTime(rs.getTimestamp("create_time"))
                        .startTime(rs.getTimestamp("start_time"))
                        .endTime(rs.getTimestamp("end_time"))
                        .unit(rs.getString("unit"))
                        .transportation(OfficialAppointment.Transportation.getType(rs.getInt("transportation")))
                        .licensePlate(rs.getString("license_plate"))
                        .departmentId(rs.getLong("department_id"))
                        .receptionist(rs.getString("receptionist"))
                        .reason(rs.getString("reason"))
                        .status(OfficialAppointment.Status.getType(rs.getInt("status")))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public void update(OfficialAppointment appointment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE official_appointment SET name = ?, id_card = ?, phone = ?, campus = ?, create_time = ?, start_time = ?, end_time = ?, unit = ?, transportation = ?, license_plate = ?, department_id = ?, receptionist = ?, reason = ?, status = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, appointment.getName());
            stmt.setString(2, appointment.getIdCard());
            stmt.setString(3, appointment.getPhone());
            stmt.setInt(4, appointment.getCampus().getValue());
            stmt.setTimestamp(5, new Timestamp(appointment.getCreateTime().getTime()));
            stmt.setTimestamp(6, new Timestamp(appointment.getStartTime().getTime()));
            stmt.setTimestamp(7, new Timestamp(appointment.getEndTime().getTime()));
            stmt.setString(8, appointment.getUnit());
            stmt.setInt(9, appointment.getTransportation().getValue());
            stmt.setString(10, appointment.getLicensePlate());
            stmt.setLong(11, appointment.getDepartmentId());
            stmt.setString(12, appointment.getReceptionist());
            stmt.setString(13, appointment.getReason());
            stmt.setInt(14, appointment.getStatus().getValue());
            stmt.setLong(15, appointment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public void delete(long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM official_appointment WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public ArrayList<OfficialAppointment> findByPage(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<OfficialAppointment> appointmentList = new ArrayList<>();
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM official_appointment LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (pageNum - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            while (rs.next()) {
                appointmentList.add(OfficialAppointment.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .idCard(rs.getString("id_card"))
                        .phone(rs.getString("phone"))
                        .campus(OfficialAppointment.Campus.getType(rs.getInt("campus")))
                        .createTime(rs.getTimestamp("create_time"))
                        .startTime(rs.getTimestamp("start_time"))
                        .endTime(rs.getTimestamp("end_time"))
                        .unit(rs.getString("unit"))
                        .transportation(OfficialAppointment.Transportation.getType(rs.getInt("transportation")))
                        .licensePlate(rs.getString("license_plate"))
                        .departmentId(rs.getLong("department_id"))
                        .receptionist(rs.getString("receptionist"))
                        .reason(rs.getString("reason"))
                        .status(OfficialAppointment.Status.getType(rs.getInt("status")))
                        .build());
            }
            return appointmentList;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public int count() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM official_appointment";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public void approveAppointment(long id, OfficialAppointment.Status status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE official_appointment SET status = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, status.getValue());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public List<OfficialAppointment> findPendingAppointmentsByStatus(OfficialAppointment.Status status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OfficialAppointment> appointmentList = new ArrayList<>();
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM official_appointment WHERE status = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, status.getValue());
            rs = stmt.executeQuery();
            while (rs.next()) {
                appointmentList.add(OfficialAppointment.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .idCard(rs.getString("id_card"))
                        .phone(rs.getString("phone"))
                        .campus(OfficialAppointment.Campus.getType(rs.getInt("campus")))
                        .createTime(rs.getTimestamp("create_time"))
                        .startTime(rs.getTimestamp("start_time"))
                        .endTime(rs.getTimestamp("end_time"))
                        .unit(rs.getString("unit"))
                        .transportation(OfficialAppointment.Transportation.getType(rs.getInt("transportation")))
                        .licensePlate(rs.getString("license_plate"))
                        .departmentId(rs.getLong("department_id"))
                        .receptionist(rs.getString("receptionist"))
                        .reason(rs.getString("reason"))
                        .status(OfficialAppointment.Status.getType(rs.getInt("status")))
                        .build());
            }
            return appointmentList;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }
}
