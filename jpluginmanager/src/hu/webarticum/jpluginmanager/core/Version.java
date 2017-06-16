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
        Pattern pattern = Pattern.compile("^[=v]*(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)(?:\\-([0-9A-Za-z\\-]+))?(?:\\+([0-9A-Za-z-]+))?)?)?$");
        Matcher matcher = pattern.matcher(versionString);
        if (matcher.find()) {
            String majorMatch = matcher.group(1);
            String minorMatch = matcher.group(2);
            String patchMatch = matcher.group(3);
            String preMatch = matcher.group(4);
            String buildMatch = matcher.group(5);
            this.major = majorMatch == null ? 0 : Integer.parseInt(majorMatch);
            this.minor = minorMatch == null ? 0 : Integer.parseInt(minorMatch);
            this.patch = patchMatch == null ? 0 : Integer.parseInt(patchMatch);
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
    public String toString() {
        String result = major + "." + minor + "." + patch;
        if (!pre.isEmpty()) {
            result += result + "-" + pre;
        }
        if (!build.isEmpty()) {
            result += result + "+" + build;
        }
        return result;
    }

    @Override
    public int compareTo(Version other) {
        if (major != other.major) {
            return Integer.compare(major, other.major);
        } else if (minor != other.minor) {
            return Integer.compare(minor, other.minor);
        } else if (patch != other.patch) {
            return Integer.compare(patch, other.patch);
        } else if (!pre.equals(other.pre)) {
            
            // TODO
            return 0;
            
        } else {
            return 0;
        }
    }

    public boolean matches(String versionMatcher) {
        versionMatcher = versionMatcher.replaceAll("\\s+", " ").trim();
        String[] subMatchers = versionMatcher.split(" \\|\\| ");
        for (String subMatcher: subMatchers) {
            String[] subSubMatchers = subMatcher.split(" ");
            boolean matches = true;
            for (String subSubMatcher: subSubMatchers) {
                if (!matchesSingle(subSubMatcher)) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }
    
    public boolean matchesSingle(String singleVersionMatcher) {
        if (singleVersionMatcher.matches("\\*(\\.\\*)+")) {
            return true;
        }
        
        {
            Pattern pattern = Pattern.compile("^(<|>|<=|>=|=?)v?((?:\\d+)(?:\\.(?:\\d+)(?:\\.(?:\\d+)(?:\\-[0-9A-Za-z\\-]+)?(?:\\+[0-9A-Za-z-]+)?)?)?)$");
            Matcher matcher = pattern.matcher(singleVersionMatcher);
            if (matcher.find()) {
                String operator = matcher.group(1);
                String cmpVersionString = matcher.group(2);
                Version cmpVersion = new Version(cmpVersionString);
                int cmp = compareTo(cmpVersion);
                if (operator.equals("<")) {
                    return cmp < 0;
                } else if (operator.equals(">")) {
                    return cmp > 0;
                } else if (operator.equals("<=")) {
                    return cmp <= 0;
                } else if (operator.equals(">=")) {
                    return cmp >= 0;
                } else {
                    // XXX check build...
                    return cmp == 0;
                }
            }
        }
        
        if (singleVersionMatcher.indexOf('*') >= 0) {
            String remain = singleVersionMatcher.replaceAll("^v", "");
            String majorMatcher = "";
            String minorMatcher = "";
            String patchMatcher = "";
            String preMatcher = "";
            String buildMatcher = "";
            
            int plusPos = singleVersionMatcher.indexOf('+');
            if (plusPos >= 0) {
                remain = singleVersionMatcher.substring(0, plusPos);
                buildMatcher = singleVersionMatcher.substring(plusPos + 1);
            }
            
            int minusPos = remain.indexOf('-');
            if (minusPos >= 0) {
                remain = singleVersionMatcher.substring(0, minusPos);
                preMatcher = singleVersionMatcher.substring(minusPos + 1);
            }
            
            String[] numericTokens = remain.split("\\.");
            if (numericTokens.length > 0) {
                majorMatcher = numericTokens[0];
            }
            if (numericTokens.length > 1) {
                minorMatcher = numericTokens[1];
            }
            if (numericTokens.length > 2) {
                patchMatcher = numericTokens[2];
            }
            
            if (majorMatcher.matches("\\d+")) {
                if (major != Integer.parseInt(majorMatcher)) {
                    return false;
                }
            } else if (!majorMatcher.matches("|\\*")) {
                return false;
            }
            
            if (minorMatcher.matches("\\d+")) {
                if (minor != Integer.parseInt(minorMatcher)) {
                    return false;
                }
            } else if (!minorMatcher.matches("|\\*")) {
                return false;
            }
            
            if (patchMatcher.matches("\\d+")) {
                if (patch != Integer.parseInt(patchMatcher)) {
                    return false;
                }
            } else if (!patchMatcher.matches("|\\*")) {
                return false;
            }
            
            if (!preMatcher.isEmpty()) {
                if (pre.isEmpty()) {
                    return false;
                }
                String prePattern = preMatcher.replaceAll("[\\.\\-]", "\\\\$0").replaceAll("\\*", ".*");
                if (!pre.matches(prePattern)) {
                    return false;
                }
            }

            if (!buildMatcher.isEmpty()) {
                if (build.isEmpty()) {
                    return false;
                }
                String buildPattern = buildMatcher.replaceAll("[\\.\\-]", "\\\\$0").replaceAll("\\*", ".*");
                if (!build.matches(buildPattern)) {
                    return false;
                }
            }
            
            return true;
        }
        
        return false;
    }
}
