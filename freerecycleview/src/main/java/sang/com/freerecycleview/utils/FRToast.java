package sang.com.freerecycleview.utils;

import android.content.Context;
import android.widget.Toast;

import sang.com.freerecycleview.error.NoInitException;


public class FRToast {
	private static Toast toast = null;
	private static Context context;

	public static void init(Context cnt){
		context=cnt;
	}
	public static void showTextToast(  String msg) {
		showTextToast(context,msg);
	}

	public static void showTextToast(int stringId) {
		showTextToast(context,context.getString(stringId));
	}


	public static void showTextToast(Context context, String msg) {

		if (context==null){
			throw new NoInitException("context is null,please call init(context) first");
		}

		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
	public static void showTextToast(Context context, int msg) {
		showTextToast(context,context.getString(msg));

	}


}