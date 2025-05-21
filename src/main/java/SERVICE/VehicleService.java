package SERVICE;

import DAO.VehicleDAO;
import Model.Enums.Role;
import Model.Enums.VehicleStatus;
import Model.User;
import Model.Vehicle;
import Util.PasswordUtil;

import java.sql.SQLException;
import java.util.List;

public class VehicleService {
    private  final VehicleDAO vehicleDAO ;

    public VehicleService(){
        this.vehicleDAO =new VehicleDAO();
    }


    public void save(Vehicle vehicle , User adminUser) throws SQLException {
        if (adminUser == null || adminUser.getRole() != Role.ADMIN) {
            System.out.println("Hata: Sadece ADMIN rolüne sahip kullanıcılar araç ekleyebilir.");
            return; // ADMIN değilse işlemi durdur
        }


        Vehicle foundedVehicle =vehicleDAO.findByModel(vehicle.getModel());
        if (foundedVehicle!=null){
            System.out.println("Bu modelde araç mevcut. Kayıt yapılamadı: " + vehicle.getModel());
        }else {
            vehicleDAO.save(vehicle);
            System.out.println("Araç başarıyla kaydedildi: " + vehicle.getBrand() + " " + vehicle.getModel() + " (Ekleyen: " + adminUser.getEmail() + ")");
        }
    }

    public Vehicle findById(Long id ) throws SQLException {
        Vehicle vehicle =vehicleDAO.findById(id);
        if (vehicle ==null){
            System.out.println("Hata: Belirtilen ID'ye (" + id + ") sahip araç bulunamadı.");
        }
        return vehicle;
    }

    public List<Vehicle> findAll() throws SQLException {
        List<Vehicle> vehicles =vehicleDAO.findAll();
        if (vehicles.isEmpty()){
            System.out.println("Sistemde kayıtlı araç bulunmamaktadır.");
        }
        return vehicles ;
    }

    public void update(Vehicle vehicle) throws SQLException {
        Vehicle existingVehicle =vehicleDAO.findById(vehicle.getId());
        if (existingVehicle==null){
            System.out.println("Hata: Güncellenecek araç bulunamadı. ID: " + vehicle.getId());
            return;
        }
        existingVehicle.setBrand(vehicle.getBrand());
        existingVehicle.setModel(vehicle.getModel());
        existingVehicle.setType(vehicle.getType());
        existingVehicle.setStatus(vehicle.getStatus());
        existingVehicle.setHourlyRate(vehicle.getHourlyRate());
        existingVehicle.setDailyRate(vehicle.getDailyRate());
        existingVehicle.setWeeklyRate(vehicle.getWeeklyRate());
        existingVehicle.setMonthlyRate(vehicle.getMonthlyRate());
        existingVehicle.setVehicleValue(vehicle.getVehicleValue());
        vehicleDAO.update(existingVehicle);
        System.out.println("Araç başarıyla güncellendi: ID " + vehicle.getId());
    }

    public void delete (Vehicle vehicle) throws SQLException {
        Vehicle vehicleToDelete =vehicleDAO.findById(vehicle.getId());
        if (vehicleToDelete==null){System.out.println("Hata: Silinecek araç bulunamadı. ID: " +vehicle.getId());
            return;
        }
        vehicleDAO.delete(vehicle.getId());
        System.out.println("Araç başarıyla silindi: ID " + vehicle.getId());
    }

    public void markVehicleRented(Vehicle vehicle) throws SQLException {
        Vehicle vehicleRented =vehicleDAO.findById(vehicle.getId());
        if (vehicleRented!=null){
            if (vehicleRented.getStatus()== VehicleStatus.RENTED){
                System.out.println("Araç ID " + vehicleRented.getId() + "zaten kiralandı durumunda.");
                return;
            }
            vehicleRented.setStatus(VehicleStatus.RENTED);
            vehicleDAO.update(vehicleRented);
        }else {
            System.out.println("Hata: Durumu güncellenecek araç bulunamadı. ID: " + vehicleRented.getId());
        }
    }


    public void markVehicleAvailable(Vehicle vehicle) throws SQLException {
        Vehicle vehicleAvaible =vehicleDAO.findById(vehicle.getId());
        if (vehicleAvaible!=null){
            if (vehicleAvaible.getStatus()== VehicleStatus.RENTED){
                System.out.println("Araç ID " + vehicleAvaible.getId() + "zaten müsait durumda.");
                return;
            }
            vehicleAvaible.setStatus(VehicleStatus.AVAILABLE);
            vehicleDAO.update(vehicleAvaible);
        }else {
            System.out.println("Hata: Durumu güncellenecek araç bulunamadı. ID: " + vehicleAvaible.getId());
        }
    }


}
