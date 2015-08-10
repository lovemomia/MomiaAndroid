package com.youxing.duola.app;

import com.youxing.common.app.MappingManager;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class DLMappingManager extends MappingManager {

    public DLMappingManager() {
        putPage("fillorder", new MappingPage("fillorder", true));
    }

}
