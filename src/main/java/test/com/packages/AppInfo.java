package test.com.packages;

import android.graphics.drawable.Drawable;

///import java.util.List;

/**
 * Информация о приложении
 */
public class AppInfo {

    private final String packageName;
    private final int versionCode;
    private final String versionName;
    private final String name;
    private final Drawable icon;


    public AppInfo(String packageName, int versionCode, String versionName, String name, Drawable icon) {
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.name = name;
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "packageName='" + packageName + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                '}';
    }

}
