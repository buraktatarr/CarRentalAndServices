package DAO;

import DAO.Const.SqlScriptConst;
import Model.Enums.Role;
import Model.User;
import Util.DBUtil;

import java.sql.*;
import java.time.LocalDate; // LocalDate import'unu ekledim
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements BaseDAO<User> {

    @Override
    public void save(User user) throws SQLException {
        try (Connection connection = DBUtil.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.USER_SAVE);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getRole().name());
            // Eğer dateOfBirth null ise SET NULL olarak ayarla
            if (user.getDateOfBirth() != null) {
                ps.setDate(6, Date.valueOf(user.getDateOfBirth()));
            } else {
                ps.setNull(6, Types.DATE); // SQL'de DATE tipi için NULL ayarla
            }
            ps.executeUpdate();

        } catch (SQLException e) {
            // Hata mesajını daha açıklayıcı hale getirebilirim
            throw new SQLException("Kullanıcı kaydedilirken bir veritabanı hatası oluştu: " + e.getMessage(), e);
        }
    }

    public User findByEmail(String email) throws SQLException {
        User user = null;

        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.USER_FIND_BY_EMAIL);
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setPassword(resultSet.getString("password"));


                Date sqlDateOfBirth = resultSet.getDate("date_of_birth");
                user.setDateOfBirth((sqlDateOfBirth != null) ? sqlDateOfBirth.toLocalDate() : null);

                // created_date ve updated_date null olabilir .
                Timestamp createdTimestamp = resultSet.getTimestamp("created_date");
                user.setCreatedDate((createdTimestamp != null) ? createdTimestamp.toLocalDateTime() : null);

                Timestamp updatedTimestamp = resultSet.getTimestamp("updated_date");
                user.setUpdatedDate((updatedTimestamp != null) ? updatedTimestamp.toLocalDateTime() : null);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("E-posta ile kullanıcı bulunamadı: " + email, e); // Hata mesajı düzeltildi
        }
        return user;
    }

    @Override
    public User findById(Long id) throws SQLException {
        User user = null;

        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.USER_FIND_BY_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setPassword(resultSet.getString("password"));


                Date sqlDateOfBirth = resultSet.getDate("date_of_birth");
                user.setDateOfBirth((sqlDateOfBirth != null) ? sqlDateOfBirth.toLocalDate() : null);

                // created_date ve updated_date null olabilir
                Timestamp createdTimestamp = resultSet.getTimestamp("created_date");
                user.setCreatedDate((createdTimestamp != null) ? createdTimestamp.toLocalDateTime() : null);

                Timestamp updatedTimestamp = resultSet.getTimestamp("updated_date");
                user.setUpdatedDate((updatedTimestamp != null) ? updatedTimestamp.toLocalDateTime() : null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("ID ile kullanıcı bulunamadı: " + id, e); // Hata mesajı düzeltildi
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlScriptConst.USER_FIND_ALL);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setPassword(resultSet.getString("password"));


                Date sqlDateOfBirth = resultSet.getDate("date_of_birth");
                user.setDateOfBirth((sqlDateOfBirth != null) ? sqlDateOfBirth.toLocalDate() : null);

                // created_date ve updated_date null olabilir
                Timestamp createdTimestamp = resultSet.getTimestamp("created_date");
                user.setCreatedDate((createdTimestamp != null) ? createdTimestamp.toLocalDateTime() : null);

                Timestamp updatedTimestamp = resultSet.getTimestamp("updated_date");
                user.setUpdatedDate((updatedTimestamp != null) ? updatedTimestamp.toLocalDateTime() : null);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Tüm kullanıcılar getirilirken bir veritabanı hatası oluştu.", e); // Hata mesajı düzeltildi
        }
        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        try (Connection connection = DBUtil.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.USER_UPDATE);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getRole().name());
            // Eğer dateOfBirth null ise SET NULL olarak ayarla
            if (user.getDateOfBirth() != null) {
                ps.setDate(6, Date.valueOf(user.getDateOfBirth()));
            } else {
                ps.setNull(6, Types.DATE); // SQL'de DATE tipi için NULL ayarla
            }
            ps.setLong(7, user.getId()); // ID parametre sırasını düzelttim
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Kullanıcı güncellenirken bir veritabanı hatası oluştu: " + e.getMessage(), e); // Hata mesajı düzeltildi
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlScriptConst.USER_DELETE);
            ps.setLong(1, id);
            ps.executeUpdate();
            System.out.println("Kullanıcı başarıyla silindi: ID " + id); //

        } catch (SQLException e) {
            throw new RuntimeException("Kullanıcı silinirken bir veritabanı hatası oluştu: ID=" + id + " " + e.getMessage(), e); // Hata mesajı düzeltildi
        }
    }
}