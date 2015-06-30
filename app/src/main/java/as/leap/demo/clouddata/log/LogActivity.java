package as.leap.demo.clouddata.log;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import as.leap.ConsoleLogNode;
import as.leap.LASLog;
import as.leap.LogNodeDecorator;
import as.leap.demo.clouddata.R;
import as.leap.utils.FileHandle;
import as.leap.utils.FileHandles;

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
        LASLog.setWrapper(decorator);
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
