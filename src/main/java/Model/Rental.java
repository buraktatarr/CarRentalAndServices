package Model;

import Model.Enums.RentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Rental extends BaseModel  {

    private  User user;
    private Vehicle vehicle;
    private LocalDateTime rentalStartDate;
    private LocalDateTime rentalEndDate;
    private RentType rentType ;
    private BigDecimal deposit;
    private BigDecimal totalPrice;

    public Rental() {
    }

    public Rental(User user, Vehicle vehicle, LocalDateTime rentalStartDate, LocalDateTime rentalEndDate, RentType rentType, BigDecimal deposit, BigDecimal totalPrice) {
        this.user = user;
        this.vehicle = vehicle;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.rentType = rentType;
        this.deposit = deposit;
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDateTime rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDateTime getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(LocalDateTime rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public RentType getRentType() {
        return rentType;
    }

    public void setRentType(RentType rentType) {
        this.rentType = rentType;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
