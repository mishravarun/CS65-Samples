package edu.dartmouth.cs.binddemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
	private NotificationManager mNotificationManager;
	private Timer mTimer = new Timer();
	private int counter = 0, incrementBy = 1;
	private static boolean isRunning = false;

	private List<Messenger> mClients = new ArrayList<Messenger>(); // Keeps
																	// track of
																	// all
																	// current
																	// registered
																	// clients.
	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_INT_VALUE = 3;
	public static final int MSG_SET_STRING_VALUE = 4;

	// Reference to a Handler, which others can use to send messages to it. This
	// allows for the implementation of message-based communication across
	// processes, by creating a Messenger pointing to a Handler in one process,
	// and handing that Messenger to another process.

	private final Messenger mMessenger = new Messenger(
			new IncomingMessageHandler()); // Target we publish for clients to
											// send messages to IncomingHandler.

	private static final String TAG = "CS65";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "S:onCreate(): Service Started.");
		showNotification();
		mTimer.scheduleAtFixedRate(new MyTask(), 0, 5000L);
		isRunning = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "S:onStartCommand(): Received start id " + startId + ": "
				+ intent);
		return START_STICKY; // Run until explicitly stopped.
	}

	/**
	 * Display a notification in the notification bar.
	 */
	private void showNotification() {

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);
		Notification notification = new Notification.Builder(this)
				.setContentTitle(this.getString(R.string.service_label))
				.setContentText(
						getResources().getString(R.string.service_started))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(contentIntent).build();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification.flags = notification.flags
				| Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager.notify(0, notification);

	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "S:onBind() - return mMessenger.getBinder()");

		// getBinder()
		// Return the IBinder that this Messenger is using to communicate with
		// its associated Handler; that is, IncomingMessageHandler().
		
		return mMessenger.getBinder();
	}

	/**
	 * Send the data to all registered clients.
	 * 
	 * @param intvaluetosend
	 *            The value to send.
	 */
	private void sendMessageToUI(int intvaluetosend) {
		Log.d(TAG, "S:sendMessageToUI");
		Iterator<Messenger> messengerIterator = mClients.iterator();
		while (messengerIterator.hasNext()) {
			Messenger messenger = messengerIterator.next();
			try {
				// Send data as an Integer
				Log.d(TAG, "S:TX MSG_SET_INT_VALUE");
				messenger.send(Message.obtain(null, MSG_SET_INT_VALUE,
						intvaluetosend, 0));

				// Send data as a String
				Bundle bundle = new Bundle();
				bundle.putString("str1", "ab" + intvaluetosend + "cd");
				Message msg = Message.obtain(null, MSG_SET_STRING_VALUE);
				msg.setData(bundle);
				Log.d(TAG, "S:TX MSG_SET_STRING_VALUE");
				messenger.send(msg);

			} catch (RemoteException e) {
				// The client is dead. Remove it from the list.
				mClients.remove(messenger);
			}
		}
	}

	public static boolean isRunning() {
		return isRunning;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "S:onDestroy():Service Stopped");
		super.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
		}
		counter = 0;
		mNotificationManager.cancelAll(); // Cancel the persistent notification.
		isRunning = false;
	}

	// ////////////////////////////////////////
	// Nested classes
	// ///////////////////////////////////////

	/**
	 * The task to run...
	 */
	private class MyTask extends TimerTask {
		@Override
		public void run() {
			Log.d(TAG, "T:MyTask():Timer doing work." + counter);
			try {
				counter += incrementBy;
				sendMessageToUI(counter);

			} catch (Throwable t) { // you should always ultimately catch all
									// exceptions in timer tasks.
				Log.e("TimerTick", "Timer Tick Failed.", t);
			}
		}
	}

	/**
	 * Handle incoming messages from MainActivity
	 */
	private class IncomingMessageHandler extends Handler { // Handler of
															// incoming messages
															// from clients.
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "S:handleMessage: " + msg.what);
			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.add(msg.replyTo) ");
				mClients.add(msg.replyTo);
				break;
			case MSG_UNREGISTER_CLIENT:
				Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.remove(msg.replyTo) ");
				mClients.remove(msg.replyTo);
				break;
			case MSG_SET_INT_VALUE:
				incrementBy = msg.arg1;
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
}