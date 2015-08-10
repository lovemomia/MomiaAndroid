package com.youxing.duola.app;

import com.youxing.common.app.MappingManager;
import com.youxing.common.app.YXApplication;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class DLApplication extends YXApplication {

    @Override
    protected MappingManager mappingManager() {
        return new DLMappingManager();
    }
}
