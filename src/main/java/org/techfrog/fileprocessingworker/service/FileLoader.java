package org.techfrog.fileprocessingworker.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLoader {

    public static String loadFile(String fileSource) throws IOException {
        String data;
        Stream<String> lines = null;
        Path path = Paths.get(fileSource);
        try {
            lines = Files.lines(path);
            data = lines.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw e;
        } finally {
            if (lines != null) {
                lines.close();
            }
        }
        return data;
    }
}
