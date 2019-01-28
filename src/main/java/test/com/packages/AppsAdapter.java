package test.com.packages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер
 */
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private List<AppInfo> apps = new ArrayList<>();
    private List<AppInfo> filteredApps = new ArrayList<>();

    private String query = "";

    public void setApps(List<AppInfo> apps) {
        this.apps = apps;

        filterApps();
    }


    private void filterApps() {
        filteredApps.clear();

        if (query.isEmpty()) {
            filteredApps.addAll(apps);
        } else {
            for (AppInfo app : apps) {
                if (app.getName().toLowerCase().contains(query)) {
                    filteredApps.add(app);
                }
            }
        }
    }

    public void setQuery(String query) {
        this.query = query;

        filterApps();
    }

    // В этом методе мы создаем новую ячейку
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.view_item_app, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // В этом методе мы привязываем данные к ячейке
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppInfo appInfo = filteredApps.get(position);

        holder.nameTv.setText(appInfo.getName());
        holder.versionTv.setText(appInfo.getVersionName());
        holder.iconIv.setImageDrawable(appInfo.getIcon());

        //тег для того чтобы можно было удалить, содерижт список инфо о приложении
        holder.itemView.setTag(appInfo);
    }

    // В этом методе мы возвращаем количество элементов списка
    @Override
    public int getItemCount() {
        return filteredApps.size();
    }

    /**
     * View holder
     * Хранит информацию о ячейке
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iconIv;
        private final TextView nameTv;
        private final TextView versionTv;

        public ViewHolder(View itemView) {
            super(itemView);

            iconIv = itemView.findViewById(R.id.icon_iv);
            nameTv = itemView.findViewById(R.id.name_tv);
            versionTv = itemView.findViewById(R.id.version_tv);
        }
    }

}