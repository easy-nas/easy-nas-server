package com.easynas.server.model.admin.request;

import lombok.Data;

import java.util.List;

/**
 * @author liangyongrui
 */
@Data
public class PathListRequest {
    private List<String> path;
}
