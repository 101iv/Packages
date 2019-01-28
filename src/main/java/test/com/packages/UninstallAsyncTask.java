package test.com.packages;

import android.os.AsyncTask;

public class UninstallAsyncTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        //спроектирован таким образом, чтобы в него можно было передать несколько параметров.
        // Мы будем использовать только один
        String packageName = params[0];
        boolean result = RootHelper.uninstall(packageName);
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
