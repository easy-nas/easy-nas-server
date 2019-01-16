package com.easynas.server.util;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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
    public static boolean canMove(String fromPath, List<String> toPaths) {
        //todo check 路径是否均为文件夹
        File[] files = new File(fromPath).listFiles();
        if (files == null) {
            return false;
        }
        List<Long> filesSize = Arrays.stream(files).map(File::length).collect(Collectors.toList());
        Queue<Long> maxHeap = toPaths.stream().map(t -> new File(t).getFreeSpace()).collect(Collectors.toCollection(PriorityQueue::new));
        if (maxHeap.isEmpty()) {
            return false;
        }
        filesSize.sort(Long::compareTo);
        for (Long size : filesSize) {
            Long topSize = maxHeap.poll();
            if (size > topSize) {
                return false;
            }
            topSize -= size;
            maxHeap.add(topSize);
        }
        return true;
    }

    /**
     * 得到指定路径的文件（夹）大小
     *
     * @param path 绝对路径
     * @return size
     */
    public static long getPathSize(String path) {
        File file = new File(path);
        return getPathSize(file);
    }

    public static long getPathSize(File file) {
        if (file.isFile()) {
            return file.length();
        }
        long sum = 0;
        for (File child : Objects.requireNonNull(file.listFiles())) {
            sum += getPathSize(child);
        }
        return sum;
    }

}
