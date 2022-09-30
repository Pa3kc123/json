package sk.pa3kc.data2;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Date;
import java.util.List;

import sk.pa3kc.json.ann.JsonKey;
import sk.pa3kc.json.ann.JsonValueFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Issue {
    private Long id;
    private User project;
    private User tracker;
    private User status;
    private User priority;
    private User author;
    @JsonKey("assigned_to")
    @JsonAlias("assigned_to")
    private AssignedTo assignedTo;
    private String subject;
    private String description;
    @JsonKey("start_date")
    @JsonAlias("start_date")
    @JsonValueFormat("yyyy-MM-dd")
    private Date startDate;
    @JsonKey("due_date")
    @JsonAlias("due_date")
    @JsonValueFormat("yyyy-MM-dd")
    private Date dueDate;
    @JsonKey("done_ratio")
    @JsonAlias("done_ratio")
    private Long doneRatio;
    @JsonKey("is_private")
    @JsonAlias("is_private")
    private Boolean isPrivate;
    @JsonKey("is_favorited")
    @JsonAlias("is_favorited")
    private Boolean isFavorited;
    @JsonKey("estimated_hours")
    @JsonAlias("estimated_hours")
    private String estimatedHours;
    @JsonKey("custom_fields")
    @JsonAlias("custom_fields")
    private List<CustomField> customFields;
    @JsonKey("created_on")
    @JsonAlias("created_on")
    private Date createdOn;
    @JsonKey("updated_on")
    @JsonAlias("updated_on")
    private Date updatedOn;
    @JsonKey("closed_on")
    @JsonAlias("closed_on")
    private Date closedOn;
}
