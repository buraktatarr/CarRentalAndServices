package Model;

import Model.Enums.VehicleStatus;
import Model.Enums.VehicleType;

import java.math.BigDecimal;

public class Vehicle extends BaseModel{

    private String brand;
    private String model;
    private VehicleType type;
    private VehicleStatus status;

    private BigDecimal vehicleValue; // Araç bedeli (depozito hesabı için)

    // Farklı kiralama süreleri için fiyatlar
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private BigDecimal weeklyRate;
    private BigDecimal monthlyRate;


    // Parametresiz Yapıcı Metot
    public Vehicle() {
    }

    // Parametreli Yapıcı Metot
    // ID, createdDate, updatedDate genellikle sistem tarafından yönetilir
    public Vehicle(String brand, String model, VehicleType type, VehicleStatus status,
                   BigDecimal hourlyRate, BigDecimal dailyRate, BigDecimal weeklyRate, BigDecimal monthlyRate, BigDecimal vehicleValue) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.status = status;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.weeklyRate = weeklyRate;
        this.monthlyRate = monthlyRate;
        this.vehicleValue = vehicleValue;
    }

    // Getter ve Setter Metotları (BaseModel'deki getter/setter'lar hariç)

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getWeeklyRate() {
        return weeklyRate;
    }

    public void setWeeklyRate(BigDecimal weeklyRate) {
        this.weeklyRate = weeklyRate;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getVehicleValue() {
        return vehicleValue;
    }

    public void setVehicleValue(BigDecimal vehicleValue) {
        this.vehicleValue = vehicleValue;
    }
}
