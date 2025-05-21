package Model;

import java.time.LocalDateTime;

public class BaseModel {
    private  Long id ; // ID alanı, genellikle veritabanı tarafından atanır

    private LocalDateTime createdDate ; // Kaydın oluşturulduğu tarih/saat

    private LocalDateTime updatedDate ; // Kaydın en son güncellendiği tarih/saat

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


    public LocalDateTime getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
