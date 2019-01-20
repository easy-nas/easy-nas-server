package com.easynas.server.util;

import com.google.common.io.CharStreams;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行linux命令
 *
 * @author liangyongrui
 */
public class CommandUtils {
    private CommandUtils() {
    }

    /**
     * 获取路径所在的分区
     *
     * @param path 需要获取的路径
     * @return 分区
     */
    public static String getPathPartition(@NonNull String path) throws IOException {
        final var exec = Runtime.getRuntime().exec("df " + path);
        final var info = CharStreams.toString(new InputStreamReader(exec.getInputStream()));
        final var infoSplit = info.split("\n");
        if (infoSplit.length <= 1) {
            throw new IOException("获取路径所在分区失败， " + info);
        }
        return infoSplit[1].split(" ")[0];
    }

    @CanIgnoreReturnValue
    public static Process rm(@NonNull String path) throws IOException {
        return Runtime.getRuntime().exec("rm " + path + " -rf");
    }

    @CanIgnoreReturnValue
    public static Process cp(@NonNull String source, @NonNull String destination) throws IOException {
        return Runtime.getRuntime().exec("cp " + source + " " + destination + " -r");
    }

    /**
     * 先复制，复制完成后删除源数据
     *
     * @param source      源数据地址
     * @param destination 目的地址
     * @return 删除process
     */
    @CanIgnoreReturnValue
    public static Process cpThenRm(@NonNull String source, @NonNull String destination)
            throws IOException, InterruptedException {
        final var process = cp(source, destination);
        process.waitFor();
        return rm(source);
    }

}
