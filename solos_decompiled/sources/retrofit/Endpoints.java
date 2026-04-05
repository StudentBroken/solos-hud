package retrofit;

/* JADX INFO: loaded from: classes53.dex */
public final class Endpoints {
    private static final String DEFAULT_NAME = "default";

    private Endpoints() {
    }

    public static Endpoint newFixedEndpoint(String url) {
        return new FixedEndpoint(url, "default");
    }

    public static Endpoint newFixedEndpoint(String url, String name) {
        return new FixedEndpoint(url, name);
    }

    private static class FixedEndpoint implements Endpoint {
        private final String apiUrl;
        private final String name;

        FixedEndpoint(String apiUrl, String name) {
            this.apiUrl = apiUrl;
            this.name = name;
        }

        @Override // retrofit.Endpoint
        public String getUrl() {
            return this.apiUrl;
        }

        @Override // retrofit.Endpoint
        public String getName() {
            return this.name;
        }
    }
}
