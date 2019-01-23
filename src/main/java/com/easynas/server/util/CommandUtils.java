package com.easynas.server.util;

import com.google.common.io.CharStreams;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行linux命令
 *
 * @author liangyongrui
 */
public class CommandUtils {

    private static final int SHA256SUM_LENGTH = 64;

    private CommandUtils() {
    }

    /**
     * 获取路径所在的分区
     *
     * @param path 需要获取的路径
     * @return 分区
     * @throws IOException 可能是路径不存在
     */
    public static String getPathPartition(@NonNull String path) throws IOException {
        final var exec = Runtime.getRuntime().exec("df " + path);
        final var info = getProcessString(exec);
        final var infoSplit = info.split("\n");
        if (infoSplit.length <= 1) {
            throw new IOException("获取路径所在分区失败， " + info);
        }
        return infoSplit[1].split(" ")[0];
    }

    /**
     * 删除路径所指的文件
     *
     * @param path 路径
     * @return Process 如果需要等他删完,或者获取删除结果需要自行控制
     * @throws IOException 没测出什么时候有异常...
     */
    @CanIgnoreReturnValue
    public static Process rm(@NonNull String path) throws IOException {
        return Runtime.getRuntime().exec("rm " + path + " -rf");
    }

    /**
     * 复制文件到destination
     * 如果目的地址不存在，路径最后一个名称将是复制完的文件名称
     * 如果目的地址存在，则把需要复制的东西放在目的地址中
     *
     * @param source 文件地址
     * @param destination 目的地址
     * @return Process
     * @throws IOException destination 没有写权限
     */
    @CanIgnoreReturnValue
    public static Process cp(@NonNull String source, @NonNull String destination) throws IOException {
        final var directoryPath = destination.substring(0, destination.lastIndexOf("/"));
        final var fa = new File(directoryPath);
        if (!fa.exists() && !fa.mkdirs()) {
            throw new IOException("创建父目录失败");
        }
        return Runtime.getRuntime().exec("cp " + source + " " + destination + " -r");
    }

    /**
     * 软链接
     */
    @CanIgnoreReturnValue
    public static Process lnS(@NonNull String source, @NonNull String destination) throws IOException {
        return Runtime.getRuntime().exec("ln -sfn " + source + " " + destination);
    }

    /**
     * 先复制，复制完成后删除源数据
     *
     * @param source 源数据地址
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

    /**
     * 获取文件的sha256sum
     *
     * @param filePath 文件路径
     * @return sha256sum
     * @throws IOException 地址不存在，地址是目录
     */
    public static String sha256sum(@NonNull String filePath) throws IOException {
        final var exec = Runtime.getRuntime().exec("sha256sum " + filePath);
        final var info = getProcessString(exec);
        final var infoSplit = info.split(" ");
        if (infoSplit.length <= 1 || infoSplit[0].length() != SHA256SUM_LENGTH) {
            throw new IOException("获取sha256sum失败， " + info);
        }
        return infoSplit[0];
    }

    public static String getProcessString(Process exec) throws IOException {
        return CharStreams.toString(new InputStreamReader(exec.getInputStream()));
    }

}
