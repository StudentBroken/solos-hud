package com.ua.sdk.internal;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Reference;
import com.ua.sdk.Request;
import com.ua.sdk.Resource;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CachePolicy;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.cache.NullDiskCache;
import com.ua.sdk.cache.NullMemCache;
import com.ua.sdk.concurrent.CreateRequest;
import com.ua.sdk.concurrent.DeleteRequest;
import com.ua.sdk.concurrent.EntityEventHandler;
import com.ua.sdk.concurrent.FetchRequest;
import com.ua.sdk.concurrent.SaveRequest;
import com.ua.sdk.concurrent.SynchronousRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public abstract class AbstractManager<T extends Resource> {
    protected final CacheSettings cacheSettings;
    protected final DiskCache<T> diskCache;
    protected final ExecutorService executor;
    protected final Cache memCache;
    protected final EntityService<T> service;

    protected AbstractManager(CacheSettings cacheSettings, Cache memCache, DiskCache<T> diskCache, EntityService<T> service, ExecutorService executor) {
        this.cacheSettings = (CacheSettings) Precondition.isNotNull(cacheSettings);
        if (diskCache == null) {
            this.diskCache = new NullDiskCache();
        } else {
            this.diskCache = diskCache;
        }
        if (memCache == null) {
            this.memCache = new NullMemCache();
        } else {
            this.memCache = memCache;
        }
        this.executor = executor;
        this.service = service;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public T create(T t) throws UaException {
        Precondition.isNotNull(t);
        long jPutForCreate = this.diskCache.putForCreate(t);
        T t2 = (T) onPostServiceCreate(this.service.create(t));
        this.diskCache.updateAfterCreate(jPutForCreate, t2);
        this.memCache.put(t2);
        return t2;
    }

    public Request create(final T entity, CreateCallback<T> callback) {
        final CreateRequest<T> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.internal.AbstractManager.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(AbstractManager.this.create(entity), null);
                } catch (UaException e) {
                    request.done(entity, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    protected T onPostServiceCreate(T createdEntity) throws UaException {
        return createdEntity;
    }

    public T fetch(Reference reference) throws UaException {
        return (T) fetch(reference, this.cacheSettings.getDefaultCachePolicy());
    }

    protected T fetch(Reference reference, CachePolicy cachePolicy) throws UaException {
        return (T) fetch(reference, cachePolicy, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x005b  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected T fetch(com.ua.sdk.Reference r9, com.ua.sdk.cache.CachePolicy r10, boolean r11) throws com.ua.sdk.UaException {
        /*
            r8 = this;
            r6 = 0
            if (r9 != 0) goto Lc
            com.ua.sdk.UaException r4 = new com.ua.sdk.UaException
            java.lang.String r5 = "ref can't be null"
            r4.<init>(r5)
            throw r4
        Lc:
            if (r10 != 0) goto L14
            com.ua.sdk.cache.CacheSettings r4 = r8.cacheSettings
            com.ua.sdk.cache.CachePolicy r10 = r4.getDefaultCachePolicy()
        L14:
            r3 = 0
            boolean r4 = r10.checkCacheFirst()
            if (r4 == 0) goto L6a
            if (r11 == 0) goto L41
            com.ua.sdk.cache.Cache r4 = r8.memCache
            long r0 = r4.getCacheAge(r9)
            boolean r4 = r10.ignoreAge()
            if (r4 != 0) goto L37
            int r4 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r4 < 0) goto L41
            com.ua.sdk.cache.CacheSettings r4 = r8.cacheSettings
            long r4 = r4.getMaxCacheAge()
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 > 0) goto L41
        L37:
            com.ua.sdk.cache.Cache r4 = r8.memCache
            com.ua.sdk.Resource r3 = r4.get(r9)
            if (r3 == 0) goto L41
            r4 = r3
        L40:
            return r4
        L41:
            com.ua.sdk.cache.DiskCache<T extends com.ua.sdk.Resource> r4 = r8.diskCache
            long r0 = r4.getCacheAge(r9)
            boolean r4 = r10.ignoreAge()
            if (r4 != 0) goto L5b
            int r4 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r4 < 0) goto L6a
            com.ua.sdk.cache.CacheSettings r4 = r8.cacheSettings
            long r4 = r4.getMaxCacheAge()
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 > 0) goto L6a
        L5b:
            com.ua.sdk.cache.DiskCache<T extends com.ua.sdk.Resource> r4 = r8.diskCache
            com.ua.sdk.Resource r3 = r4.get(r9)
            if (r3 == 0) goto L6a
            com.ua.sdk.cache.Cache r4 = r8.memCache
            r4.put(r3)
            r4 = r3
            goto L40
        L6a:
            boolean r4 = r10.checkNetwork()
            if (r4 == 0) goto L86
            com.ua.sdk.internal.EntityService<T extends com.ua.sdk.Resource> r4 = r8.service     // Catch: com.ua.sdk.UaException -> L88
            com.ua.sdk.Resource r3 = r4.fetch(r9)     // Catch: com.ua.sdk.UaException -> L88
            com.ua.sdk.Resource r3 = r8.onPostServiceFetch(r9, r3)     // Catch: com.ua.sdk.UaException -> L88
            if (r3 == 0) goto L86
            com.ua.sdk.cache.DiskCache<T extends com.ua.sdk.Resource> r4 = r8.diskCache     // Catch: com.ua.sdk.UaException -> L88
            r4.updateAfterFetch(r3)     // Catch: com.ua.sdk.UaException -> L88
            com.ua.sdk.cache.Cache r4 = r8.memCache     // Catch: com.ua.sdk.UaException -> L88
            r4.put(r3)     // Catch: com.ua.sdk.UaException -> L88
        L86:
            r4 = r3
            goto L40
        L88:
            r2 = move-exception
            boolean r4 = r10.checkNetworkFirst()
            if (r4 == 0) goto L9c
            boolean r4 = r10.checkCache()
            if (r4 == 0) goto L9c
            com.ua.sdk.cache.CachePolicy r4 = com.ua.sdk.cache.CachePolicy.CACHE_ONLY
            com.ua.sdk.Resource r4 = r8.fetch(r9, r4, r11)
            goto L40
        L9c:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ua.sdk.internal.AbstractManager.fetch(com.ua.sdk.Reference, com.ua.sdk.cache.CachePolicy, boolean):com.ua.sdk.Resource");
    }

    protected Request fetch(Reference ref, FetchCallback<T> callback) {
        return fetch(ref, this.cacheSettings.getDefaultCachePolicy(), callback);
    }

    protected Request fetch(final Reference ref, CachePolicy cachePolicy, FetchCallback<T> callback) {
        Resource resource;
        final CachePolicy policy = cachePolicy != null ? cachePolicy : this.cacheSettings.getDefaultCachePolicy();
        if (policy.checkCacheFirst()) {
            long ageInCache = this.memCache.getCacheAge(ref);
            if ((policy.ignoreAge() || (ageInCache >= 0 && ageInCache <= this.cacheSettings.getMaxCacheAge())) && (resource = this.memCache.get(ref)) != null) {
                EntityEventHandler.callOnFetched(resource, null, callback);
                return SynchronousRequest.INSTANCE;
            }
        }
        final FetchRequest<T> request = new FetchRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.internal.AbstractManager.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(AbstractManager.this.fetch(ref, policy, !policy.checkCacheFirst()), null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    protected T onPostServiceFetch(Reference ref, T entity) throws UaException {
        return entity;
    }

    protected EntityList<T> onPostServiceFetchPage(Reference ref, EntityList<T> list) throws UaException {
        return list;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public T update(T t) throws UaException {
        T t2 = null;
        Resource resource = this.diskCache.get(t.getRef());
        try {
            this.memCache.put(t);
            this.diskCache.putForSave(t);
            t2 = (T) onPostServiceSave(this.service.save(t));
            this.diskCache.updateAfterSave(t2);
            this.memCache.remove(t);
            this.memCache.put(t2);
            return t2;
        } catch (UaException e) {
            switch (e.getCode()) {
                default:
                    if (t2 == null) {
                        this.diskCache.updateAfterSave(resource);
                        this.memCache.remove(t);
                        this.memCache.put(resource);
                        break;
                    }
                case NOT_CONNECTED:
                    throw e;
            }
        }
    }

    protected T onPostServiceSave(T entity) throws UaException {
        return entity;
    }

    public Request update(final T entity, SaveCallback<T> callback) {
        final SaveRequest<T> request = new SaveRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.internal.AbstractManager.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(AbstractManager.this.update(entity), null);
                } catch (UaException e) {
                    request.done(entity, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    public <R extends Reference> R delete(R ref) throws UaException {
        this.memCache.remove(ref);
        this.diskCache.markForDelete(ref);
        this.service.delete(ref);
        this.diskCache.delete(ref);
        return ref;
    }

    public Request delete(final Reference ref, DeleteCallback callback) {
        final DeleteRequest request = new DeleteRequest(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.internal.AbstractManager.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    AbstractManager.this.delete(ref);
                    request.done(ref, null);
                } catch (UaException e) {
                    request.done(ref, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    public EntityList<T> fetchPage(EntityListRef<T> ref) throws UaException {
        return fetchPage(ref, this.cacheSettings.getDefaultCachePolicy());
    }

    protected EntityList<T> fetchPage(EntityListRef<T> ref, CachePolicy cachePolicy) throws UaException {
        return fetchPage((EntityListRef) ref, cachePolicy, true);
    }

    protected EntityList<T> fetchPage(EntityListRef<T> ref, CachePolicy cachePolicy, boolean includeMemCache) throws UaException {
        if (cachePolicy == null) {
            cachePolicy = this.cacheSettings.getDefaultCachePolicy();
        }
        EntityList<T> list = null;
        try {
            try {
                if (cachePolicy.checkCacheFirst()) {
                    if (includeMemCache) {
                        long ageInCache = this.memCache.getCacheAge(ref);
                        if ((cachePolicy.ignoreAge() || (ageInCache >= 0 && ageInCache <= this.cacheSettings.getMaxCacheAge())) && (list = (EntityList) this.memCache.get(ref)) != null) {
                            return list;
                        }
                    }
                    long ageInCache2 = this.diskCache.getCacheAge(ref);
                    if ((cachePolicy.ignoreAge() || (ageInCache2 >= 0 && ageInCache2 <= this.cacheSettings.getMaxCacheAge())) && (list = this.diskCache.getList(ref)) != null) {
                        this.memCache.put(list);
                        return list;
                    }
                }
                if (cachePolicy.checkNetwork()) {
                    try {
                        list = onPostServiceFetchPage(ref, this.service.fetchPage(ref));
                        if (list != null) {
                            if (list instanceof AbstractEntityList) {
                                boolean partial = ((AbstractEntityList) list).preparePartials(ref);
                                this.diskCache.updateAfterFetch(ref, list, partial);
                            } else {
                                this.diskCache.updateAfterFetch(ref, list, false);
                            }
                            this.memCache.put(list);
                        }
                    } catch (UaException e) {
                        if (cachePolicy.checkNetworkFirst() && cachePolicy.checkCache()) {
                            return fetchPage(ref, CachePolicy.CACHE_ONLY, includeMemCache);
                        }
                        throw e;
                    }
                }
                return list;
            } catch (Throwable t) {
                throw new UaException(t);
            }
        } catch (UaException e2) {
            throw e2;
        }
    }

    protected Request fetchPage(EntityListRef<T> ref, FetchCallback<EntityList<T>> callback) {
        return fetchPage(ref, this.cacheSettings.getDefaultCachePolicy(), callback);
    }

    protected Request fetchPage(final EntityListRef<T> ref, CachePolicy cachePolicy, FetchCallback<EntityList<T>> callback) {
        EntityList<T> list;
        final CachePolicy policy = cachePolicy != null ? cachePolicy : this.cacheSettings.getDefaultCachePolicy();
        if (policy.checkCacheFirst()) {
            long ageInCache = this.memCache.getCacheAge(ref);
            if ((policy.ignoreAge() || (ageInCache >= 0 && ageInCache <= this.cacheSettings.getMaxCacheAge())) && (list = (EntityList) this.memCache.get(ref)) != null) {
                EntityEventHandler.callOnFetched(list, null, callback);
                return SynchronousRequest.INSTANCE;
            }
        }
        final FetchRequest<EntityList<T>> request = new FetchRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.internal.AbstractManager.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    EntityList<T> list2 = AbstractManager.this.fetchPage(ref, policy, !policy.checkCacheFirst());
                    request.done(list2, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public T patch(T t, Reference reference) throws UaException {
        this.memCache.put(t);
        this.diskCache.putForSave(t);
        T t2 = (T) onPostServicePatch(this.service.patch(t, reference));
        this.diskCache.updateAfterSave(t2);
        this.memCache.remove(t);
        this.memCache.put(t2);
        return t2;
    }

    protected T onPostServicePatch(T entity) throws UaException {
        return entity;
    }

    public Request patch(final T entity, final Reference ref, CreateCallback<T> callback) {
        final CreateRequest<T> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.internal.AbstractManager.6
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(AbstractManager.this.patch(entity, ref), null);
                } catch (UaException e) {
                    request.done(entity, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }
}
