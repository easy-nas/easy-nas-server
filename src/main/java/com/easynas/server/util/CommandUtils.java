package com.easynas.server.util;

import com.google.common.io.CharStreams;

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
    public static String getPathPartition(String path) throws IOException {
        Process exec = Runtime.getRuntime().exec("df " + path);
        String info = CharStreams.toString(new InputStreamReader(exec.getInputStream()));
        String[] infoSplit = info.split("\n");
        if (infoSplit.length <= 1) {
            throw new IOException("获取路径所在分区失败， " + info);
        }
        return infoSplit[1].split(" ")[0];
    }
}
