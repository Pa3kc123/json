package sk.pa3kc.data;

import java.util.Map;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;
import sk.pa3kc.json.ann.JsonOptions;

@JsonOptions(ignoreMissing = true)
public @Data class File {
    /** The ID of the file. */
    private String id;
    /** The name of the file. This is not necessarily unique within a folder. Note that for immutable items such as the top level folders of shared drives, My Drive root folder, and Application Data folder the name is constant. */
    private String name;
    /** The MIME type of the file.  Google Drive will attempt to automatically detect an appropriate value from uploaded content if no value is provided. The value cannot be changed unless a new revision is uploaded.  If a file is created with a Google Doc MIME type, the uploaded content will be imported if possible. The supported import formats are published in the About resource. */
    private String mimeType;
    /** A short description of the file. */
    private String description;
    /** Whether the user has starred the file. */
    private Boolean starred;
    /** Whether the file has been trashed, either explicitly or from a trashed parent folder. Only the owner may trash a file. The trashed item is excluded from all files.list responses returned for any user who does not own the file. However, all users with access to the file can see the trashed item metadata in an API response. All users with access can copy, download, export, and share the file. */
    private Boolean trashed;
    /** Whether the file has been explicitly trashed, as opposed to recursively trashed from a parent folder. */
    private Boolean explicitlyTrashed;
    /** The IDs of the parent folders which contain the file.  If not specified as part of a create request, the file will be placed directly in the user's My Drive folder. If not specified as part of a copy request, the file will inherit any discoverable parents of the source file. Update requests must use the addParents and removeParents parameters to modify the parents list. */
    private List<String> parents;
    /** A collection of arbitrary key-value pairs which are private to the requesting app.  Entries with null values are cleared in update and copy requests.  These properties can only be retrieved using an authenticated request. An authenticated request uses an access token obtained with a OAuth 2 client ID. You cannot use an API key to retrieve private properties. */
    private Map<String, String> appProperties;
    /** The list of spaces which contain the file. The currently supported values are 'drive', 'appDataFolder' and 'photos'. */
    private List<String> spaces;
    /** A link for downloading the content of the file in a browser. This is only available for files with binary content in Google Drive. */
    private String webContentLink;
    /** A link for opening the file in a relevant Google editor or viewer in a browser. */
    private String webViewLink;
    /** A static, unauthenticated link to the file's icon. */
    private String iconLink;
    /** A short-lived link to the file's thumbnail, if available. Typically lasts on the order of hours. Only populated when the requesting app can access the file's content. If the file isn't shared publicly, the URL returned in Files.thumbnailLink must be fetched using a credentialed request. */
    private String thumbnailLink;
    /** Whether the file has been viewed by this user. */
    private Boolean viewedByMe;
    /** The last time the file was viewed by the user (RFC 3339 date-time). */
    private Long viewedByMeTime;
    /** The time at which the file was created (RFC 3339 date-time). */
    private Long createdTime;
    /** The last time the file was modified by anyone (RFC 3339 date-time).  Note that setting modifiedTime will also update modifiedByMeTime for the user. */
    private Long modifiedTime;
    /** The last time the file was modified by the user (RFC 3339 date-time). */
    private Long modifiedByMeTime;
    /** The time at which the file was shared with the user, if applicable (RFC 3339 date-time). */
    private Long sharedWithMeTime;
    /** The user who shared the file with the requesting user, if applicable. */
    private User sharingUser;
    /** The owner of this file. Only certain legacy files may have more than one owner. This field isn't populated for items in shared drives. */
    private List<String> owners;
    /** The last user to modify the file. */
    private User lastModifyingUser;
    /** Whether the file has been shared. Not populated for items in shared drives. */
    private Boolean shared;
    /** Whether the user owns the file. Not populated for items in shared drives. */
    private Boolean ownedByMe;
    /** Warning: This item is deprecated.Deprecated - use copyRequiresWriterPermission instead. */
    private Boolean viewersCanCopyContent;
    /** Whether users with only writer permission can modify the file's permissions. Not populated for items in shared drives. */
    private Boolean writersCanShare;
    /** The full list of permissions for the file. This is only available if the requesting user can share the file. Not populated for items in shared drives. */
    private List<String> permissions;
    /** The original filename of the uploaded content if available, or else the original value of the name field. This is only available for files with binary content in Google Drive. */
    private String originalFilename;
    /** The full file extension extracted from the name field. May contain multiple concatenated extensions, such as "tar.gz". This is only available for files with binary content in Google Drive.  This is automatically updated when the name field changes, however it is not cleared if the new name does not contain a valid extension. */
    private String fullFileExtension;
    /** The final component of fullFileExtension. This is only available for files with binary content in Google Drive. */
    private String fileExtension;
    /** The MD5 checksum for the content of the file. This is only applicable to files with binary content in Google Drive. */
    private String md5Checksum;
    /** The size of the file's content in bytes. This is applicable to binary files in Google Drive and Google Docs files. */
    private BigInteger size;
    /** The number of storage quota bytes used by the file. This includes the head revision as well as previous revisions with keepForever enabled. */
    private Long quotaBytesUsed;
    /** The ID of the file's head revision. This is currently only available for files with binary content in Google Drive. */
    private String headRevisionId;
    /** Additional information about the content of the file. These fields are never populated in responses. */
    private Map<String, String> contentHints;
    /** Additional metadata about image media, if available. */
    private Map<String, String> imageMediaMetadata;
    /** Additional metadata about video media. This may not be available immediately upon upload. */
    private Map<String, String> videoMediaMetadata;
    /** Capabilities the current user has on this file. Each capability corresponds to a fine-grained action that a user may take. */
    private Map<String, String> capabilities;
    /** Whether the file was created or opened by the requesting app. */
    private Boolean isAppAuthorized;
    /** Whether this file has a thumbnail. This does not indicate whether the requesting app has access to the thumbnail. To check access, look for the presence of the thumbnailLink field. */
    private Boolean hasThumbnail;
    /** The thumbnail version for use in thumbnail cache invalidation. */
    private Long thumbnailVersion;
    /** Whether the file has been modified by this user. */
    private Boolean modifiedByMe;
    /** If the file has been explicitly trashed, the user who trashed it. Only populated for items in shared drives. */
    private User trashingUser;
    /** The time that the item was trashed (RFC 3339 date-time). Only populated for items in shared drives. */
    private Long trashedTime;
    /** Warning: This item is deprecated.Deprecated - use driveId instead. */
    private String teamDriveId;
    /** Whether there are permissions directly on this file. This field is only populated for items in shared drives. */
    private Boolean hasAugmentedPermissions;
    /** List of permission IDs for users with access to this file. */
    private List<String> permissionIds;
    /** Whether the options to copy, print, or download this file, should be disabled for readers and commenters. */
    private Boolean copyRequiresWriterPermission;
    /** Links for exporting Docs Editors files to specific formats. */
    private Map<String, String> exportLinks;
    /** ID of the shared drive the file resides in. Only populated for items in shared drives. */
    private String driveId;
    /** Shortcut file details. Only populated for shortcut files, which have the mimeType field set to application/vnd.google-apps.shortcut. */
    private Map<String, String> shortcutDetails;
    /** Restrictions for accessing the content of the file. Only populated if such a restriction exists. */
    private List<String> contentRestrictions;
    /** A key needed to access the item via a shared link. */
    private String resourceKey;
    /** Contains details about the link URLs that clients are using to refer to this item. */
    private Map<String, String> linkShareMetadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Boolean getTrashed() {
        return trashed;
    }

    public void setTrashed(Boolean trashed) {
        this.trashed = trashed;
    }

    public Boolean getExplicitlyTrashed() {
        return explicitlyTrashed;
    }

    public void setExplicitlyTrashed(Boolean explicitlyTrashed) {
        this.explicitlyTrashed = explicitlyTrashed;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public Map<String, String> getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(Map<String, String> appProperties) {
        this.appProperties = appProperties;
    }

    public List<String> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<String> spaces) {
        this.spaces = spaces;
    }

    public String getWebContentLink() {
        return webContentLink;
    }

    public void setWebContentLink(String webContentLink) {
        this.webContentLink = webContentLink;
    }

    public String getWebViewLink() {
        return webViewLink;
    }

    public void setWebViewLink(String webViewLink) {
        this.webViewLink = webViewLink;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public Boolean getViewedByMe() {
        return viewedByMe;
    }

    public void setViewedByMe(Boolean viewedByMe) {
        this.viewedByMe = viewedByMe;
    }

    public Long getViewedByMeTime() {
        return viewedByMeTime;
    }

    public void setViewedByMeTime(Long viewedByMeTime) {
        this.viewedByMeTime = viewedByMeTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getModifiedByMeTime() {
        return modifiedByMeTime;
    }

    public void setModifiedByMeTime(Long modifiedByMeTime) {
        this.modifiedByMeTime = modifiedByMeTime;
    }

    public Long getSharedWithMeTime() {
        return sharedWithMeTime;
    }

    public void setSharedWithMeTime(Long sharedWithMeTime) {
        this.sharedWithMeTime = sharedWithMeTime;
    }

    public User getSharingUser() {
        return sharingUser;
    }

    public void setSharingUser(User sharingUser) {
        this.sharingUser = sharingUser;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

    public User getLastModifyingUser() {
        return lastModifyingUser;
    }

    public void setLastModifyingUser(User lastModifyingUser) {
        this.lastModifyingUser = lastModifyingUser;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getOwnedByMe() {
        return ownedByMe;
    }

    public void setOwnedByMe(Boolean ownedByMe) {
        this.ownedByMe = ownedByMe;
    }

    public Boolean getViewersCanCopyContent() {
        return viewersCanCopyContent;
    }

    public void setViewersCanCopyContent(Boolean viewersCanCopyContent) {
        this.viewersCanCopyContent = viewersCanCopyContent;
    }

    public Boolean getWritersCanShare() {
        return writersCanShare;
    }

    public void setWritersCanShare(Boolean writersCanShare) {
        this.writersCanShare = writersCanShare;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFullFileExtension() {
        return fullFileExtension;
    }

    public void setFullFileExtension(String fullFileExtension) {
        this.fullFileExtension = fullFileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getMd5Checksum() {
        return md5Checksum;
    }

    public void setMd5Checksum(String md5Checksum) {
        this.md5Checksum = md5Checksum;
    }

    public BigInteger getSize() {
        return size;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }

    public Long getQuotaBytesUsed() {
        return quotaBytesUsed;
    }

    public void setQuotaBytesUsed(Long quotaBytesUsed) {
        this.quotaBytesUsed = quotaBytesUsed;
    }

    public String getHeadRevisionId() {
        return headRevisionId;
    }

    public void setHeadRevisionId(String headRevisionId) {
        this.headRevisionId = headRevisionId;
    }

    public Map<String, String> getContentHints() {
        return contentHints;
    }

    public void setContentHints(Map<String, String> contentHints) {
        this.contentHints = contentHints;
    }

    public Map<String, String> getImageMediaMetadata() {
        return imageMediaMetadata;
    }

    public void setImageMediaMetadata(Map<String, String> imageMediaMetadata) {
        this.imageMediaMetadata = imageMediaMetadata;
    }

    public Map<String, String> getVideoMediaMetadata() {
        return videoMediaMetadata;
    }

    public void setVideoMediaMetadata(Map<String, String> videoMediaMetadata) {
        this.videoMediaMetadata = videoMediaMetadata;
    }

    public Map<String, String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, String> capabilities) {
        this.capabilities = capabilities;
    }

    public Boolean getAppAuthorized() {
        return isAppAuthorized;
    }

    public void setAppAuthorized(Boolean appAuthorized) {
        isAppAuthorized = appAuthorized;
    }

    public Boolean getHasThumbnail() {
        return hasThumbnail;
    }

    public void setHasThumbnail(Boolean hasThumbnail) {
        this.hasThumbnail = hasThumbnail;
    }

    public Long getThumbnailVersion() {
        return thumbnailVersion;
    }

    public void setThumbnailVersion(Long thumbnailVersion) {
        this.thumbnailVersion = thumbnailVersion;
    }

    public Boolean getModifiedByMe() {
        return modifiedByMe;
    }

    public void setModifiedByMe(Boolean modifiedByMe) {
        this.modifiedByMe = modifiedByMe;
    }

    public User getTrashingUser() {
        return trashingUser;
    }

    public void setTrashingUser(User trashingUser) {
        this.trashingUser = trashingUser;
    }

    public Long getTrashedTime() {
        return trashedTime;
    }

    public void setTrashedTime(Long trashedTime) {
        this.trashedTime = trashedTime;
    }

    public String getTeamDriveId() {
        return teamDriveId;
    }

    public void setTeamDriveId(String teamDriveId) {
        this.teamDriveId = teamDriveId;
    }

    public Boolean getHasAugmentedPermissions() {
        return hasAugmentedPermissions;
    }

    public void setHasAugmentedPermissions(Boolean hasAugmentedPermissions) {
        this.hasAugmentedPermissions = hasAugmentedPermissions;
    }

    public List<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<String> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Boolean getCopyRequiresWriterPermission() {
        return copyRequiresWriterPermission;
    }

    public void setCopyRequiresWriterPermission(Boolean copyRequiresWriterPermission) {
        this.copyRequiresWriterPermission = copyRequiresWriterPermission;
    }

    public Map<String, String> getExportLinks() {
        return exportLinks;
    }

    public void setExportLinks(Map<String, String> exportLinks) {
        this.exportLinks = exportLinks;
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public Map<String, String> getShortcutDetails() {
        return shortcutDetails;
    }

    public void setShortcutDetails(Map<String, String> shortcutDetails) {
        this.shortcutDetails = shortcutDetails;
    }

    public List<String> getContentRestrictions() {
        return contentRestrictions;
    }

    public void setContentRestrictions(List<String> contentRestrictions) {
        this.contentRestrictions = contentRestrictions;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public Map<String, String> getLinkShareMetadata() {
        return linkShareMetadata;
    }

    public void setLinkShareMetadata(Map<String, String> linkShareMetadata) {
        this.linkShareMetadata = linkShareMetadata;
    }
}
