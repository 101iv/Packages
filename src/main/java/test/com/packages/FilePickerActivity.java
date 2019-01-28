package test.com.packages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class FilePickerActivity  extends AppCompatActivity {
    private static final String TAG = "FilePickerActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FileManager fileManager;
    private FilesAdapter filesAdapter;
    public static final String EXTRA_FILE_PATH = "file_path";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        RecyclerView recyclerView = findViewById(R.id.files_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filesAdapter = new FilesAdapter();
        recyclerView.setAdapter(filesAdapter);

        initFileManager();
    }

    private void initFileManager() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Разрешение предоставлено
            fileManager = new FileManager(this);
            updateFileList();
        } else {
            requestPermissions();
        }
    }

    //проверим, есть ли у нас разрешение на чтение файлов
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE
        );
    }

    @Override
    //обработаем событие предоставления разрешения
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //мы должны проверить, предоставил ли пользователь разрешение
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted!");
                initFileManager();
            } else {
                Log.i(TAG, "Permission denied");
                requestPermissions(); // Запрашиваем ещё раз
            }
        }
    }




    //будет обновлять список файлов и передавать его в адаптер
    private void updateFileList() {
        List<File> files = fileManager.getFiles();

        filesAdapter.setFiles(files);
        filesAdapter.notifyDataSetChanged();
    }


    private final FilesAdapter.OnFileClickListener onFileClickListener = new FilesAdapter.OnFileClickListener() {
        @Override
        public void onFileClick(File file) {
            if (file.isDirectory()) {
                //переходим в директорию
                fileManager.navigateTo(file);
                // и обновляем список файлов в RecyclerView
                updateFileList();
            } else {
                //проверить расширение файла
                if (file.getName().endsWith(".apk")) {
                    // создаём Intent, засовываем в него путь до файла
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_FILE_PATH, file.getAbsolutePath());
                    // сообщаем, что все прошло хорошо, и передаём созданный Intent.
                    setResult(RESULT_OK, intent);
                    //завершаем Activity
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //подписываться мы должны после выполнения метода суперкласса
        filesAdapter.setOnFileClickListener(onFileClickListener);
    }
    @Override
    protected void onStop() {
        filesAdapter.setOnFileClickListener(null);
        //отписываться — до
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //вызываем метод суперкласса только в том случае,
        // если fileManager ещё не существует (например, если пользователь не предоставил разрешение)
        if (fileManager != null && fileManager.navigateUp()) {
            updateFileList();
        } else {
            super.onBackPressed();
        }
    }


}

