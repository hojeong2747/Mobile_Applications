package mobile.example.alarmtest;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class MainActivity extends Activity {
	
	PendingIntent sender = null;
	AlarmManager alarmManager = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_test);
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		createNotificationChannel(); // onCreate()에서 알림 채널 생성
	}


	public void mOnClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
			case R.id.onetime:
				// 알람 예약에 의해 호출될 BR 지정  - MyBroadcastReceiver
				intent = new Intent(this, MyBroadcastReceiver.class);
				sender = PendingIntent.getBroadcast(this, 0, intent, 0);

				// 알람 동작 시간 지정. 3초후
				Calendar calendar = Calendar.getInstance(); // Calendar 객체 생성
				calendar.setTimeInMillis(System.currentTimeMillis()); // 절대 시간을 Millisecond 단위로 반환
				calendar.add(Calendar.SECOND, 3); // 현재 시간에서 3초 후 알람 동작

				// 알람 등록 - set
				alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);
				// 현재 시간보다 3초 뒤에 알람 동작 (알람이 울리면 MyBroadcastReceiver가 동작)
				break;
			case R.id.repeat:
				// 알람 예약에 의해 호출될 BR 지정 - RepeatReceiver
				intent = new Intent(this, RepeatReceiver.class);
				sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				// 기존 알람 수정 - REQ_CODE, intent 동일한 pendingIntent 생성

				// 알람 등록 - setRepeating (반복주기만 추가)
				alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
						SystemClock.elapsedRealtime() + 3000, 10000 * 6, sender);
				// 60초당 한번 알람 등록 --> 최소 1분 정도로 반복을 설정하여야 함

//       정확도가 떨어지는 반복 알람 설정 시
//       alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 3000,
//             AlarmManager.INTERVAL_FIFTEEN_MINUTES, sender);
				break;
			case R.id.stop:
				intent = new Intent(this, RepeatReceiver.class);
				sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				// 기존 알람 취소 - REQ_CODE, intent 동일한 pendingIntent 생성
				if (sender != null) alarmManager.cancel(sender);
				break;
		}

	}

	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
			CharSequence name = getString(R.string.channel_name);       // strings.xml 에 채널명 기록
			String description = getString(R.string.channel_description);       // strings.xml에 채널 설명 기록
			int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
			NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);    // CHANNEL_ID 지정
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);  // 채널 생성
			notificationManager.createNotificationChannel(channel);
		}
	}


//	일정 시간 간격으로 작업을 반복하고자 할 때는 Handler 를 사용할 수도 있음
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.arg1 != 5) {
//                Toast.makeText(AlarmTestActivity.this, "Alarm!!! " + msg.arg1, Toast.LENGTH_SHORT).show();
//                Message newMsg = handler.obtainMessage();
//                newMsg.arg1 = msg.arg1 + 1;
//                this.sendMessageDelayed(newMsg, 3000);
//            }
//        }
//    };

}

