package model.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.time.LocalTime;

@Entity(tableName = "application")
public class Application {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String type;
    private String status;
    private float hoursRequested;
    private String created;
    @SerializedName("fromTime")
    private String fromTime;
    @SerializedName("toTime")
    private String toTime;
    private String resolved;
    private Boolean urgent;
    private Long userId;
    private Long resolverId;

    public Application() {
    }

    public Application(Long id, String type, String status, float hoursRequested,
                       String created, String fromTime, String toTime, String resolved,
                       boolean urgent, Long userId, Long resolverId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.hoursRequested = hoursRequested;
        this.created = created;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.resolved = resolved;
        this.urgent = urgent;
        this.userId = userId;
        this.resolverId = resolverId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getHoursRequested() {
        return hoursRequested;
    }

    public void setHoursRequested(float hoursRequested) {
        this.hoursRequested = hoursRequested;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public Boolean getUrgent() {
        return urgent;
    }

    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getResolverId() {
        return resolverId;
    }

    public void setResolverId(Long resolverId) {
        this.resolverId = resolverId;
    }
}