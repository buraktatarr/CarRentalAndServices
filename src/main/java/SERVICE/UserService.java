package SERVICE;

import DAO.UserDAO;
import Model.Enums.Role;
import Model.User;
import Model.Vehicle;
import Util.PasswordUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;



    public UserService() {
        this.userDAO = new UserDAO();
    }



    public User findById(Long id) throws SQLException {
        User user =userDAO.findById(id);
        if (user ==null){
            System.out.println("Hata: Belirtilen ID'ye (" + id + ") sahip araç bulunamadı.");
        }
        return user;
    }

    public List<User> findAll(){
        List<User> users = userDAO.findAll();
        if (users.isEmpty()) {
            System.out.println("Sistemde kayıtlı kullanıcı bulunmamaktadır.");
        }
        return users;
    }



    public void save(String email, String firstName, String lastName, String password, Role role, LocalDate dataOfBirth) throws SQLException {
        User foundUser = userDAO.findByEmail(email);
        if (foundUser!=null){
            System.out.println("Girilen email zaten kayıtlıdır ");
        }else {userDAO.save(new User(email, PasswordUtil.hash(password),firstName, lastName,  role,dataOfBirth));
            System.out.println("Kayıt Başarılı!");}
    }

    public User login(String email,String password) throws SQLException {
        User foundUser = userDAO.findByEmail(email);
        if (foundUser != null) {
            String hashedPassword = PasswordUtil.hash(password);
            if (!hashedPassword.equals(foundUser.getPassword())) {
                System.out.println("Girilen Şifre Yanlıştır ");
                return null;
            }
            System.out.println("Giriş Başarılı! Hoş geldiniz " + foundUser.getFirstName());
            return foundUser;
        } else {
            System.out.println("Girilen Şifre yada isim yanlıştır");
            return null;
        }

    }
    public void update(User user) throws SQLException {
        User existingUser = userDAO.findById(user.getId());
        if (existingUser==null){
            System.out.println("Hata: Güncellenecek kullanıcı bulunamadı. ID: " + user.getId());
            return;
        }
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setRole(user.getRole());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        userDAO.update(existingUser);
        System.out.println("Kullanıcı başarıyla güncellendi: ID " + user.getId());
    }

    public void delete (User user) throws SQLException {
        User userDelete =userDAO.findById(user.getId());
        if (userDelete==null){System.out.println("Hata: Silinecek Kullanıcı bulunamadı. ID: " +userDelete.getId());
            return;
        }
        userDAO.delete(user.getId());
        System.out.println("Kullanıcı başarıyla silindi: ID " + user.getId());
    }

}
