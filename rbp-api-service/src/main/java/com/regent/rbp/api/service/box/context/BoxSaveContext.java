package com.regent.rbp.api.service.box.context;

import com.regent.rbp.api.core.box.Box;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@Data
public class BoxSaveContext {
    public BoxSaveContext() {
    }

    List<Box> box = new ArrayList<>();

}
