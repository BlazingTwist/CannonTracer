package the_dark_jumper.cannontracer.util;

import java.util.Timer;
import java.util.TimerTask;

public abstract class LoopingIncrementer {
	private Timer timer = null;
	private boolean incrementLoopIsRunning = false;
	private boolean decrementLoopIsRunning = false;

	protected abstract void doIncrement();
	protected abstract void doDecrement();

	public void onIncrement(boolean pressed) {
		if (pressed) {
			decrementLoopIsRunning = false;
			if (incrementLoopIsRunning) {
				return;
			}
			doIncrement();
			incrementLoopIsRunning = true;
			startTimer(this::onIncrementLoop, 300);
		} else {
			incrementLoopIsRunning = false;
		}
	}

	private void onIncrementLoop() {
		if (!incrementLoopIsRunning) {
			return;
		}
		doIncrement();
		startTimer(this::onIncrementLoop, 50);
	}

	public void onDecrement(boolean pressed) {
		if (pressed) {
			incrementLoopIsRunning = false;
			if (decrementLoopIsRunning) {
				return;
			}
			doDecrement();
			decrementLoopIsRunning = true;
			startTimer(this::onDecrementLoop, 300);
		} else {
			decrementLoopIsRunning = false;
		}
	}

	private void onDecrementLoop() {
		if (!decrementLoopIsRunning) {
			return;
		}
		doDecrement();
		startTimer(this::onDecrementLoop, 50);
	}

	protected void startTimer(Runnable runnable, int delay) {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runnable.run();
			}
		}, delay);
	}
}
