package com.amao.rpc.core.client.future;

import com.amao.rpc.core.data.Request;
import com.amao.rpc.core.data.Response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class RpcFuture implements Future<Response> {

    public static final int STATE_PENDING = 0;
    public static final int STATE_INVALID = 1;

    private int state = RpcFuture.STATE_INVALID;
    private Request request;
    private Response response;


    public RpcFuture(Request request) {
        this.request = request;
    }

    private Sync sync = new Sync();

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Response get() throws InterruptedException, ExecutionException {
        sync.acquire(2);
        return response;
    }

    @Override
    public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (sync.tryAcquireNanos(2, unit.toNanos(timeout))) {
            return response;
        } else {
            throw new RuntimeException("Timeout com.amao.exception. Request id: " + request.getMessageHeader().getSessionId());
        }
    }


    public  void done(Response response) {
        sync.release(2);
        this.response = response;
    }

    static class Sync extends AbstractQueuedSynchronizer {

        private static final int DONE = 1;
        private static final int PENDING = 0;


        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == DONE;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == PENDING) {
                return compareAndSetState(PENDING, DONE);
            }
            return false;
        }

        public boolean isDone() {
            return getState() == DONE;
        }

    }


}
