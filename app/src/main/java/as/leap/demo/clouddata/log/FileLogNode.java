package as.leap.demo.clouddata.log;

import as.leap.LogNode;
import as.leap.utils.FileHandle;

/**
 * Created by mrseasons on 2015/05/15.
 */
public class FileLogNode implements LogNode {

    private FileHandle handle;

    public FileLogNode(FileHandle handle) {
        this.handle = handle;
    }

    @Override
    public void println(int priority, String tag, String message, Throwable throwable) {
        String priorityStr = null;

        // For the purposes of this View, we want to print the priority as readable text.
        switch (priority) {
            case android.util.Log.VERBOSE:
                priorityStr = "VERBOSE";
                break;
            case android.util.Log.DEBUG:
                priorityStr = "DEBUG";
                break;
            case android.util.Log.INFO:
                priorityStr = "INFO";
                break;
            case android.util.Log.WARN:
                priorityStr = "WARN";
                break;
            case android.util.Log.ERROR:
                priorityStr = "ERROR";
                break;
            case android.util.Log.ASSERT:
                priorityStr = "ASSERT";
                break;
            default:
                break;
        }

        // Handily, the Log class has a facility for converting a stack trace into a usable string.
        String exceptionStr = null;
        if (throwable != null) {
            exceptionStr = android.util.Log.getStackTraceString(throwable);
        }

        // Take the priority, tag, message, and exception, and concatenate as necessary
        // into one usable line of text.
        final StringBuilder outputBuilder = new StringBuilder();

        String delimiter = "\t";
        appendIfNotNull(outputBuilder, priorityStr, delimiter);
        appendIfNotNull(outputBuilder, tag, delimiter);
        appendIfNotNull(outputBuilder, message, delimiter);
        appendIfNotNull(outputBuilder, exceptionStr, delimiter);

        // In case this was originally called from an AsyncTask or some other off-UI thread,
        // make sure the update occurs within the UI thread.
        appendToLog(outputBuilder.toString());
    }

    private StringBuilder appendIfNotNull(StringBuilder source, String addStr, String delimiter) {
        if (addStr != null) {
            if (addStr.length() == 0) {
                delimiter = "";
            }

            return source.append(addStr).append(delimiter);
        }
        return source;
    }

    public void appendToLog(String s) {
        handle.tryWriteString("\n" + s, true);
    }
}
