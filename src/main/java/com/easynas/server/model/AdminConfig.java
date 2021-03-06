package com.easynas.server.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * config 文件的model
 *
 * 这个类无法把成员final 化
 *
 * @author liangyongrui
 */
@ToString
@Data
public class AdminConfig {
    private Map<String, String> generalInformationPath;
    private Map<String, List<String>> fileSavePaths;
}
