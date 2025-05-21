package SERVICE;

import DAO.RentalDAO;
import Model.Enums.RentType;
import Model.Enums.Role;
import Model.Enums.VehicleStatus;
import Model.Enums.VehicleType;
import Model.Rental;
import Model.User;
import Model.Vehicle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {

    private final RentalDAO rentalDAO;
    private final VehicleService vehicleService;

    public RentalService(){
        this.rentalDAO=new RentalDAO();
        this.vehicleService=new VehicleService();
    }


    public Rental rentVehicle(User user, Vehicle vehicle , LocalDateTime rentalStartDate, LocalDateTime rentalEndDate, RentType rentType) throws SQLException {

        // 0. Genel Geçerlilik Kontrolleri
        if (user == null || vehicle == null || rentalStartDate == null || rentalEndDate == null || rentType == null) {
            System.out.println("Hata: Kiralama işlemi için gerekli tüm bilgiler eksiksiz olmalıdır.");
            return null;
        }

        if (rentalStartDate.isAfter(rentalEndDate)) {
            System.out.println("Hata: Kiralama başlangıç tarihi bitiş tarihinden sonra olamaz.");
            return null;
        }

        // 1. Araç Müsaitlik Kontrolü
        // Veritabanından aracın güncel durumunu çekmek daha güvenli olur
        Vehicle currentVehicleState = vehicleService.findById(vehicle.getId());
        if (currentVehicleState == null || currentVehicleState.getStatus() != VehicleStatus.AVAILABLE) {
            System.out.println("Hata: Araç kiralama için müsait değil veya bulunamadı! Mevcut durumu: " + (currentVehicleState != null ? currentVehicleState.getStatus() : "Yok"));
            return null;
        }


        // 2. Kural: Kiralanabilir Araç Tipleri (Otomobil, Helikopter, Motosiklet)
        if (vehicle.getType() != VehicleType.CAR &&
                vehicle.getType() != VehicleType.HELICOPTER &&
                vehicle.getType() != VehicleType.MOTORCYCLE) {
            System.out.println("Hata: Sadece Otomobil, Helikopter ve Motosiklet kiralanabilir. Araç tipi: " + vehicle.getType());
            return null;
        }

        // Kiralama Süresi Hesaplama
        long rentalDuration = 0; // Gün, saat, hafta veya ay olarak
        long durationInMonths = 0; // Kurumsal kiralama için ay cinsinden süre

        if (rentType == RentType.HOURLY) {
            rentalDuration = ChronoUnit.HOURS.between(rentalStartDate, rentalEndDate) + 1; // +1 başlangıç saatini de sayar
        } else if (rentType == RentType.DAILY) {
            rentalDuration = ChronoUnit.DAYS.between(rentalStartDate, rentalEndDate) + 1; // +1 başlangıç gününü de sayar
        } else if (rentType == RentType.WEEKLY) {
            rentalDuration = ChronoUnit.WEEKS.between(rentalStartDate, rentalEndDate);
        } else if (rentType == RentType.MONTHLY) {
            durationInMonths = ChronoUnit.MONTHS.between(rentalStartDate.toLocalDate(), rentalEndDate.toLocalDate()); // Ay farkı
            rentalDuration = durationInMonths; // Kiralama süresi ay olarak kabul edilebilir
        } else {
            System.out.println("Hata: Geçersiz kiralama tipi. Fiyat hesaplaması yapılamadı.");
            return null;
        }

        // Negatif süre kontrolü (bitiş tarihi başlangıç tarihinden önceyse)
        // Eğer rentalDuration 0 ise ve aylık kiralama değilse (çünkü aylık kiralama 0 olabilir ve Math.max(1,...) ile 1 ay olacağız)
        if (rentalDuration <= 0 && rentType != RentType.MONTHLY) {
            System.out.println("Hata: Kiralama süresi geçerli değil veya sıfır. Bitiş tarihi başlangıç tarihinden sonra olmalıdır.");
            return null;
        }


        // 3. Kural: Kurumsal kullanıcılar en az 1 aylık kiralama yapabilir.
        if (user.getRole() == Role.CORPORATE && durationInMonths < 1) {
            System.out.println("Hata: Kurumsal kullanıcılar en az 1 aylık kiralama yapabilir. Seçilen süre: " + durationInMonths + " ay.");
            return null;
        }

        BigDecimal calculatedPrice;
        BigDecimal depositAmount = BigDecimal.ZERO; // Varsayılan olarak 0

        BigDecimal durationBigDecimal = BigDecimal.valueOf(rentalDuration);

        // Fiyat hesaplaması
        switch (rentType) {
            case HOURLY:
                calculatedPrice = vehicle.getHourlyRate().multiply(durationBigDecimal);
                break;
            case DAILY:
                calculatedPrice = vehicle.getDailyRate().multiply(durationBigDecimal);
                break;
            case WEEKLY:
                calculatedPrice = vehicle.getWeeklyRate().multiply(durationBigDecimal);
                break;
            case MONTHLY:
                // Aylık kiralamada minimum 1 ay alınır
                calculatedPrice = vehicle.getMonthlyRate().multiply(BigDecimal.valueOf(Math.max(1, durationInMonths)));
                break;
            default:
                System.out.println("Hata: Bilinmeyen kiralama tipi fiyat hesaplaması yapılamadı.");
                return null;
        }

        // 4. Kural: Araç bedeli 2 Milyon TL'den fazla olan araçlar için yaş ve depozito kontrolü
        BigDecimal twoMillion = new BigDecimal("2000000"); // 2 Milyon TL
        if (vehicle.getVehicleValue().compareTo(twoMillion) > 0) { // Araç bedeli > 2 Milyon ise
            // Kullanıcının yaşını hesapla
            LocalDate today = LocalDate.now();
            int age = Period.between(user.getDateOfBirth(), today).getYears();

            // Yaş kontrolü
            if (age < 30) {
                System.out.println("Hata: " + vehicle.getVehicleValue() + " TL değerindeki araçlar için 30 yaşından büyük olmanız gerekmektedir. Yaşınız: " + age);
                return null;
            }

            // Depozito hesapla (%10)
            BigDecimal DEPOSIT_RATE = new BigDecimal("0.10"); // %10 depozito
            depositAmount = vehicle.getVehicleValue().multiply(DEPOSIT_RATE);
            System.out.println("Bilgi: Yüksek değerli araç için " + depositAmount + " TL depozito talep edilmektedir.");
        }


        // Kiralama kaydını oluştur
        Rental newRental = new Rental(
                user,              // User objesini direkt geçiyoruz
                vehicle,           // Vehicle objesini direkt geçiyoruz
                rentalStartDate,
                rentalEndDate,
                rentType,
                depositAmount,
                calculatedPrice

        );

        rentalDAO.save(newRental);
        System.out.println("Kiralama kaydı veritabanına başarıyla eklendi.");

        // Aracın durumunu KİRALANDI olarak güncelle
        vehicleService.markVehicleRented(vehicle);
        System.out.println("Araç durumu başarıyla KİRALANDI olarak güncellendi.");

        System.out.println("Kiralama işlemi başarıyla tamamlandı.");
        System.out.println("Kiralayan: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Kiralanan Araç: " + vehicle.getBrand() + " " + vehicle.getModel());
        System.out.println("Kiralama Başlangıç: " + rentalStartDate);
        System.out.println("Kiralama Bitiş: " + rentalEndDate);
        System.out.println("Toplam Kiralama Süresi: " + rentalDuration + " " + rentType.name().toLowerCase() + (rentalDuration > 1 ? "s" : ""));
        System.out.println("Depozito: " + depositAmount + " TL");
        System.out.println("Toplam Kiralama Ücreti: " + calculatedPrice + " TL");

        return newRental;
    }

    public void returnVehicle(Long rentalId) throws SQLException {
        Rental rental = rentalDAO.findById(rentalId);

        // 1. Kiralama Kaydının Bulunup Bulunmadığını Kontrol Et
        if (rental == null) {
            System.out.println("Hata: Belirtilen ID'ye (" + rentalId + ") sahip kiralama kaydı bulunamadı.");
            return; // Kayıt yoksa işlemi durdur
        }

        rental.setRentalEndDate(LocalDateTime.now());
        rentalDAO.update(rental);
        vehicleService.markVehicleAvailable(rental.getVehicle());



    }
    public Rental findById(Long id) throws SQLException {
        Rental rental = rentalDAO.findById(id);
        if (rental == null) {
            System.out.println("Kiralama kaydı ID " + id + " bulunamadı.");
        }
        return rental;
    }
    public List<Rental> findAll() throws SQLException {
        List<Rental> rentals = rentalDAO.findAll();
        if (rentals.isEmpty()) {
            System.out.println("Sistemde kayıtlı kiralama bulunmamaktadır.");
        }
        return rentals;
    }

    public void delete (Rental rental) throws SQLException {
        Rental rentalDelete =rentalDAO.findById(rental.getId());
        if (rentalDelete==null){System.out.println("Hata: Silinecek araç bulunamadı. ID: " +rentalDelete.getId());
            return;
        }
        rentalDAO.delete(rental.getId());
        System.out.println("Araç başarıyla silindi: ID " + rental.getId());
    }

}
