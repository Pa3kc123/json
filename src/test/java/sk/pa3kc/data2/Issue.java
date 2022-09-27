package sk.pa3kc.data2;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class Issue {
    private Long id;
    private User project;
    private User tracker;
    private User status;
    private User priority;
    private User author;
    private AssignedTo assignedTo;
    private String subject;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Long doneRatio;
    private Boolean isPrivate;
    private Boolean isFavorited;
    private Object estimatedHours;
    private List<CustomField> customFields;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    private Object closedOn;

    public Long getID() { return id; }
    public void setID(Long value) { this.id = value; }

    public User getProject() { return project; }
    public void setProject(User value) { this.project = value; }

    public User getTracker() { return tracker; }
    public void setTracker(User value) { this.tracker = value; }

    public User getStatus() { return status; }
    public void setStatus(User value) { this.status = value; }

    public User getPriority() { return priority; }
    public void setPriority(User value) { this.priority = value; }

    public User getAuthor() { return author; }
    public void setAuthor(User value) { this.author = value; }

    public AssignedTo getAssignedTo() { return assignedTo; }
    public void setAssignedTo(AssignedTo value) { this.assignedTo = value; }

    public String getSubject() { return subject; }
    public void setSubject(String value) { this.subject = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate value) { this.startDate = value; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate value) { this.dueDate = value; }

    public Long getDoneRatio() { return doneRatio; }
    public void setDoneRatio(Long value) { this.doneRatio = value; }

    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean value) { this.isPrivate = value; }

    public Boolean getIsFavorited() { return isFavorited; }
    public void setIsFavorited(Boolean value) { this.isFavorited = value; }

    public Object getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Object value) { this.estimatedHours = value; }

    public List<CustomField> getCustomFields() { return customFields; }
    public void setCustomFields(List<CustomField> value) { this.customFields = value; }

    public OffsetDateTime getCreatedOn() { return createdOn; }
    public void setCreatedOn(OffsetDateTime value) { this.createdOn = value; }

    public OffsetDateTime getUpdatedOn() { return updatedOn; }
    public void setUpdatedOn(OffsetDateTime value) { this.updatedOn = value; }

    public Object getClosedOn() { return closedOn; }
    public void setClosedOn(Object value) { this.closedOn = value; }
}
