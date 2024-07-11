package Model;

import java.sql.Date;

public class SubscriptionModel {

    private long subscription_id;
    private long userId;
    private Date startDate;
    private String status;
    private String courseId;
    private String courseActivityId;

    public long getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(long subscription_id) {
        this.subscription_id = subscription_id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseActivityId() {
        return courseActivityId;
    }

    public void setCourseActivityId(String courseActivityId) {
        this.courseActivityId = courseActivityId;
    }
}
