package com.youxing.common.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Enviroment {

	private static String versionName;

	public static String versionName() {
		if (versionName == null) {
			try {
				PackageInfo pinfo = YXApplication
						.instance()
						.getPackageManager()
						.getPackageInfo(
								YXApplication.instance().getPackageName(),
								PackageManager.GET_CONFIGURATIONS);
				versionName = pinfo.versionName;
			} catch (NameNotFoundException e) {
			}
		}
		return versionName;
	}

}
