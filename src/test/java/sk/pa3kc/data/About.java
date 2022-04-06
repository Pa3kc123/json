package sk.pa3kc.data;

import java.util.List;
import java.util.Map;

import lombok.Data;
import sk.pa3kc.json.ann.JsonOptions;

@JsonOptions(ignoreMissing = true)
public @Data class About {
    /** The authenticated user. */
    public User user;
    /** The user's storage quota limits and usage. All fields are measured in bytes. */
    public StorageQuota storageQuota;
    /** A map of source MIME type to possible targets for all supported imports. */
    public Map<String, String[]> importFormats;
    /** A map of source MIME type to possible targets for all supported exports. */
    public Map<String, String[]> exportFormats;
    /** A map of maximum import sizes by MIME type, in bytes. */
    public Map<String, Long> maxImportSizes;
    /** The maximum upload size in bytes. */
    public Long maxUploadSize;
    /** Whether the user has installed the requesting app. */
    public Boolean appInstalled;
    /** The currently supported folder colors as RGB hex strings. */
    public List<String> folderColorPalette;
    /** Warning: This item is deprecated.Deprecated - use driveThemes instead. */
    public DriveTheme[] teamDriveThemes;
    /** Warning: This item is deprecated.Deprecated - use canCreateDrives instead. */
    public Boolean canCreateTeamDrives;
    /** A list of themes that are supported for shared drives. */
    public List<DriveTheme> driveThemes;
    /** Whether the user can create shared drives. */
    public Boolean canCreateDrives;
}
