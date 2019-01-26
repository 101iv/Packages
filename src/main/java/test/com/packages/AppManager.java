package test.com.packages;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, получающий список приложений
 */
public class AppManager {

    private final PackageManager packageManager;

    public AppManager(Context context) {
        packageManager = context.getPackageManager();
    }

    public List<AppInfo> getInstalledApps() {
        List<AppInfo> installedApps = new ArrayList<>();

        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo installedPackage : installedPackages) {
            AppInfo appInfo = new AppInfo(
                    installedPackage.packageName, // Имя пакета
                    installedPackage.versionCode, // Код версии
                    installedPackage.versionName, // Имя версии
                    installedPackage.applicationInfo.loadLabel(packageManager).toString(), // Имя приложения
                    installedPackage.applicationInfo.loadIcon(packageManager) // Иконка приложения
            );

            installedApps.add(appInfo);
        }

        return installedApps;
    }

}
