package Model.course;

import java.util.Collections;
import java.util.List;

public class CourseResourceRequest {

    private String courseId;
    private List<Content> contentList;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<Content> getContentList() {
        return Collections.unmodifiableList(contentList);
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = Collections.unmodifiableList(contentList);
    }
}

