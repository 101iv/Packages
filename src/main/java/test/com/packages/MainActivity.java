package test.com.packages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /**свайп для списка */
    private SwipeRefreshLayout swipeRefreshLayout;

    private AppsAdapter appsAdapter;

    private AppManager appManager;

    private static final int TYPE_DIRECTORY = 0;
    private static final int TYPE_FILE = 1;


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

        //Класс, получающий список пакетов
        appManager = new AppManager(this);
        List<AppInfo> installedApps = appManager.getInstalledApps();

        //новый адаптер
        appsAdapter = new AppsAdapter();

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

        //создаем адаптер для обновляемого списка
        AppsAdapter appsAdapter = new AppsAdapter();
        //и привязывае его к списку
        recyclerView.setAdapter(appsAdapter);

        //в адаптер добавляем список приложенией
        appsAdapter.setApps(installedApps);

        //сообщаем адаптеру, что данные изменились.
        // Если не уведомить его об изменении данных, то они попросту не отобразятся.
        appsAdapter.notifyDataSetChanged();



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
        startActivity(intent);
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
