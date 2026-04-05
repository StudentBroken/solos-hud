package retrofit;

import java.util.ArrayList;
import java.util.List;
import retrofit.RequestInterceptor;

/* JADX INFO: loaded from: classes53.dex */
final class RequestInterceptorTape implements RequestInterceptor.RequestFacade, RequestInterceptor {
    private final List<CommandWithParams> tape = new ArrayList();

    private enum Command {
        ADD_HEADER { // from class: retrofit.RequestInterceptorTape.Command.1
            @Override // retrofit.RequestInterceptorTape.Command
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addHeader(name, value);
            }
        },
        ADD_PATH_PARAM { // from class: retrofit.RequestInterceptorTape.Command.2
            @Override // retrofit.RequestInterceptorTape.Command
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addPathParam(name, value);
            }
        },
        ADD_ENCODED_PATH_PARAM { // from class: retrofit.RequestInterceptorTape.Command.3
            @Override // retrofit.RequestInterceptorTape.Command
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addEncodedPathParam(name, value);
            }
        },
        ADD_QUERY_PARAM { // from class: retrofit.RequestInterceptorTape.Command.4
            @Override // retrofit.RequestInterceptorTape.Command
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addQueryParam(name, value);
            }
        },
        ADD_ENCODED_QUERY_PARAM { // from class: retrofit.RequestInterceptorTape.Command.5
            @Override // retrofit.RequestInterceptorTape.Command
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addEncodedQueryParam(name, value);
            }
        };

        abstract void intercept(RequestInterceptor.RequestFacade requestFacade, String str, String str2);
    }

    RequestInterceptorTape() {
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addHeader(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_HEADER, name, value));
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addPathParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_PATH_PARAM, name, value));
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addEncodedPathParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_ENCODED_PATH_PARAM, name, value));
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addQueryParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_QUERY_PARAM, name, value));
    }

    @Override // retrofit.RequestInterceptor.RequestFacade
    public void addEncodedQueryParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_ENCODED_QUERY_PARAM, name, value));
    }

    @Override // retrofit.RequestInterceptor
    public void intercept(RequestInterceptor.RequestFacade request) {
        for (CommandWithParams cwp : this.tape) {
            cwp.command.intercept(request, cwp.name, cwp.value);
        }
    }

    private static final class CommandWithParams {
        final Command command;
        final String name;
        final String value;

        CommandWithParams(Command command, String name, String value) {
            this.command = command;
            this.name = name;
            this.value = value;
        }
    }
}
