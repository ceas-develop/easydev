package com.ceas.develop.easydev.worker;

import com.ceas.develop.easydev.task.Task;

import java.util.Objects;

public class Worker<Params, Result> {
    private final Work<Params, Result> work;
    private WorkerTask workerTask;
    private Before<Params> before;
    private After<Result> after;

    protected Worker(Work<Params, Result> work) {
        this.work = Objects.requireNonNull(work, "work cannot be null");
        this.before = params -> params;
        this.after = (result, exception) -> {
        };
    }

    public static <Params, Result> Worker<Params, Result> get(Work<Params, Result> work) {
        return new Worker<>(work);
    }

    public Worker<Params, Result> after(After<Result> after) {
        if (after != null) this.after = after;
        return this;
    }

    public Worker<Params, Result> before(Before<Params> before) {
        if (before != null) this.before = before;
        return this;
    }

    public boolean isWorking() {
        return workerTask != null && (workerTask.isRunning() && !workerTask.isTaskDestroyed());
    }


    public void stop() {
        if (isWorking()) {
            workerTask.destroyTask();
            workerTask = null;
        }
    }

    public final Feedback<Result> execute(Params... params) {
        if (workerTask == null) {
            workerTask = new WorkerTask(new Feedback<>());
        } else if (isWorking()) {
            return null;
        }
        workerTask.execute(before.value(params));
        return workerTask.feedback;
    }

    private class WorkerTask extends Task<Params, Integer, Result, Exception> {

        private final Feedback<Result> feedback;


        private WorkerTask(Feedback<Result> feedback) {
            this.feedback = feedback;
        }

        @Override
        protected Result doTaskInBackground(Params... params) throws Exception {
            return work.working(
                    new Work.Manager() {
                        private int progressMax;

                        @Override
                        public void setMaxProgress(int max) {
                            this.progressMax = max;
                        }

                        @Override
                        public void notifyProgress(int current) {
                            postProgressTask(current, progressMax);
                        }

                        @Override
                        public void stop() {
                            destroyTask();
                        }
                    },
                    params
            );
        }

        @Override
        protected void onResultTask(Result result) {
            feedback.notify(result, isTaskDestroyed());
            after.value(result, null);
        }

        @Override
        protected void onPostProgressTask(Integer... post) {
            feedback.notify(post[0], post[1]);
        }

        @Override
        protected void onDestroyTask(Result result) {
            onResultTask(result);
        }

        @Override
        protected void onFailureTask(Exception exception) {
            feedback.notify(exception);
            after.value(null, exception);
        }
    }


    @FunctionalInterface
    public interface Work<Params, Result> {
        Result working(Manager manager, Params... params) throws Exception;

        interface Manager {
            void setMaxProgress(int max);

            void notifyProgress(int current);

            void stop();
        }
    }

    @FunctionalInterface
    public interface Before<Params> {
        Params[] value(Params... params);
    }

    @FunctionalInterface
    public interface After<Result> {
        void value(Result result, Exception e);
    }


    public static class Feedback<Value> {
        private Success<Value> success;
        private Progress progress;
        private Failure failure;
        private boolean progressSupport;

        private void notify(Value value, boolean stopped) {
            if (success != null) {
                success.value(value, stopped);
            }
        }

        private void notify(int current, int max) {
            if (progress != null) {
                progress.value(current, max);
                progressSupport = true;
            }
        }

        private void notify(Exception exception) {
            if (failure != null) {
                failure.value(exception);
            }
        }

        public boolean hasProgressSupport() {
            return progressSupport;
        }

        public Feedback<Value> success(Success<Value> success) {
            this.success = success;
            return this;
        }

        public Feedback<Value> progress(Progress progress) {
            this.progress = progress;
            return this;
        }

        public void failure(Failure failure) {
            this.failure = failure;
        }

        @FunctionalInterface
        public interface Success<Value> {
            void value(Value value, boolean stopped);
        }

        @FunctionalInterface
        public interface Progress {
            void value(int current, int max);
        }

        @FunctionalInterface
        public interface Failure {
            void value(Exception e);
        }

    }

}