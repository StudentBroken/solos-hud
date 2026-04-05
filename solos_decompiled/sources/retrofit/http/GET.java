package retrofit.http;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes53.dex */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@RestMethod(HttpRequest.METHOD_GET)
public @interface GET {
    String value();
}
