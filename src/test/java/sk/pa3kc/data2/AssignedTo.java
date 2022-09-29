package sk.pa3kc.data2;

import sk.pa3kc.json.ann.JsonKey;

public class AssignedTo {
    private Long id;
    private String name;
    @JsonKey("avatar_urls")
    private AvatarUrls avatarUrls;

    public Long getId() { return id; }
    public void setId(Long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public AvatarUrls getAvatarUrls() { return avatarUrls; }
    public void setAvatarUrls(AvatarUrls value) { this.avatarUrls = value; }
}
