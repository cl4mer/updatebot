/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package io.jenkins.updatebot.support;

import io.fabric8.utils.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class FileHelper {
    public static boolean isFile(File file) {
        return file != null && file.isFile() && file.exists();
    }

    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory() && file.exists();
    }

    public static Object getRelativePathToCurrentDir(File dir) {
        File currentDir = new File(System.getProperty("user.dir", "."));
        try {
            String relativePath = Files.getRelativePath(currentDir, dir);
            if (relativePath.startsWith("/")) {
                return relativePath.substring(1);
            }
            return relativePath;
        } catch (IOException e) {
            return dir;
        }
    }

    /**
     * Returns true if a file matching the filter can be found in the given directory
     */
    public static boolean hasFile(File dir, FileFilter filter) {
        if (filter.accept(dir)) {
            return true;
        }
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (hasFile(file, filter)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Reads a {@link File} and returns the list of lines
     */
    public static List<String> readLines(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> answer = new ArrayList<String>();
        try {
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    answer.add(line);
                } else {
                    break;
                }
            }
        } finally {
            reader.close();
        }
        return answer;
    }

    /**
     * Writes the given lines to the {@link File}
     */
    public static void writeLines(File file, List<String> lines) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        try {
            for (String line : lines) {
                writer.println(line);
            }
        } finally {
            writer.close();
        }
    }

}
