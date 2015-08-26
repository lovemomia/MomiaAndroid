package com.youxing.duola.app;

import com.youxing.common.app.MappingManager;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class DLMappingManager extends MappingManager {

    public DLMappingManager() {
        putPage("fillorder", new MappingPage("fillorder", true));
        putPage("personinfo", new MappingPage("personinfo", true));
        putPage("couponlist", new MappingPage("couponlist", true));
        putPage("orderdetail", new MappingPage("orderdetail", true));
        putPage("myorderlist", new MappingPage("myorderlist", true));
    }

}
