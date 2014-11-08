package com.qihoo.health.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.qihoo.health.db.DBManager;
import com.qihoo.health.model.Message;
import com.qihoo.health.model.User;
import com.qihoo.health.user.Login;

import android.app.Application;
import android.os.Process;
import android.util.Log;

public class HealthController implements Runnable {

	private Application mApplication;
	private Thread mThread;
	private static HealthController inst = null;
	private BlockingQueue<Command> mCommands = new PriorityBlockingQueue<Command>();

	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	private boolean mBusy;

	static AtomicInteger sequencing = new AtomicInteger(0);

	static class Command implements Comparable<Command> {
		public Runnable runnable;

		public HealthListener listener;

		public String description;

		boolean isForeground;

		int sequence = sequencing.getAndIncrement();

		@Override
		public int compareTo(Command other) {
			if (other.isForeground && !isForeground) {
				return 1;
			} else if (!other.isForeground && isForeground) {
				return -1;
			} else {
				return (sequence - other.sequence);
			}
		}
	}

	private HealthController(Application application) {
		mApplication = application;
		mThread = new Thread(this);
		mThread.setName("NettoolsController");
		mThread.start();
	}

	public synchronized static HealthController getInstance(
			Application application) {
		if (inst == null) {
			inst = new HealthController(application);
		}
		return inst;
	}

	public boolean isBusy() {
		return mBusy;
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			String commandDescription = null;
			try {
				final Command command = mCommands.take();

				if (command != null) {
					commandDescription = command.description;

					mBusy = true;
				}
				command.runnable.run();
			} catch (Exception e) {
				Log.e("test", "Error running command '"
						+ commandDescription + "'", e);
			}

			mBusy = false;
		}
	}

	private void put(String description, HealthListener listener,
			Runnable runnable) {
		putCommand(mCommands, description, listener, runnable, true);
	}

	private void putBackground(String description, HealthListener listener,
			Runnable runnable) {
		putCommand(mCommands, description, listener, runnable, false);
	}

	private void putCommand(BlockingQueue<Command> queue, String description,
			HealthListener listener, Runnable runnable, boolean isForeground) {
		int retries = 10;
		Exception e = null;
		while (retries-- > 0) {
			try {
				Command command = new Command();
				command.listener = listener;
				command.runnable = runnable;
				command.description = description;
				command.isForeground = isForeground;
				queue.put(command);
				return;
			} catch (InterruptedException ie) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ne) {
				}
				e = ie;
			}
		}
		throw new Error(e);
	}
	
	public void registerPhone(final String phoneNum, final HealthListener listener) {
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				if (Login.getInstance().GetCode(phoneNum))
					listener.registerFinished();
				else 
					listener.registerFailed();
			}
		});
	}
	
	public void checkCode(final String phoneNum, final String code, final HealthListener listener) {
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				if (Login.getInstance().CheckCode(code, phoneNum))
					listener.checkCodeFinished();
				else 
					listener.checkCodeFailed();
			}
		});
	}
	
	public void saveMessage(final Message message) {
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				DBManager.getInstance(mApplication).saveMessage(message);
			}
		});
	}

	public void saveUser(final User user) {
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				DBManager.getInstance(mApplication).saveUser(user);
			}
		});
	}

}
