package sk.pa3kc.data2;

import java.util.Date;
import java.util.List;

import sk.pa3kc.json.ann.JsonKey;

public class Issue {
    private Long id;
    private User project;
    private User tracker;
    private User status;
    private User priority;
    private User author;
    @JsonKey("assigned_to")
    private AssignedTo assignedTo;
    private String subject;
    private String description;
    @JsonKey("start_date")
    private Date startDate;
    @JsonKey("due_date")
    private Date dueDate;
    @JsonKey("done_ratio")
    private Long doneRatio;
    @JsonKey("is_private")
    private Boolean isPrivate;
    @JsonKey("is_favorited")
    private Boolean isFavorited;
    @JsonKey("estimated_hours")
    private String estimatedHours;
    @JsonKey("custom_fields")
    private List<CustomField> customFields;
    @JsonKey("created_on")
    private Date createdOn;
    @JsonKey("updated_on")
    private Date updatedOn;
    @JsonKey("closed_on")
    private Date closedOn;

    public Long getId() { return id; }
    public void setId(Long value) { this.id = value; }

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

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date value) { this.startDate = value; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date value) { this.dueDate = value; }

    public Long getDoneRatio() { return doneRatio; }
    public void setDoneRatio(Long value) { this.doneRatio = value; }

    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean value) { this.isPrivate = value; }

    public Boolean getIsFavorited() { return isFavorited; }
    public void setIsFavorited(Boolean value) { this.isFavorited = value; }

    public String getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(String value) { this.estimatedHours = value; }

    public List<CustomField> getCustomFields() { return customFields; }
    public void setCustomFields(List<CustomField> value) { this.customFields = value; }

    public Date getCreatedOn() { return createdOn; }
    public void setCreatedOn(Date value) { this.createdOn = value; }

    public Date getUpdatedOn() { return updatedOn; }
    public void setUpdatedOn(Date value) { this.updatedOn = value; }

    public Date getClosedOn() { return closedOn; }
    public void setClosedOn(Date value) { this.closedOn = value; }
}
