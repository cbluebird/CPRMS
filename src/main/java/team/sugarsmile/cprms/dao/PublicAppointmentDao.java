package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.PublicAppointment;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicAppointmentDao {
    public void insert(PublicAppointment appointment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO public_appointment (name, id_card, phone, campus, create_time, start_time, end_time, unit, transportation, license_plate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public PublicAppointment findById(long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM public_appointment WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return PublicAppointment.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .idCard(rs.getString("id_card"))
                        .phone(rs.getString("phone"))
                        .campus(PublicAppointment.Campus.getType(rs.getInt("campus")))
                        .createTime(rs.getTimestamp("create_time"))
                        .startTime(rs.getTimestamp("start_time"))
                        .endTime(rs.getTimestamp("end_time"))
                        .unit(rs.getString("unit"))
                        .transportation(PublicAppointment.Transportation.getType(rs.getInt("transportation")))
                        .licensePlate(rs.getString("license_plate"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public void update(PublicAppointment appointment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE public_appointment SET name = ?, id_card = ?, phone = ?, campus = ?, create_time = ?, start_time = ?, end_time = ?, unit = ?, transportation = ?, license_plate = ? WHERE id = ?";
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
            stmt.setLong(11, appointment.getId());
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
            String sql = "DELETE FROM public_appointment WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public ArrayList<PublicAppointment> findByPage(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<PublicAppointment> appointmentList = new ArrayList<>();
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM public_appointment LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (pageNum - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            while (rs.next()) {
                appointmentList.add(PublicAppointment.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .idCard(rs.getString("id_card"))
                        .phone(rs.getString("phone"))
                        .campus(PublicAppointment.Campus.getType(rs.getInt("campus")))
                        .createTime(rs.getTimestamp("create_time"))
                        .startTime(rs.getTimestamp("start_time"))
                        .endTime(rs.getTimestamp("end_time"))
                        .unit(rs.getString("unit"))
                        .transportation(PublicAppointment.Transportation.getType(rs.getInt("transportation")))
                        .licensePlate(rs.getString("license_plate"))
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
            String sql = "SELECT COUNT(*) FROM public_appointment";
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
}
