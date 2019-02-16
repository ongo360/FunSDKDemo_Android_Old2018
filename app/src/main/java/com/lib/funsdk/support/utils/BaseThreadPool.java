package com.lib.funsdk.support.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BaseThreadPool {

	private static ScheduledExecutorService fixedThreadPool;

	private static ExecutorService singleThreadExecutor;

	private static BaseThreadPool instance;

	private static int length = 2;

	private BaseThreadPool() {
	}

	public static BaseThreadPool getInstance() {
		if (null == instance) {
			instance = new BaseThreadPool();
		}
		if (null == fixedThreadPool) {
			fixedThreadPool = Executors.newScheduledThreadPool(length);
		}
		if (null == singleThreadExecutor) {
			singleThreadExecutor = Executors.newSingleThreadExecutor();
		}
		return instance;
	}

	/**
	 * 
	 * @param task
	 * @param s1
	 * @param s2
	 */
	public void doTask(Runnable task, long s1, long s2) {
		fixedThreadPool.scheduleAtFixedRate(task, s1, s2, TimeUnit.SECONDS);
	}

	public void doTaskBySinglePool(Thread task) {
		doTaskBySinglePool(task, Thread.NORM_PRIORITY);
	}

	/**
	 * 为单线程池添加线程，并给此线程添加优先级
	 * 
	 * @param task
	 *            线程
	 * @param priority
	 *            优先级，1-10之间
	 */
	public void doTaskBySinglePool(Thread task, int priority) {
		if (priority > Thread.MAX_PRIORITY || priority < Thread.MIN_PRIORITY) {
			priority = Thread.NORM_PRIORITY;
		}
		task.setPriority(priority);
		singleThreadExecutor.execute(task);
	}

}
