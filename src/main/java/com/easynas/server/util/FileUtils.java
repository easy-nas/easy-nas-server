package com.easynas.server.util;

import com.easynas.server.model.Pair;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import static com.easynas.server.util.CommandUtils.cpThenRm;
import static java.util.stream.Collectors.toCollection;

/**
 * @author liangyongrui
 */
@Slf4j
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 判断是否可以把 fromDirectory 中的文件，移动到 toDirectories 中
     */
    private static boolean canMove(@NonNull File[] needMoveFiles, @NonNull List<File> toDirectories) {
        final var checkCount = toDirectories.stream().filter(File::isDirectory).count();
        if (toDirectories.size() == 0 || checkCount != toDirectories.size()) {
            return false;
        }
        final var maxHeap = toDirectories.stream().map(File::getFreeSpace)
                .collect(toCollection(PriorityQueue::new));
        for (final var file : needMoveFiles) {
            final var size = file.length();
            final var topSize = maxHeap.poll();
            if (topSize == null || size > topSize) {
                return false;
            }
            maxHeap.add(topSize - size);
        }
        return true;
    }

    /**
     * 把 fromDirectory 中的文件，移动到 toDirectories 中
     *
     * @return 成功返回true
     */
    public static boolean scatterMove(@NonNull File fromDirectory, @NonNull List<File> toDirectories) {
        final var needMoveFiles = fromDirectory.listFiles();
        if (needMoveFiles == null) {
            return false;
        }
        if (!canMove(needMoveFiles, toDirectories)) {
            return false;
        }
        final var directorySizeHead = toDirectories.stream().map(t -> new Pair<>(t.getFreeSpace(), t))
                .collect(toCollection(() -> new PriorityQueue<>((o1, o2) ->
                        o1.getKey().equals(o2.getKey())
                                ? o1.getValue().getAbsolutePath().compareTo(o2.getValue().getAbsolutePath())
                                : o1.getKey().compareTo(o2.getKey()))));
        for (final var file : needMoveFiles) {
            final var size = file.length();
            final var topDirectory = directorySizeHead.poll();
            assert topDirectory != null;
            final var fromPath = file.getAbsolutePath();
            final var toPath = topDirectory.getValue().getPath();
            try {
                cpThenRm(fromPath, toPath);
            } catch (IOException | InterruptedException e) {
                log.error("移动文件失败," + fromPath + "=>" + toPath, e);
                return false;
            }
            directorySizeHead.add(new Pair<>(topDirectory.getKey() - size, topDirectory.getValue()));
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
