package com.easynas.server.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * config 文件的model
 *
 * @author liangyongrui
 */
@ToString
@Data
public class ConfigModel {

    private Map<String, String> generalInformationPath;
    private Map<String, List<String>> fileSavePaths;


}
