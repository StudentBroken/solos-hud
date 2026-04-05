package bolts;

/* JADX INFO: loaded from: classes23.dex */
public class ExecutorException extends RuntimeException {
    public ExecutorException(Exception e) {
        super("An exception was thrown by an Executor", e);
    }
}
