package com.easynas.server.util;


import lombok.NonNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * @author liangyongrui
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * todo test
     * 判断是否可以把fromPath中的文件，移动到toPath中
     */
    public static boolean canMove(@NonNull String fromPath, @NonNull List<String> toPaths) {
        final var fromPathDirectory = new File(fromPath);
        if (fromPathDirectory.isFile()) {
            return false;
        }
        final var toPathDirectories = toPaths.stream().map(File::new).filter(File::isDirectory).collect(toList());
        if (toPathDirectories.size() == 0 || toPathDirectories.size() < toPaths.size()) {
            return false;
        }
        final var files = fromPathDirectory.listFiles();
        if (files == null) {
            return false;
        }
        final var filesSize = Arrays.stream(files).map(File::length).collect(toList());
        final var maxHeap = toPathDirectories.stream().map(File::getFreeSpace)
                .collect(toCollection(PriorityQueue::new));
        filesSize.sort(Long::compareTo);
        for (final var size : filesSize) {
            final var topSize = maxHeap.poll();
            if (topSize == null || size > topSize) {
                return false;
            }
            maxHeap.add(topSize - size);
        }
        return true;
    }

    /**
     * 得到指定路径的文件（夹）大小
     *
     * @param path 绝对路径
     * @return size
     */
    public static long getPathSize(@NonNull String path) {
        final var file = new File(path);
        return getPathSize(file);
    }

    public static long getPathSize(@NonNull File file) {
        if (file.isFile()) {
            return file.length();
        }
        final var fileChildren = file.listFiles();
        assert fileChildren != null;
        return Arrays.stream(fileChildren).map(FileUtils::getPathSize).mapToLong(t -> t).sum();
    }

}
