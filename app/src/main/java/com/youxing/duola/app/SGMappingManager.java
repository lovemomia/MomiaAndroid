package com.youxing.duola.app;

import com.youxing.common.app.MappingManager;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class SGMappingManager extends MappingManager {

    public SGMappingManager() {
        // 提交订单
        putPage("fillorder", new MappingPage("fillorder", true));
        // 个人信息
        putPage("personinfo", new MappingPage("personinfo", true));
        // 红包列表
        putPage("couponlist", new MappingPage("couponlist", true));
        // 订单详情
        putPage("orderdetail", new MappingPage("orderdetail", true));
        // 我的订单列表
        putPage("myorderlist", new MappingPage("myorderlist", true));
        // 出行人
        putPage("orderperson", new MappingPage("orderperson", true));
        // 更新（新增）出行人
        putPage("orderupdateperson", new MappingPage("orderupdateperson", true));
        // 我的收藏
        putPage("myfav", new MappingPage("myfav", true));

        // 预约课程
        putPage("bookingsubjectlist", new MappingPage("bookingsubjectlist", true));

        // 邀请好友
        putPage("share", new MappingPage("share", true));
    }

}
