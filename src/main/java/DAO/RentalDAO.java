package DAO;

import DAO.Const.SqlScriptConst;
import Model.Enums.RentType;
import Model.Rental;
import Model.User;
import Model.Vehicle;
import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO implements BaseDAO<Rental> {


    private final UserDAO userDAO;
    private final VehicleDAO vehicleDAO;

    public RentalDAO() {
        this.userDAO = new UserDAO();
        this.vehicleDAO = new VehicleDAO();
    }


    @Override
    public void save(Rental rental) throws SQLException {

        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.RENTAL_SAVE);
            ps.setLong(1, rental.getUser().getId()); // User ID'si
            ps.setLong(2, rental.getVehicle().getId()); // Vehicle ID'si
            ps.setTimestamp(3, Timestamp.valueOf(rental.getRentalStartDate()));
            // rentalEndDate null olabilir, kontrol et
            if (rental.getRentalEndDate() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(rental.getRentalEndDate()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            ps.setString(5, rental.getRentType().name());
            ps.setBigDecimal(6, rental.getDeposit());
            ps.setBigDecimal(7, rental.getTotalPrice());

            ps.executeUpdate();
            System.out.println("Kiralama kaydı başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Kiralama kaydı eklenirken bir hata oluştu.", e);
        }

    }

    @Override
    public Rental findById(Long id) throws SQLException {
        Rental rental = null;
        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.RENTAL_FIND_BY_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                rental = new Rental();
                rental.setId(resultSet.getLong("id"));

                // User ve Vehicle objelerini ID'lerinden çekerek doldur
                Long userId = resultSet.getLong("user_id");
                User user = userDAO.findById(userId);
                rental.setUser(user);

                Long vehicleId = resultSet.getLong("vehicle_id");
                Vehicle vehicle = vehicleDAO.findById(vehicleId);
                rental.setVehicle(vehicle);

                rental.setRentalStartDate(resultSet.getTimestamp("rental_start_date").toLocalDateTime());

                // rental_end_date null olabilir

                Timestamp endDateTimestamp = resultSet.getTimestamp("rental_end_date");

                if (endDateTimestamp != null) {
                    rental.setRentalEndDate(endDateTimestamp.toLocalDateTime());
                }

                rental.setRentType(RentType.valueOf(resultSet.getString("rent_type")));
                rental.setDeposit(resultSet.getBigDecimal("deposit"));
                rental.setTotalPrice(resultSet.getBigDecimal("total_price"));
                rental.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
                rental.setUpdatedDate(resultSet.getTimestamp("updated_date").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Kiralama kaydı ID ile aranırken bir hata oluştu: " + id, e);
        }
        return rental;
    }

    @Override
    public List<Rental> findAll() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlScriptConst.RENTAL_FIND_ALL);

            while (resultSet.next()) {
                Rental rental = new Rental();
                rental.setId(resultSet.getLong("id"));

                // User ve Vehicle objelerini ID'lerinden çekerek doldur
                Long userId = resultSet.getLong("user_id");
                User user = userDAO.findById(userId);
                rental.setUser(user);

                Long vehicleId = resultSet.getLong("vehicle_id");
                Vehicle vehicle = vehicleDAO.findById(vehicleId);
                rental.setVehicle(vehicle);

                rental.setRentalStartDate(resultSet.getTimestamp("rental_start_date").toLocalDateTime());
                Timestamp endDateTimestamp = resultSet.getTimestamp("rental_end_date");
                if (endDateTimestamp != null) {
                    rental.setRentalEndDate(endDateTimestamp.toLocalDateTime());
                }

                rental.setRentType(RentType.valueOf(resultSet.getString("rent_type")));
                rental.setDeposit(resultSet.getBigDecimal("deposit"));
                rental.setTotalPrice(resultSet.getBigDecimal("total_price"));
                rental.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
                rental.setUpdatedDate(resultSet.getTimestamp("updated_date").toLocalDateTime());
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Tüm kiralama kayıtları getirilirken bir hata oluştu.", e);
        }
        return rentals;
    }

    @Override
    public void update(Rental rental) throws SQLException {
        try (Connection connection =DBUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.RENTAL_UPDATE);
            ps.setLong(1,rental.getUser().getId());
            ps.setLong(2,rental.getVehicle().getId());
            ps.setTimestamp(3,Timestamp.valueOf(rental.getRentalStartDate()));
            if (rental.getRentalEndDate() !=null){
                ps.setTimestamp(4,Timestamp.valueOf(rental.getRentalEndDate()));
            }else {
                ps.setNull(4,Types.TIMESTAMP);
            }
            ps.setString(5,rental.getRentType().name());
            ps.setBigDecimal(6,rental.getDeposit());
            ps.setBigDecimal(7, rental.getTotalPrice());
            ps.setLong(8, rental.getId());
            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Kiralama kaydı güncellenirken bir hata oluştu: " + rental.getId(), e);
        }

    }

    @Override
    public void delete(long id) throws SQLException {
        try (Connection connection =DBUtil.getConnection()){
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.RENTAL_DELETE);
            ps.setLong(1,id);
            ps.executeUpdate();
            System.out.println("Kiralama kaydı başarıyla silindi: ID " + id);
        }catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Kiralama kaydı silinirken bir hata oluştu: " + id, e);
        }

    }
}

