package sk.pa3kc.data;

import lombok.Data;
import sk.pa3kc.json.ann.JsonOptions;

@JsonOptions(ignoreMissing = true)
public @Data class User {
    /** A plain text displayable name for this user. */
    public String displayName;
    /** A link to the user's profile photo, if available. */
    public String photoLink;
    /** Whether this user is the requesting user. */
    public Boolean me;
    /** The user's ID as visible in Permission resources. */
    public String permissionId;
    /** The email address of the user. This may not be present in certain contexts if the user has not made their email address visible to the requester. */
    public String emailAddress;
}
