package model.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private float hoursBalance;
    private boolean active;
    private double longitud;
    private double latitud;
    private Long rolId;

    public User() {}

    public User(String name, String phone, String email, String password, double longitud, double latitud) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.hoursBalance = 0f;
        this.active = false;
        this.longitud = longitud;
        this.latitud = latitud;
    }


    public Long getRolId() {
        return rolId;
    }
    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getHoursBalance() {
        return hoursBalance;
    }

    public void setHoursBalance(float hoursBalance) {
        this.hoursBalance = hoursBalance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
