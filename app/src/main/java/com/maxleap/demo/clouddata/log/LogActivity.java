package com.maxleap.demo.clouddata.log;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.maxleap.ConsoleLogNode;
import com.maxleap.LogNodeDecorator;
import com.maxleap.MLLog;
import com.maxleap.demo.clouddata.R;
import com.maxleap.utils.FileHandle;
import com.maxleap.utils.FileHandles;

public abstract class LogActivity extends AppCompatActivity {

    private LogFragment logFragment;

    protected void initializeLogging() {
        logFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);

        FileHandle handle = FileHandles.sdcard(getPackageName() + ".log");
        FileLogNode fileLogNode = new FileLogNode(handle);
        LogNodeDecorator fileNodeDecorator = new LogNodeDecorator(fileLogNode);
        fileNodeDecorator.setEnabled(false);

        LogNodeDecorator consoleNodeDecorator = new LogNodeDecorator(new ConsoleLogNode());
        consoleNodeDecorator.setNext(fileNodeDecorator);

        LogNodeDecorator decorator = new LogNodeDecorator(consoleNodeDecorator);
        decorator.setNext(logFragment.getLogView());
        MLLog.setWrapper(decorator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                if (logFragment != null) {
                    logFragment.getLogView().setText("");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
