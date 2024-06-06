package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class OfficialAppointmentDao {
    public void insert(OfficialAppointment appointment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO official_appointment (name, id_card, phone, campus, create_time, appointment_time, unit, transportation, license_plate, department_id, receptionist, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, appointment.getName());
            stmt.setString(2, appointment.getIdCard());
            stmt.setString(3, appointment.getPhone());
            stmt.setInt(4, appointment.getCampus().getValue());
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.setDate(6, Date.valueOf(appointment.getAppointmentTime()));
            stmt.setString(7, appointment.getUnit());
            stmt.setInt(8, appointment.getTransportation().getValue());
            stmt.setString(9, appointment.getLicensePlate());
            stmt.setLong(10, appointment.getDepartmentId());
            stmt.setString(11, appointment.getReceptionist());
            stmt.setString(12, appointment.getReason());
            stmt.setInt(13, appointment.getStatus().getValue());
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
                        .createTime(rs.getDate("create_time").toLocalDate())
                        .appointmentTime(rs.getDate("appointment_time").toLocalDate())
                        .unit(rs.getString("unit"))
                        .transportation(OfficialAppointment.Transportation.getType(rs.getInt("transportation")))
                        .licensePlate(rs.getString("license_plate"))
                        .departmentId(rs.getInt("department_id"))
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
            String sql = "UPDATE official_appointment SET name = ?, id_card = ?, phone = ?, campus = ?, create_time = ?, appointment_time = ?, unit = ?, transportation = ?, license_plate = ?, department_id = ?, receptionist = ?, reason = ?, status = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, appointment.getName());
            stmt.setString(2, appointment.getIdCard());
            stmt.setString(3, appointment.getPhone());
            stmt.setInt(4, appointment.getCampus().getValue());
            stmt.setDate(5, Date.valueOf(appointment.getCreateTime()));
            stmt.setDate(6, Date.valueOf(appointment.getAppointmentTime()));
            stmt.setString(7, appointment.getUnit());
            stmt.setInt(8, appointment.getTransportation().getValue());
            stmt.setString(9, appointment.getLicensePlate());
            stmt.setLong(10, appointment.getDepartmentId());
            stmt.setString(11, appointment.getReceptionist());
            stmt.setString(12, appointment.getReason());
            stmt.setInt(13, appointment.getStatus().getValue());
            stmt.setLong(14, appointment.getId());
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
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM official_appointment LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (pageNum - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            return getAppointmentList(rs);
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

    public ArrayList<OfficialAppointment> findByPage(String name, String idCard, String phone, int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM official_appointment WHERE name = ? AND id_card = ? AND phone = ? ORDER BY create_time DESC  LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, idCard);
            stmt.setString(3, phone);
            stmt.setInt(4, (pageNum - 1) * pageSize);
            stmt.setInt(5, pageSize);
            rs = stmt.executeQuery();
            return getAppointmentList(rs);
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public int count(String name, String idCard, String phone) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM official_appointment WHERE name = ? AND id_card = ? AND phone = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, idCard);
            stmt.setString(3, phone);
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

    public void updateAppointmentStatus(Integer id, OfficialAppointment.Status status) {
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

    public ArrayList<OfficialAppointment> searchAppointments(String applyDate, String appointmentDate, Integer campus, String unit, String name, String idCard, String receptionist, Integer status, Integer departmentId, String countApplyDateStr, String countAppointmentDateStr, int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM official_appointment WHERE 1=1");
            if (applyDate != null && !applyDate.isEmpty()) {
                sql.append(" AND create_time = ?");
            }
            if (appointmentDate != null && !appointmentDate.isEmpty()) {
                sql.append(" AND appointment_time = ?");
            }
            if (campus != null) {
                sql.append(" AND campus = ?");
            }
            if (unit != null && !unit.isEmpty()) {
                sql.append(" AND unit LIKE ?");
            }
            if (name != null && !name.isEmpty()) {
                sql.append(" AND name LIKE ?");
            }
            if (idCard != null && !idCard.isEmpty()) {
                sql.append(" AND id_card = ?");
            }
            if (receptionist != null && !receptionist.isEmpty()) {
                sql.append(" AND receptionist LIKE ?");
            }
            if (status != null) {
                sql.append(" AND status = ?");
            }
            if (departmentId != null) {
                sql.append(" AND department_id = ?");
            }
            if (countApplyDateStr != null && !countApplyDateStr.isEmpty()) {
                sql.append(" AND (create_time >= ? AND create_time < ?)");
            }

            if (countAppointmentDateStr != null && !countAppointmentDateStr.isEmpty()) {
                sql.append(" AND (appointment_time >= ? AND appointment_time < ?)");
            }
            sql.append(" LIMIT ?, ?");

            stmt = conn.prepareStatement(sql.toString());

            int index = 1;
            if (applyDate != null && !applyDate.isEmpty()) {
                stmt.setString(index++, applyDate);
            }
            if (appointmentDate != null && !appointmentDate.isEmpty()) {
                stmt.setString(index++, appointmentDate);
            }
            if (campus != null) {
                stmt.setInt(index++, campus);
            }
            if (unit != null && !unit.isEmpty()) {
                stmt.setString(index++, unit);
            }
            if (name != null && !name.isEmpty()) {
                stmt.setString(index++, name);
            }
            if (idCard != null && !idCard.isEmpty()) {
                stmt.setString(index++, idCard);
            }
            if (receptionist != null && !receptionist.isEmpty()) {
                stmt.setString(index++, receptionist);
            }
            if (status != null) {
                stmt.setInt(index++, status);
            }
            if (departmentId != null) {
                stmt.setLong(index++, departmentId);
            }
            if (countApplyDateStr != null && !countApplyDateStr.isEmpty()) {
                Date startTime = Date.valueOf(countApplyDateStr + "-01");
                stmt.setDate(index++, startTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startTime);
                cal.add(Calendar.MONTH, 1);
                Date endTime = new Date(cal.getTimeInMillis());

                stmt.setDate(index++, endTime);
            }
            if (countAppointmentDateStr != null && !countAppointmentDateStr.isEmpty()) {
                Date startTime = Date.valueOf(countAppointmentDateStr + "-01");
                stmt.setDate(index++, startTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startTime);
                cal.add(Calendar.MONTH, 1);
                Date endTime = new Date(cal.getTimeInMillis());

                stmt.setDate(index++, endTime);
            }
            stmt.setInt(index++, (pageNum - 1) * pageSize);
            stmt.setInt(index, pageSize);
            rs = stmt.executeQuery();
            return getAppointmentList(rs);
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public int countForSearch(String applyDate, String appointmentDate, Integer campus, String unit, String name, String idCard, String receptionist, Integer status, Integer departmentId, String countApplyDateStr, String countAppointmentDateStr) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM official_appointment where 1=1");
            if (applyDate != null && !applyDate.isEmpty()) {
                sql.append(" AND create_time = ?");
            }
            if (appointmentDate != null && !appointmentDate.isEmpty()) {
                sql.append(" AND appointment_time = ?");
            }
            if (campus != null) {
                sql.append(" AND campus = ?");
            }
            if (unit != null && !unit.isEmpty()) {
                sql.append(" AND unit LIKE ?");
            }
            if (name != null && !name.isEmpty()) {
                sql.append(" AND name LIKE ?");
            }
            if (idCard != null && !idCard.isEmpty()) {
                sql.append(" AND id_card = ?");
            }
            if (receptionist != null && !receptionist.isEmpty()) {
                sql.append(" AND receptionist LIKE ?");
            }
            if (status != null) {
                sql.append(" AND status = ?");
            }
            if (departmentId != null) {
                sql.append(" AND department_id = ?");
            }
            if (countApplyDateStr != null && !countApplyDateStr.isEmpty()) {
                sql.append(" AND (create_time >= ? AND create_time < ?)");
            }

            if (countAppointmentDateStr != null && !countAppointmentDateStr.isEmpty()) {
                sql.append(" AND (appointment_time >= ? AND appointment_time < ?)");
            }

            stmt = conn.prepareStatement(sql.toString());

            int index = 1;
            if (applyDate != null && !applyDate.isEmpty()) {
                stmt.setString(index++, applyDate);
            }
            if (appointmentDate != null && !appointmentDate.isEmpty()) {
                stmt.setString(index++, appointmentDate);
            }
            if (campus != null) {
                stmt.setInt(index++, campus);
            }
            if (unit != null && !unit.isEmpty()) {
                stmt.setString(index++, unit);
            }
            if (name != null && !name.isEmpty()) {
                stmt.setString(index++, name);
            }
            if (idCard != null && !idCard.isEmpty()) {
                stmt.setString(index++, idCard);
            }
            if (receptionist != null && !receptionist.isEmpty()) {
                stmt.setString(index++, receptionist);
            }
            if (status != null) {
                stmt.setInt(index++, status);
            }
            if (departmentId != null) {
                stmt.setInt(index++, departmentId);
            }
            if (countApplyDateStr != null && !countApplyDateStr.isEmpty()) {
                Date startTime = Date.valueOf(countApplyDateStr + "-01");
                stmt.setDate(index++, startTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startTime);
                cal.add(Calendar.MONTH, 1);
                Date endTime = new Date(cal.getTimeInMillis());

                stmt.setDate(index++, endTime);
            }

            if (countAppointmentDateStr != null && !countAppointmentDateStr.isEmpty()) {
                Date startTime = Date.valueOf(countAppointmentDateStr + "-01");
                stmt.setDate(index++, startTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startTime);
                cal.add(Calendar.MONTH, 1);
                Date endTime = new Date(cal.getTimeInMillis());

                stmt.setDate(index, endTime);
            }

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

    private ArrayList<OfficialAppointment> getAppointmentList(ResultSet rs) throws SQLException {
        ArrayList<OfficialAppointment> appointmentList = new ArrayList<>();
        while (rs.next()) {
            appointmentList.add(OfficialAppointment.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .idCard(rs.getString("id_card"))
                    .phone(rs.getString("phone"))
                    .campus(OfficialAppointment.Campus.getType(rs.getInt("campus")))
                    .createTime(rs.getDate("create_time").toLocalDate())
                    .appointmentTime(rs.getDate("appointment_time").toLocalDate())
                    .unit(rs.getString("unit"))
                    .transportation(OfficialAppointment.Transportation.getType(rs.getInt("transportation")))
                    .licensePlate(rs.getString("license_plate"))
                    .departmentId(rs.getInt("department_id"))
                    .receptionist(rs.getString("receptionist"))
                    .reason(rs.getString("reason"))
                    .status(OfficialAppointment.Status.getType(rs.getInt("status")))
                    .build());
        }
        return appointmentList;
    }
}
