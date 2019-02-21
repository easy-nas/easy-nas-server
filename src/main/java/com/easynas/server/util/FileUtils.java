package com.easynas.server.util;

import com.easynas.server.model.Pair;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.easynas.server.util.CommandUtils.cp;
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
    private static boolean canSave(@NonNull final File[] needMoveFiles, @NonNull final List<File> toDirectories) {
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
     * 把 fromDirectory 中的文件， 随机复制到toDirectories中
     *
     * @param fromDirectory 复制源地址
     * @param toDirectories 复制目标目录们
     * @return 不可变map（复制之前的地址，复制后的地址）
     */
    public static Map<String, String> scatterCopy(@NonNull final File fromDirectory, @NonNull final List<File> toDirectories) {
        final var needCopyFiles = fromDirectory.listFiles();
        final var resultSize = Optional.ofNullable(needCopyFiles).map(t -> t.length).orElse(0);
        Map<String, String> result = new HashMap<>(resultSize);
        if (needCopyFiles == null || !canSave(needCopyFiles, toDirectories)) {
            return Map.copyOf(result);
        }
        final var directorySizeHead = toDirectories.stream().map(t -> new Pair<>(t.getFreeSpace(), t))
                .collect(toCollection(() -> new PriorityQueue<>((o1, o2) ->
                        o1.getKey().equals(o2.getKey())
                                ? o1.getValue().getAbsolutePath().compareTo(o2.getValue().getAbsolutePath())
                                : o1.getKey().compareTo(o2.getKey()))));
        for (final var file : needCopyFiles) {
            final var size = file.length();
            final var topDirectory = directorySizeHead.poll();
            assert topDirectory != null;
            final var fromPath = file.getAbsolutePath();
            final var toPath = topDirectory.getValue().getPath();
            try {
                cp(fromPath, toPath).waitFor();
                result.put(fromPath, toPath);
            } catch (IOException | InterruptedException e) {
                log.error("移动文件失败," + fromPath + "=>" + toPath, e);
                return Map.copyOf(result);
            }
            directorySizeHead.add(new Pair<>(topDirectory.getKey() - size, topDirectory.getValue()));
        }
        return Map.copyOf(result);
    }

    /**
     * 得到指定路径的文件（夹）大小
     *
     * @param path 绝对路径
     * @return size
     */
    public static long getPathSize(@NonNull final String path) {
        final var file = new File(path);
        return getPathSize(file);
    }

    public static long getPathSize(@NonNull final File file) {
        if (file.isFile()) {
            return file.length();
        }
        final var fileChildren = file.listFiles();
        assert fileChildren != null;
        return Arrays.stream(fileChildren).map(FileUtils::getPathSize).mapToLong(t -> t).sum();
    }

}
