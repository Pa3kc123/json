package sk.pa3kc.data;

import java.util.Map;

import lombok.Data;

public @Data class Drive {
    /** Identifies what kind of resource this is. Value: the fixed string "drive#drive". */
    private String kind;
    /** The ID of this shared drive which is also the ID of the top level folder of this shared drive. */
    private String id;
    /** The name of this shared drive. */
    private String name;
    /** The ID of the theme from which the background image and color will be set. The set of possible driveThemes can be retrieved from a drive.about.get response.  When not specified on a drive.drives.create request, a random theme is chosen from which the background image and color are set.  This is a write-only field; it can only be set on requests that don't set colorRgb or backgroundImageFile. */
    private String themeId;
    /** The color of this shared drive as an RGB hex string.  It can only be set on a drive.drives.update request that does not set themeId. */
    private String colorRgb;
    /** An image file and cropping parameters from which a background image for this shared drive is set.  This is a write only field; it can only be set on drive.drives.update requests that don't set themeId.  When specified, all fields of the backgroundImageFile must be set. */
    private Map<String, String> backgroundImageFile;
    /** A short-lived link to this shared drive's background image. */
    private String backgroundImageLink;
    /** Capabilities the current user has on this shared drive. */
    private Map<String, String> capabilities;
    /** The time at which the shared drive was created (RFC 3339 date-time). */
    private Long createdTime;
    /** Whether the shared drive is hidden from default view. */
    private Boolean hidden;
    /** A set of restrictions that apply to this shared drive or items inside this shared drive. */
    private Map<String, String> restrictions;
}
