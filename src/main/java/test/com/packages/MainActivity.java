package test.com.packages;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /**свайп для списка */
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppsAdapter appsAdapter;
    private AppManager appManager;

    private static final int REQUEST_CODE_PICK_APK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        //основной макет, привязка класса к айди
        setContentView(R.layout.activity_main);

        //контейнер для списка привязываем к его айди из макета
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        //устанавливаем слушатель для этого контейнера
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);



        //обновляемый список привязываем к его айди из макета
        RecyclerView recyclerView = findViewById(R.id.apps_rv);

        //менеджер, отвечающий за вывод приложений
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //устанавливаем для обновляемого списка наш менеджер
        recyclerView.setLayoutManager(layoutManager);

        //устанавливаем для списка менедджер, который отвечает за порядок вывода
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //объявляем декоратор для разделения приложений в списке
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        //добавляем в список декоратор
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Класс, получающий список пакетов
        appManager = new AppManager(this);
        List<AppInfo> installedApps = appManager.getInstalledApps();

        //новый адаптер
        appsAdapter = new AppsAdapter();

        //создаем адаптер для обновляемого списка
        //AppsAdapter appsAdapter = new AppsAdapter();
        //и привязывае его к списку
        recyclerView.setAdapter(appsAdapter);

        //в адаптер добавляем список приложенией
        appsAdapter.setApps(installedApps);

        //сообщаем адаптеру, что данные изменились.
        // Если не уведомить его об изменении данных, то они попросту не отобразятся.
        appsAdapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        // добавляем ItemTouchHelper в качестве ItemDecoration (так же, как делали с разделителями)
        recyclerView.addItemDecoration(itemTouchHelper);
        //"прикрепляем" ItemTouchHelper к RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    //метод перезагрузка списка приложений
    private void reloadApps() {
        List<AppInfo> installedApps = appManager.getInstalledApps();
        appsAdapter.setApps(installedApps);
        appsAdapter.notifyDataSetChanged();
    }

    //контейнер для обновляемого списка. Обновляет, когда тянешь пальцем вниз и отпускаешь
    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            reloadApps();
            swipeRefreshLayout.setRefreshing(false);
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG,"click " + newText);
                appsAdapter.setQuery(newText.toLowerCase().trim());
                appsAdapter.notifyDataSetChanged();

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.install_item:
                startFilePickerActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //виджет тост, всплыв подсказка
    private void showToast() {

        Toast toast = Toast.makeText(this, "Hello", Toast.LENGTH_LONG);
        toast.show();

    }

    private void startFilePickerActivity() {
        Intent intent = new Intent(this, FilePickerActivity.class);

        //startActivity(intent);
        //не просто запускаем Activity, но и ждём какого-то результата от неё.
        startActivityForResult(intent, REQUEST_CODE_PICK_APK);

    }

    @Override
    //проверим, что ответ пришёл именно от нашей Activity, получим путь до файла и выведем его в лог
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_APK && resultCode == RESULT_OK) {
            String apkPath = data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH);
            Log.i(TAG, "APK: " + apkPath);

            startAppInstallation(apkPath);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //здесь будем запускать установку приложения
    private void startAppInstallation(String apkPath) {

        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        //проверяем версию Android, и если она больше либо равна N (7.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", new File(apkPath));
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            //на более ранних версиях мы вполне можем использовать старый способ создания URI
            uri = Uri.fromFile(new File(apkPath));
        }

        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Создаст новый процесс

        startActivity(installIntent);

    }

    private final ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {

        @Override
        //этот метод вызывается, когда пользователь начинает тянуть ячейку.
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //ItemTouchHelper.ACTION_STATE_IDLE сообщает, что действие запрещено, так как мы не позволяем перемещать ячейку.
            //ItemTouchHelper.END говорит о том, что мы разрешаем перемещение ячейки "в конец"
            // — слева направо на LTR локалях и справа налево на RTL локалях.
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.END);
        }

        @Override
        //вызывается, когда пользователь переместил ячейку. Вторым аргуметром передаётся
        // ViewHolder ячейки, которую переместили, а третьим — ViewHolder ячейки,
        // на которую произошло перетаскивание.
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        //вызывается, когда пользователь завершил жест "свайп". Первым аргументом передаётся
        // ViewHolder ячейки, которую свайпнули, а вторым — направление свайпа.
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            AppInfo appInfo = (AppInfo) viewHolder.itemView.getTag();

             startAppUninstallation(appInfo);
            }
        };

// версия без root
//    private void startAppUninstallation(AppInfo appInfo) {
//        //неявный Intent
//        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
//
//        intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
//        Log.i(TAG,"uninstall package:" + appInfo.getPackageName());
//        startActivity(intent);
//
//    }

    private void startAppUninstallation(AppInfo appInfo) {
        uninstallWithRoot(appInfo);
    }

    private void uninstallWithRoot(AppInfo appInfo) {
        UninstallAsyncTask uninstallAsyncTask = new UninstallAsyncTask();
        uninstallAsyncTask.execute(appInfo.getPackageName());
    }





    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

}
