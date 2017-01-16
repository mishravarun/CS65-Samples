package edu.dartmouth.cs.notifydemo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;

public class NotifyService extends Service {
	
	final static String ACTION = "NotifyServiceAction";
	final static String STOP_SERVICE_BROADCAST_KEY="StopServiceBroadcastKey";
	final static int RQS_STOP_SERVICE = 1;
	
	NotifyServiceReceiver notifyServiceReceiver;
	


	private final String myBlog = "http://www.cs.dartmouth.edu/~campbell/cs65/cs65.html";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		notifyServiceReceiver = new NotifyServiceReceiver();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);
		
		// Send Notification
		Context context = getApplicationContext();
		String notificationTitle = "Demo of Notification!";
		String notificationText = "Course Website";
		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
		PendingIntent pendingIntent 
				= PendingIntent.getActivity(getBaseContext(), 
						0, myIntent, 
						Intent.FLAG_ACTIVITY_NEW_TASK);
		
		
		
		
		Notification notification = new Notification.Builder(this)
        .setContentTitle(notificationTitle)
        .setContentText(notificationText).setSmallIcon(R.drawable.icon)
        .setContentIntent(pendingIntent).build();
	    NotificationManager notificationManager = 
	    		  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification.flags = notification.flags
				| Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notificationManager.notify(0, notification); 
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(notifyServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class NotifyServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int rqs = arg1.getIntExtra(STOP_SERVICE_BROADCAST_KEY, 0);
			
			if (rqs == RQS_STOP_SERVICE){
				stopSelf();
				((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
				.cancelAll();
			}
		}
	}
	
}
