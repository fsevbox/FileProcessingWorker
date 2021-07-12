package org.techfrog.fileprocessingworker.service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPSearcher {

    private static final String IPV4_PATTERN_STRING =
            "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private static final Pattern PATTERN = Pattern.compile(IPV4_PATTERN_STRING);

    public static Set<String> searchForIps(String loadedFile) {
        Set<String> result = new HashSet<>();
        Matcher matcher = PATTERN.matcher(loadedFile);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}
