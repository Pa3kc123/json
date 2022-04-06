package sk.pa3kc.data;

import lombok.Data;

public @Data class Change {
    private String kind;
    private String type;
    private String changeType;
    private String time;  //!datetime
    private boolean removed;
    private String fileId;
    private File file;
    private String teamDriveId;
    private String driveId;
    private Drive teamDrive;
    private Drive drive;
}
