package sk.pa3kc.data;

import lombok.Data;

public @Data class StorageQuota {
    /** The usage limit, if applicable. This will not be present if the user has unlimited storage. */
    public long limit;
    /** The total usage across all services. */
    public long usage;
    /** The usage by all files in Google Drive. */
    public long usageInDrive;
    /** The usage by trashed files in Google Drive. */
    public long usageInDriveTrash;
}
