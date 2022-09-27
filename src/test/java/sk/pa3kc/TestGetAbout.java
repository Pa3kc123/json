package sk.pa3kc;

import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sk.pa3kc.data.DriveTheme;
import sk.pa3kc.data.StorageQuota;
import sk.pa3kc.data.User;

import lombok.Data;

public @Data class TestGetAbout {
    private String kind;
    private User user;
    private StorageQuota storageQuota;
    private Map<String, AbstractList<String>> importFormats;
    private AbstractMap<String, List<String>> exportFormats;
    private LinkedHashMap<String, BigDecimal> maxImportSizes;
    private long maxUploadSize;
    private boolean appInstalled;
    private String[] folderColorPalette;
    private Collection<DriveTheme> teamDriveThemes;
    private MList<DriveTheme> driveThemes;
    private Boolean canCreateTeamDrives;
    private Boolean canCreateDrives;
}
