package test.com.packages;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private List<File> files = new ArrayList<>();

    public void setFiles(List<File> files) {
        this.files = files;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_item_file, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = files.get(position);

        holder.nameTv.setText(file.getName());
    }


    @Override
    public int getItemCount() {
        return files.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTv;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name_tv);
        }

    }
}
