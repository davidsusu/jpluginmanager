package hu.webarticum.jpluginmanager.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {
    
    private final int major;
    
    private final int minor;
    
    private final int patch;

    private final String pre;

    private final String build;
    
    public Version(String versionString) {
        Pattern pattern = Pattern.compile("^[=v]*(\\d+)\\.(\\d+)\\.(\\d+)(?:\\-([0-9A-Za-z\\-]+))?(?:\\+([0-9A-Za-z-]+))?$");
        Matcher matcher = pattern.matcher(versionString);
        if (matcher.find()) {
            String majorMatch = matcher.group(1);
            String minorMatch = matcher.group(2);
            String patchMatch = matcher.group(3);
            String preMatch = matcher.group(4);
            String buildMatch = matcher.group(5);
            this.major = Integer.parseInt(majorMatch);
            this.minor = Integer.parseInt(minorMatch);
            this.patch = Integer.parseInt(patchMatch);
            this.pre = preMatch == null ? "" : preMatch;
            this.build = buildMatch == null ? "" : buildMatch;
        } else {
            this.major = 0;
            this.minor = 0;
            this.patch = 0;
            this.pre = "";
            this.build = "";
        }
    }
    
    public Version(int major, int minor, int patch) {
        this(major, minor, patch, "", "");
    }

    public Version(int major, int minor, int patch, String pre) {
        this(major, minor, patch, pre, "");
    }

    public Version(int major, int minor, int patch, String pre, String build) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.pre = pre;
        this.build = build;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getPre() {
        return pre;
    }

    public String getBuild() {
        return build;
    }

    @Override
    public int compareTo(Version o) {
        // TODO
        return 0;
    }

    public boolean matches(String versionMatcher) {
        // TODO
        return false;
    }

}
