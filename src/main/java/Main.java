import Model.Enums.RentType;
import Model.Enums.Role;
import Model.Enums.VehicleStatus;
import Model.Enums.VehicleType;
import Model.Rental;
import Model.User;
import Model.Vehicle;
import SERVICE.RentalService;
import SERVICE.UserService;
import SERVICE.VehicleService;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner=new Scanner(System.in);

    private static final UserService userService=new UserService();
    private static final VehicleService vehicleService = new VehicleService();
    private static final RentalService rentalService = new RentalService();

    public static void main(String[] args) throws SQLException {

        getCustomerMenu();
    }


    private static void getCustomerMenu() throws SQLException {
        while (true){
            System.out.println("-----------Müşteri Giriş Paneli'ne Hoşgeldiniz!------------");
            System.out.println(" Müşteri Kayıt Ekranı için - 1 ");
            System.out.println(" Müşteri Giriş Ekranı için - 2 ");
            System.out.println(" Çıkış için - 0 ");

            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    registerUser();
                    break;
                case "2":
                    loginUser();
                    break;
                case "0":
                    System.out.println("Uygulamadan çıkılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçim! Lütfen tekrar deneyiniz. ");
            }
        }
    }

    private static void loginUser() {
        System.out.println("----------- Müşteri Giriş Ekranına Hoşgeldiniz! ------------");
        System.out.print("E-posta adresiniz: ");
        String email = scanner.nextLine();
        System.out.print("Şifreniz: ");
        String password = scanner.nextLine();

        try {
            User loggedInUser =userService.login(email,password);
            if (loggedInUser!=null){

                mainMenu(loggedInUser);
            }

        } catch (SQLException e) {
            System.out.println("Giriş sırasında bir veritabanı hatası oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void registerUser() throws SQLException {
        System.out.println("----------- Kullanıcı Kayıt Ekranına Hoşgeldiniz! ------------");
        System.out.print("İsim: ");
        String firstName = scanner.nextLine();
        System.out.print("Soyisim: ");
        String lastName = scanner.nextLine();
        System.out.print("E-posta: ");
        String email = scanner.nextLine();
        System.out.print("Şifre: ");
        String password = scanner.nextLine();
        System.out.print("Doğum Tarihi (YYYY-MM-DD): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine()); // Kullanıcıdan doğum tarihini al
        System.out.print("Kullanıcı Tipi (CUSTOMER/CORPORATE/ADMIN): ");
        Role role = Role.valueOf(scanner.nextLine());

        try {
            userService.save(email, firstName, lastName, password,role,dateOfBirth);
            System.out.println("Kullanıcı başarıyla oluşturuldu!");
        }
        catch (SQLException e){
            System.out.println(("Kayıt sırasında bir veritabanı hatası oluştu: " + e.getMessage()));
            e.printStackTrace();
        }

    }
    private static void mainMenu(User user) throws SQLException {
        while (true) {
            System.out.println("\n----------- Ana Menüye Hoşgeldiniz! ------------");
            System.out.println("Giriş Yapan: " + user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");

            if (user.getRole() == Role.ADMIN) {
                System.out.println(" Kullanıcı İşlemleri için - 1");
                System.out.println(" Araç İşlemleri için - 2 ");
                System.out.println(" Kiralama İşlemleri (Admin) için - 3");
            } else { // BİREYSEL ve KURUMSAL kullanıcılar
                System.out.println(" Araçları Listelemek için - 1");
                System.out.println(" Araç Kiralamak için - 2");
                System.out.println(" Kiralamalarımı Görüntülemek için - 3");
                System.out.println(" Araç İade Etmek için - 4");
            }
            System.out.println(" Çıkış Yapmak için - 0");
            System.out.print(" Seçiminiz: ");

            String choice = scanner.nextLine();

            if (user.getRole() == Role.ADMIN) {
                switch (choice) {
                    case "1":
                        adminUserOperations();
                        break;
                    case "2":
                        adminVehicleOperations(user);
                        break;
                    case "3":
                        adminRentalOperations();
                        break;
                    case "0":
                        System.out.println("Çıkış yapılıyor...");
                        return; // Menüden çıkış
                    default:
                        System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                }
            } else { // BİREYSEL ve KURUMSAL kullanıcılar için
                switch (choice) {
                    case "1":
                        listAllVehicles();
                        break;
                    case "2":
                        rentVehicle(user); // Kullanıcı objesini kiralama metoduna gönder
                        break;
                    case "3":
                        viewMyRentals(user);
                        break;
                    case "4":
                        returnVehicle();
                        break;
                    case "0":
                        System.out.println("Çıkış yapılıyor...");
                        return; // Menüden çıkış
                    default:
                        System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                }
            }
        }
    }

    private static void adminUserOperations() throws SQLException {
        while (true) {
            System.out.println("\n-----------Kullanıcı İşlemleri (Admin)------------");
            System.out.println("1 - Tüm Kullanıcıları Listele");
            System.out.println("2 - Kullanıcı Bul (ID)");
            System.out.println("3 - Kullanıcı Güncelle");
            System.out.println("4 - Kullanıcı Sil");
            System.out.println("0 - Geri Dön");
            System.out.print("Seçiminiz: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    listAllUsers();
                    break;
                case "2":
                    findUserById();
                    break;
                case "3":
                    updateUser();
                    break;
                case "4":
                    deleteUser();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Geçersiz seçim.");
            }
        }
    }

    private static void deleteUser() throws SQLException { // SQLException fırlatabilir
        System.out.print("Silinecek Kullanıcı ID: ");
        long id = Long.parseLong(scanner.nextLine());
        User deleteUser =userService.findById(id);
        try {
            userService.delete(deleteUser);
            System.out.println("Kullanıcı başarıyla silindi: ID " + id);
        } catch (SQLException e) {
            System.out.println("Kullanıcı silinirken bir veritabanı hatası oluştu: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) { // ID'nin sayısal olmaması durumu için
            System.out.println("Hata: Geçersiz ID formatı. Lütfen sayı girin.");
        }
    }

    private static void listAllUsers() throws SQLException {
        System.out.println("\n-----------Tüm Kullanıcılar------------");
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Sistemde kayıtlı kullanıcı bulunmamaktadır.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void findUserById() throws SQLException {
        System.out.print("Aranacak Kullanıcı ID: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userService.findById(id);
        if (user != null) {
            System.out.println("Kullanıcı bulundu: " + user);
        } else {
            System.out.println("Belirtilen ID'de kullanıcı bulunamadı.");
        }
    }

    private static void updateUser() throws SQLException {
        System.out.print("Güncellenecek Kullanıcı ID: ");
        long id = Long.parseLong(scanner.nextLine());
        User user = userService.findById(id);
        if (user == null) {
            System.out.println("Belirtilen ID'de kullanıcı bulunamadı.");
            return;
        }

        System.out.println("Güncel bilgiler: " + user);
        System.out.print("Yeni İsim (" + user.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) user.setFirstName(firstName);

        System.out.print("Yeni Soyisim (" + user.getLastName() + "): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) user.setLastName(lastName);

        System.out.print("Yeni E-posta (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("Yeni Şifre (boş bırakırsanız değişmez): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) user.setPassword(password); // UserService içinde PasswordUtil.hashPassword() çağrılmalı

        System.out.print("Yeni Doğum Tarihi (YYYY-MM-DD) (" + user.getDateOfBirth() + "): ");
        String dobStr = scanner.nextLine();
        if (!dobStr.isEmpty()) user.setDateOfBirth(LocalDate.parse(dobStr));

        System.out.print("Yeni Rol (BIREYSEL/KURUMSAL/ADMIN) (" + user.getRole() + "): ");
        String roleStr = scanner.nextLine();
        if (!roleStr.isEmpty()) user.setRole(Role.valueOf(roleStr.toUpperCase()));

        userService.update(user);
        System.out.println("Kullanıcı başarıyla güncellendi.");
    }
    private static void deleteVehicle() throws SQLException {
        System.out.print("Silinecek Araç ID: ");
        long id = Long.parseLong(scanner.nextLine());
        Vehicle vehicleDelete =vehicleService.findById(id);
        if (vehicleDelete == null) {
            System.out.println("Hata: Belirtilen ID'de araç bulunamadı. Silme işlemi iptal edildi.");
            return;
        }
        vehicleService.delete(vehicleDelete);
    }
    private static void adminRentalOperations() throws SQLException {
        while (true) {
            System.out.println("\n-----------Kiralama İşlemleri (Admin)------------");
            System.out.println("1 - Tüm Kiralamaları Listele");
            System.out.println("2 - Kiralama Bul (ID)");
            System.out.println("3 - Kiralama Kaydı Sil");
            System.out.println("0 - Geri Dön");
            System.out.print("Seçiminiz: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    listAllRentals();
                    break;
                case "2":
                    findRentalById();
                    break;
                case "3":
                    deleteRental();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Geçersiz seçim.");
            }
        }
    }
    private static void listAllRentals() throws SQLException {
        System.out.println("\n-----------Tüm Kiralamalar------------");
        List<Rental> rentals = rentalService.findAll();
        if (rentals.isEmpty()) {
            System.out.println("Sistemde kayıtlı kiralama bulunmamaktadır.");
        } else {
            for (Rental rental : rentals) {
                // Rental objesi artık User ve Vehicle objelerini taşıdığı için
                // bilgileri bu objelerden çekebiliriz.
                System.out.println("Kiralama ID: " + rental.getId() +
                        ", Kullanıcı: " + rental.getUser().getFirstName() + " " + rental.getUser().getLastName() +
                        ", Araç: " + rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel() +
                        ", Başlangıç: " + rental.getRentalStartDate() +
                        ", Bitiş: " + rental.getRentalEndDate() +
                        ", Tip: " + rental.getRentType() +
                        ", Depozito: " + rental.getDeposit() +
                        ", Toplam Ücret: " + rental.getTotalPrice());
            }
        }
    }
    private static void findRentalById() throws SQLException {
        System.out.print("Aranacak Kiralama ID: ");
        long id = Long.parseLong(scanner.nextLine());
        Rental rental = rentalService.findById(id);
        if (rental != null) {
            System.out.println("Kiralama bulundu: " + rental);
        } else {
            System.out.println("Belirtilen ID'de kiralama bulunamadı.");
        }
    }

    private static void deleteRental() throws SQLException {
        System.out.print("Silinecek Kiralama ID: ");
        long id = Long.parseLong(scanner.nextLine());
        Rental rentalDelete =rentalService.findById(id);
        rentalService.delete(rentalDelete);
    }

    // ----------- KULLANICI KİRALAMA İŞLEMLERİ -----------
    private static void rentVehicle(User user) throws SQLException {
        System.out.println("\n-----------Araç Kirala------------");
        listAllVehicles(); // Önce araçları göster
        System.out.print("Kiralamak istediğiniz Aracın ID'si: ");
        long vehicleId;
        try {
            vehicleId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Hata: Geçersiz Araç ID formatı. Lütfen sayı girin.");
            return;
        }

        Vehicle vehicle = vehicleService.findById(vehicleId);
        if (vehicle == null) {
            System.out.println("Hata: Belirtilen ID'de araç bulunamadı.");
            return;
        }

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            System.out.println("Hata: Araç kiralama için müsait değil! Mevcut durumu: " + vehicle.getStatus());
            return;
        }

        LocalDateTime rentalStartDate = null;
        LocalDateTime rentalEndDate = null;
        try {
            System.out.print("Kiralama Başlangıç Tarihi (YYYY-MM-DD HH:MM): ");
            String startDateTimeStr = scanner.nextLine();
            rentalStartDate = LocalDateTime.parse(startDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            System.out.print("Kiralama Bitiş Tarihi (YYYY-MM-DD HH:MM): ");
            String endDateTimeStr = scanner.nextLine();
            rentalEndDate = LocalDateTime.parse(endDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Hata: Geçersiz tarih/saat formatı. Lütfen YYYY-MM-DD HH:MM formatında girin.");
            return;
        }

        RentType rentType = null;
        try {
            System.out.print("Kiralama Tipi (HOURLY/DAILY/WEEKLY/MONTHLY): ");
            rentType = RentType.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Hata: Geçersiz kiralama tipi. Lütfen belirtilen tiplerden birini girin.");
            return;
        }

        try {
            // rentalService.rentVehicle metodu tüm iş kurallarını uygulayacak
            Rental newRental = rentalService.rentVehicle(user, vehicle, rentalStartDate, rentalEndDate, rentType);

            if (newRental != null) {
                System.out.println("Kiralama işlemi başarıyla kaydedildi. Kiralama ID: " + newRental.getId());
            } else {
                System.out.println("Kiralama işlemi başarısız oldu. Lütfen yukarıdaki hata mesajlarını kontrol edin.");
            }
        } catch (SQLException e) {
            System.out.println("Kiralama sırasında bir veritabanı hatası oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewMyRentals(User user) throws SQLException {
        System.out.println("\n-----------Kiralamalarım------------");
        List<Rental> allRentals = rentalService.findAll();
        boolean found = false;
        if (allRentals.isEmpty()) {
            System.out.println("Sistemde kayıtlı kiralama bulunmamaktadır.");
        } else {
            for (Rental rental : allRentals) {
                if (rental.getUser() != null && rental.getUser().getId().equals(user.getId())) { // Sadece kendi kiralamalarını göster
                    found = true;
                    // Null kontrolü eklendi
                    String userName = (rental.getUser() != null) ? rental.getUser().getFirstName() + " " + rental.getUser().getLastName() : "Bilinmeyen Kullanıcı";
                    String vehicleInfo = (rental.getVehicle() != null) ? rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel() : "Bilinmeyen Araç";

                    System.out.println("Kiralama ID: " + rental.getId() +
                            ", Araç: " + vehicleInfo +
                            ", Başlangıç: " + rental.getRentalStartDate() +
                            ", Bitiş: " + rental.getRentalEndDate() +
                            ", Tip: " + rental.getRentType() +
                            ", Depozito: " + rental.getDeposit() +
                            ", Toplam Ücret: " + rental.getTotalPrice());
                }
            }
        }
        if (!found) {
            System.out.println("Henüz bir kiralama kaydınız bulunmamaktadır.");
        }
    }
    private static void returnVehicle() throws SQLException {
        System.out.println("\n-----------Araç İade Et------------");
        System.out.print("İade etmek istediğiniz Kiralama Kaydının ID'si: ");
        long rentalId;
        try {
            rentalId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Hata: Geçersiz Kiralama ID formatı. Lütfen sayı girin.");
            return;
        }

        try {
            // rentalService'de returnVehicle(long rentalId) metodun olmalı
            rentalService.returnVehicle(rentalId);
            System.out.println("Araç iade işlemi başarıyla tamamlandı.");
        } catch (SQLException e) {
            System.out.println("Araç iade sırasında bir veritabanı hatası oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // ----------- YÖNETİCİ ARAÇ İŞLEMLERİ METOTLARI -----------
    private static void adminVehicleOperations(User adminUser) throws SQLException {
        while (true) {
            System.out.println("\n-----------Araç İşlemleri (Admin)------------");
            System.out.println("1 - Yeni Araç Ekle");
            System.out.println("2 - Tüm Araçları Listele");
            System.out.println("3 - Araç Bul (ID)");
            System.out.println("4 - Araç Güncelle");
            System.out.println("5 - Araç Sil");
            System.out.println("0 - Geri Dön");
            System.out.print("Seçiminiz: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addVehicle(adminUser);
                    break;
                case "2":
                    listAllVehicles();
                    break;
                case "3":
                    findVehicleById();
                    break;
                case "4":
                    updateVehicle();
                    break;
                case "5":
                    deleteVehicle();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Geçersiz seçim.");
            }
        }
    }

    private static void addVehicle(User adminUser) throws SQLException {
        System.out.println("\n-----------Yeni Araç Ekle------------");
        System.out.print("Marka: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();

        VehicleType type = null;
        try {
            System.out.print("Araç Tipi (CAR/MOTORCYCLE/HELICOPTER/BOAT/PLANE): ");
            type = VehicleType.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Hata: Geçersiz araç tipi. Lütfen belirtilen tiplerden birini girin.");
            return;
        }

        BigDecimal dailyRate, hourlyRate, weeklyRate, monthlyRate, vehicleValue;
        try {
            System.out.print("Günlük Kiralama Ücreti: ");
            dailyRate = new BigDecimal(scanner.nextLine());
            System.out.print("Saatlik Kiralama Ücreti: ");
            hourlyRate = new BigDecimal(scanner.nextLine());
            System.out.print("Haftalık Kiralama Ücreti: ");
            weeklyRate = new BigDecimal(scanner.nextLine());
            System.out.print("Aylık Kiralama Ücreti: ");
            monthlyRate = new BigDecimal(scanner.nextLine());
            System.out.print("Araç Değeri (Depozito için): "); // İsim düzeltildi
            vehicleValue = new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Hata: Geçersiz ücret/değer formatı. Lütfen sayısal değer girin.");
            return;
        }


        Vehicle vehicle = new Vehicle(brand, model, type, VehicleStatus.AVAILABLE,
                hourlyRate, dailyRate, weeklyRate, monthlyRate, vehicleValue);
        try {
            vehicleService.save(vehicle,adminUser);
            System.out.println("Araç başarıyla eklendi.");
        } catch (SQLException e) {
            System.out.println("Araç eklenirken bir veritabanı hatası oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listAllVehicles() throws SQLException {
        System.out.println("\n-----------Tüm Araçlar------------");
        List<Vehicle> vehicles = vehicleService.findAll();
        if (vehicles.isEmpty()) {
            System.out.println("Sistemde kayıtlı araç bulunmamaktadır.");
        } else {
            vehicles.forEach(System.out::println);
        }
    }

    private static void findVehicleById() throws SQLException {
        System.out.print("Aranacak Araç ID: ");
        long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Hata: Geçersiz ID formatı. Lütfen sayı girin.");
            return;
        }
        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle != null) {
            System.out.println("Araç bulundu: " + vehicle);
        } else {
            System.out.println("Belirtilen ID'de araç bulunamadı.");
        }
    }

    private static void updateVehicle() throws SQLException {
        System.out.print("Güncellenecek Araç ID: ");
        long id = Long.parseLong(scanner.nextLine());
        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle == null) {
            System.out.println("Belirtilen ID'de araç bulunamadı.");
            return;
        }

        System.out.println("Güncel bilgiler: " + vehicle);
        System.out.print("Yeni Marka (" + vehicle.getBrand() + "): ");
        String brand = scanner.nextLine();
        if (!brand.isEmpty()) vehicle.setBrand(brand);

        System.out.print("Yeni Model (" + vehicle.getModel() + "): ");
        String model = scanner.nextLine();
        if (!model.isEmpty()) vehicle.setModel(model);

        // Vehicle sınıfınızda `year` ve `color` olmadığı için bu kısımlar kaldırıldı.

        System.out.print("Yeni Tip (CAR/MOTORCYCLE/HELICOPTER/BOAT/PLANE) (" + vehicle.getType() + "): ");
        String typeStr = scanner.nextLine();
        if (!typeStr.isEmpty()) {
            try {
                vehicle.setType(Model.Enums.VehicleType.valueOf(typeStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Hata: Geçersiz araç tipi. Tip güncellenemedi.");
            }
        }

        System.out.print("Yeni Durum (AVAILABLE/RENTED/MAINTENANCE) (" + vehicle.getStatus() + "): ");
        String statusStr = scanner.nextLine();
        if (!statusStr.isEmpty()) {
            try {
                vehicle.setStatus(Model.Enums.VehicleStatus.valueOf(statusStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Hata: Geçersiz araç durumu. Durum güncellenemedi.");
            }
        }

        System.out.print("Yeni Günlük Kiralama Ücreti (" + vehicle.getDailyRate() + "): ");
        String rateStr = scanner.nextLine();
        if (!rateStr.isEmpty()) {
            try {
                vehicle.setDailyRate(new BigDecimal(rateStr));
            } catch (NumberFormatException e) {
                System.out.println("Hata: Geçersiz ücret formatı. Günlük kiralama ücreti güncellenemedi.");
            }
        }

        System.out.print("Yeni Saatlik Kiralama Ücreti (" + vehicle.getHourlyRate() + "): ");
        String hourlyRateStr = scanner.nextLine();
        if (!hourlyRateStr.isEmpty()) {
            try {
                vehicle.setHourlyRate(new BigDecimal(hourlyRateStr));
            } catch (NumberFormatException e) {
                System.out.println("Hata: Geçersiz ücret formatı. Saatlik kiralama ücreti güncellenemedi.");
            }
        }

        System.out.print("Yeni Haftalık Kiralama Ücreti (" + vehicle.getWeeklyRate() + "): ");
        String weeklyRateStr = scanner.nextLine();
        if (!weeklyRateStr.isEmpty()) {
            try {
                vehicle.setWeeklyRate(new BigDecimal(weeklyRateStr));
            } catch (NumberFormatException e) {
                System.out.println("Hata: Geçersiz ücret formatı. Haftalık kiralama ücreti güncellenemedi.");
            }
        }

        System.out.print("Yeni Aylık Kiralama Ücreti (" + vehicle.getMonthlyRate() + "): ");
        String monthlyRateStr = scanner.nextLine();
        if (!monthlyRateStr.isEmpty()) {
            try {
                vehicle.setMonthlyRate(new BigDecimal(monthlyRateStr));
            } catch (NumberFormatException e) {
                System.out.println("Hata: Geçersiz ücret formatı. Aylık kiralama ücreti güncellenemedi.");
            }
        }

        // Düzeltildi: 'Depozito Ücreti' yerine 'Araç Değeri (Depozito için)' kullanıldı
        // ve vehicle.getDeposit() yerine vehicle.getVehicleValue() kullanıldı.
        System.out.print("Yeni Araç Değeri (Depozito için) (" + vehicle.getVehicleValue() + "): ");
        String vehicleValueStr = scanner.nextLine();
        if (!vehicleValueStr.isEmpty()) {
            try {
                vehicle.setVehicleValue(new BigDecimal(vehicleValueStr));
            } catch (NumberFormatException e) {
                System.out.println("Hata: Geçersiz değer formatı. Araç değeri güncellenemedi.");
            }
        }

        try {
            vehicleService.update(vehicle);
            System.out.println("Araç başarıyla güncellendi.");
        } catch (SQLException e) {
            System.out.println("Araç güncellenirken bir veritabanı hatası oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

}