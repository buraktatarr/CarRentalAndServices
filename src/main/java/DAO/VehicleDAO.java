package DAO;

import DAO.Const.SqlScriptConst;
import Model.Enums.VehicleStatus;
import Model.Enums.VehicleType;
import Model.Vehicle;
import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO implements BaseDAO<Vehicle>{
    @Override
    public void save(Vehicle vehicle) throws SQLException {
        try (Connection connection = DBUtil.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.VEHICLE_SAVE);
            ps.setString(1, vehicle.getBrand());
            ps.setString(2, vehicle.getModel());
            ps.setString(3,vehicle.getType().name());
            ps.setString(4,vehicle.getStatus().name());
            ps.setBigDecimal(5,vehicle.getHourlyRate());
            ps.setBigDecimal(6,vehicle.getDailyRate());
            ps.setBigDecimal(7,vehicle.getWeeklyRate());
            ps.setBigDecimal(8,vehicle.getMonthlyRate());
            ps.setBigDecimal(9,vehicle.getVehicleValue());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Vehicle findById(Long id) throws SQLException {
        Vehicle vehicle =null;
        try (Connection connection =DBUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.VEHICLE_FIND_BY_ID);
            ps.setLong(1,id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                vehicle=new Vehicle();
                vehicle.setId(resultSet.getLong("id"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setType(VehicleType.valueOf(resultSet.getString("type")));
                vehicle.setStatus(VehicleStatus.valueOf(resultSet.getString("status")));
                vehicle.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                vehicle.setDailyRate(resultSet.getBigDecimal("daily_rate"));
                vehicle.setWeeklyRate(resultSet.getBigDecimal("weekly_rate"));
                vehicle.setMonthlyRate(resultSet.getBigDecimal("monthly_rate"));
                vehicle.setVehicleValue(resultSet.getBigDecimal("vehicle_value"));
                vehicle.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
                vehicle.setUpdatedDate(resultSet.getTimestamp("updated_date").toLocalDateTime());

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("ID ile araç aranırken bir hata oluştu: " + id, e);
        }
        return vehicle;
    }

    @Override
    public List<Vehicle> findAll() throws SQLException {
        List<Vehicle> vehicles=new ArrayList<>();

        try (Connection connection =DBUtil.getConnection()){
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlScriptConst.VEHICLE_FIND_ALL);


            while (resultSet.next()){
                Vehicle vehicle=new Vehicle();
                vehicle.setId(resultSet.getLong("id"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setType(VehicleType.valueOf(resultSet.getString("type")));
                vehicle.setStatus(VehicleStatus.valueOf(resultSet.getString("status")));
                vehicle.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                vehicle.setDailyRate(resultSet.getBigDecimal("daily_rate"));
                vehicle.setWeeklyRate(resultSet.getBigDecimal("weekly_rate"));
                vehicle.setMonthlyRate(resultSet.getBigDecimal("monthly_rate"));
                vehicle.setVehicleValue(resultSet.getBigDecimal("vehicle_value"));
                vehicle.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
                vehicle.setUpdatedDate(resultSet.getTimestamp("updated_date").toLocalDateTime());
                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Tüm kullanıcılar getirilirken bir hata oluştu.", e);
        }
        return vehicles;

    }

    @Override
    public void update(Vehicle vehicle) {
        try (Connection connection = DBUtil.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.VEHICLE_UPDATE);
            ps.setString(1, vehicle.getBrand());
            ps.setString(2, vehicle.getModel());
            ps.setString(3,vehicle.getType().name());
            ps.setString(4,vehicle.getStatus().name());
            ps.setBigDecimal(5,vehicle.getHourlyRate());
            ps.setBigDecimal(6,vehicle.getDailyRate());
            ps.setBigDecimal(7,vehicle.getWeeklyRate());
            ps.setBigDecimal(8,vehicle.getMonthlyRate());
            ps.setBigDecimal(9,vehicle.getVehicleValue());
            ps.setLong(10, vehicle.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(long id) {
        try (Connection connection = DBUtil.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.VEHICLE_DELETE);
            ps.setLong(1,id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Vehicle findByModel(String model) throws SQLException {
        Vehicle vehicle =null;
        try (Connection connection =DBUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.VEHICLE_FIND_BY_MODEL);
            ps.setString(1,model);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                vehicle=new Vehicle();
                vehicle.setId(resultSet.getLong("id"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setType(VehicleType.valueOf(resultSet.getString("type")));
                vehicle.setStatus(VehicleStatus.valueOf(resultSet.getString("status")));
                vehicle.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                vehicle.setDailyRate(resultSet.getBigDecimal("daily_rate"));
                vehicle.setWeeklyRate(resultSet.getBigDecimal("weekly_rate"));
                vehicle.setMonthlyRate(resultSet.getBigDecimal("monthly_rate"));
                vehicle.setVehicleValue(resultSet.getBigDecimal("vehicle_value"));
                vehicle.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
                vehicle.setUpdatedDate(resultSet.getTimestamp("updated_date").toLocalDateTime());

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("ID ile araç aranırken bir hata oluştu: " + model, e);
        }
        return vehicle;

    }
}
