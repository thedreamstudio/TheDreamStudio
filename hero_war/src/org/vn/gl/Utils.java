package org.vn.gl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.vn.constant.Constants;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class Utils {

	public static Random RANDOM = new Random();

	public static Paint PAINT = new Paint();

	public static Canvas CANVAS = new Canvas();

	public static String convertStreamToString(InputStream is)
			throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public static void writeToFile(String fileName, InputStream iStream)
			throws IOException {
		writeToFile(fileName, iStream, true);
	}

	/**
	 * Writes InputStream to a given <code>fileName<code>.
	 * And, if directory for this file does not exists,
	 * if createDir is true, creates it, otherwice throws OMDIOexception.
	 * 
	 * @param fileName
	 *            - filename save to.
	 * @param iStream
	 *            - InputStream with data to read from.
	 * @param createDir
	 *            (false by default)
	 * @throws IOException
	 *             in case of any error.
	 * 
	 */
	public static void writeToFile(String fileName, InputStream iStream,
			boolean createDir) throws IOException {
		String me = "FileUtils.WriteToFile";
		if (fileName == null) {
			throw new IOException(me + ": filename is null");
		}
		if (iStream == null) {
			throw new IOException(me + ": InputStream is null");
		}

		File theFile = new File(fileName);

		// Check if a file exists.
		if (theFile.exists()) {
			String msg = theFile.isDirectory() ? "directory" : (!theFile
					.canWrite() ? "not writable" : null);
			if (msg != null) {
				throw new IOException(me + ": file '" + fileName + "' is "
						+ msg);
			}
		}

		// Create directory for the file, if requested.
		if (createDir && theFile.getParentFile() != null) {
			theFile.getParentFile().mkdirs();
			if (theFile.exists()) {
				theFile.delete();
				theFile.createNewFile();
			}
		}

		// Save InputStream to the file.
		BufferedOutputStream fOut = null;
		try {
			fOut = new BufferedOutputStream(new FileOutputStream(theFile));
			byte[] buffer = new byte[32 * 1024];
			int bytesRead = 0;
			while ((bytesRead = iStream.read(buffer)) != -1) {
				fOut.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			throw new IOException(me + " failed, got: " + e.toString());
		} finally {
			close(iStream, fOut);
		}
	}

	public static InputStream getImageFromNetwork(String url) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity.getContentLength() > 0) {
					BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
							entity);

					return bufferedHttpEntity.getContent();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getMd5Digest(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			return number.toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isSDCardPresent() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static void showDialogWhenLostConnection(final Context context,
			List<RunningTaskInfo> runningTaskList, String currentClass) {

		String packageName = context.getPackageName();
	}

	/** Decode a bitmap from resource */
	public static Bitmap decodeRawResource(Resources resources, int resourceID) {
		InputStream is = resources.openRawResource(resourceID);
		Bitmap temp = null;
		Bitmap bitmap = null;
		try {
			temp = BitmapFactory.decodeStream(is);

			if (temp != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				bitmap = temp.copy(Config.ARGB_8888, true);

				temp.recycle();
				temp = null;
			}
		} catch (OutOfMemoryError error) {
			DebugLog.e("Utils", "OutOfMemoryError");
			temp.recycle();
			temp = null;
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
			System.gc();

			temp = BitmapFactory.decodeStream(is);

			if (temp != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bitmap = temp.copy(Config.ARGB_8888, true);

				temp.recycle();
				temp = null;
			}
		}
		return bitmap;
	}

	/**
	 * gGet the ids list of views inside a view which may cover on the cordinate
	 * 
	 */
	public static ArrayList<Integer> getClickedViewIds(View mainView, int posX,
			int posY) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		if (mainView instanceof ViewGroup) {
			// get all the children in the view hierarchy
			ArrayList<View> allChildren = new ArrayList<View>();
			allChildren.add(mainView);
			for (int i = 0; i < allChildren.size(); i++) {
				if (allChildren.get(i) instanceof ViewGroup) {
					ViewGroup viewGroup = (ViewGroup) allChildren.get(i);
					for (int j = viewGroup.getChildCount() - 1; j >= 0; j--) {
						allChildren.add(viewGroup.getChildAt(j));
					}
				}
			}

			for (int i = allChildren.size() - 1; i >= 0; i--) {
				View view = allChildren.get(i);
				// check if the click position is inside a child view
				// or not
				if (view.getLeft() <= posX && view.getRight() >= posX
						&& view.getTop() <= posY && view.getBottom() >= posY
						&& view.getId() >= 0) {
					ids.add(view.getId());
				}
			}
		} else {
			if (mainView.getId() >= 0)
				ids.add(mainView.getId());
		}

		return ids;
	}

	/**
	 * @return string la so them dau cham
	 */
	public static String formatNumber(long n) {

		String moneyString = "";
		String temp = "" + n;
		while (temp.length() > 3) {
			if (moneyString.length() == 0) {
				moneyString = temp.substring(temp.length() - 3, temp.length());
			} else {
				moneyString = temp.substring(temp.length() - 3, temp.length())
						+ "." + moneyString;
			}
			temp = temp.substring(0, temp.length() - 3);
		}
		if (moneyString.length() == 0) {
			return temp;
		}
		moneyString = temp + "." + moneyString;
		return moneyString;
	}

	/**
	 * Checking the status of network
	 * 
	 * @param context
	 * @return is true if the device is connected. Otherwise, is false
	 */
	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Check for connection
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSdPresent() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static void clearData(SharedPreferences mSettingsPreferences,
			String[] deleteKeys) {
		Editor editor = mSettingsPreferences.edit();
		for (int i = 0; i < deleteKeys.length; i++) {
			editor.remove(deleteKeys[i]);
		}

		editor.commit();
	}

	/**
	 * Closes InputStream and/or OutputStream. It makes sure that both streams
	 * tried to be closed, even first throws an exception.
	 * 
	 * @throw IOException if stream (is not null and) cannot be closed.
	 * 
	 */
	protected static void close(InputStream iStream, OutputStream oStream)
			throws IOException {
		try {
			if (iStream != null) {
				iStream.close();
			}
		} finally {
			if (oStream != null) {
				oStream.close();
			}
		}
	}

	public static boolean checkFileExistent(String path) {
		File f = new File(path);

		if (f.exists()) {
			return true;
		}
		return false;
	}

	public static float toDegreesUnit(Vector2 vector2) {
		float direction = 0;
		if (vector2.x == 0 && vector2.y == 0) {
			return direction;
		}
		direction = (float) Math.toDegrees(Math.acos(vector2.x));

		if (vector2.y > 0)
			return direction;
		else
			return -direction;
	}

	public static double toRadiansUnit(Vector2 vector2) {
		double direction = 0;
		if (vector2.x == 0 && vector2.y == 0) {
			return direction;
		}
		direction = Math.acos(vector2.x);

		if (vector2.y > 0)
			return direction;
		else
			return -direction;
	}

	/**
	 * Ham chuyen doi vecto thanh goc
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static float toDegrees(double x, double y) {
		float f = (float) Math.toDegrees(Math.atan2(y, x));
		return f;
	}

	public static String getDataFromNetwork(String urlLink) {
		try {
			String str = "";

			URL url = new URL(urlLink);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "//text plain");
			connection.setRequestProperty("Connection", "close");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputstream = connection.getInputStream();
				int length = connection.getContentLength();
				if (length != -1) {
					str = convertStreamToString(inputstream);
				}
			}

			return str;
		} catch (IOException e) {
			return null;
		}
	}

	public static String readFileToString(String path) {

		StringBuilder content = new StringBuilder();

		try {
			File fileDir = new File(path);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileDir), "UTF-8"));

			String str;
			String NL = System.getProperty("line.separator");
			while ((str = in.readLine()) != null) {
				content.append(str).append(NL);
			}

			in.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return (content == null ? null : content.toString());
	}

	public static boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static void setText(Bitmap bitmap, String string, int size,
			int color, int maxTextInLine) {
		setText(bitmap, string, size, color, maxTextInLine, 0, 0, size / 4,
				bitmap.getWidth(), bitmap.getHeight());
	}

	/**
	 * 
	 * @param bitmap
	 * @param string
	 * @param size
	 * @param color
	 * @param maxTextInLine
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @return HeightDrawText
	 */
	public static int setText(Bitmap bitmap, String string, int size,
			int color, int maxTextInLine, int pStroke, int pX, int pY,
			int pWidth, int pHeight) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(size);
		// paint.setLinearText(true);
		Canvas canvas = new Canvas(bitmap);
		// paint.setStrokeWidth(pStroke);
		final int widthBm = pWidth;
		final int length = string.length();
		int indext = 0;
		int x = size;
		float[] widths = new float[length];
		char[] characters = string.toCharArray();
		paint.getTextWidths(string, widths);
		// int lengthMaxInline = (int) ((bitmap.getWidth() - size * 2) /
		// widths[0]);
		ArrayList<String> mListCardItems = new ArrayList<String>();
		while (indext < length) {
			int lengthMaxInline = 0;
			int lengthMaxInlineNospace = -1;
			int tamp = x * 2;
			for (int i = indext; i < length; i++) {
				tamp += widths[i];
				if (tamp > widthBm)
					break;
				lengthMaxInline++;
				if (i + 1 < length) {
					if (characters[i] != ' ' && characters[i + 1] == ' ')
						lengthMaxInlineNospace = lengthMaxInline;
				} else {
					lengthMaxInlineNospace = lengthMaxInline;
				}

			}
			if (lengthMaxInlineNospace != -1)
				lengthMaxInline = lengthMaxInlineNospace;
			String s = string.substring(indext,
					Math.min(indext + lengthMaxInline, length));
			indext += lengthMaxInline;
			mListCardItems.add(s);

			for (int i = indext; i < length; i++) {
				if (characters[i] == ' ')
					indext++;
				else
					break;
			}
		}
		int y = size;
		int colorStroke = Color.argb(255, 0, 0, 0);
		for (String cardItem : mListCardItems) {
			// canvas.drawText(cardItem,
			// (widthBm - paint.measureText(cardItem)) / 2, y, paint);]
			paint.setColor(colorStroke);
			paint.setStrokeWidth(pStroke);
			canvas.drawText(cardItem, pX + x, pY + y, paint);

			paint.setColor(color);
			paint.setStrokeWidth(0);
			canvas.drawText(cardItem, pX + x, pY + y, paint);

			y += size;
		}
		return y - size / 2;
	}

	public static void setText(Bitmap bitmap, String string, int size,
			int color, int colorStroke, int Stroke) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(size);
		// paint.setLinearText(true);
		// paint.setTextSkewX(-0.3f);
		// paint.setTextSkewX(-0.3f);
		float scaleX = 1f;
		// paint.setTextScaleX(scaleX);
		Canvas canvas = new Canvas(bitmap);
		// paint.setColor(colorStroke);
		// paint.setStrokeWidth(Stroke);
		// canvas.drawText(string, bitmap.getWidth() / 2
		// - (string.length() * size / 4) * scaleX, bitmap.getHeight() / 2
		// + size / 4, paint);
		Rect rect = new Rect();
		paint.getTextBounds(string, 0, string.length(), rect);

		paint.setStrokeWidth(Stroke);
		paint.setColor(colorStroke);
		canvas.drawText(string, bitmap.getWidth() / 2 - (rect.width() / 2)
				* scaleX, bitmap.getHeight() / 2 + rect.height() / 2, paint);

		paint.setStrokeWidth(0);
		paint.setColor(color);
		canvas.drawText(string, bitmap.getWidth() / 2 - (rect.width() / 2)
				* scaleX, bitmap.getHeight() / 2 + rect.height() / 2, paint);
	}

	public static void setText(Bitmap bitmap, float x, float y, String string,
			int size, int color, int Stroke) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(size);
		Canvas canvas = new Canvas(bitmap);
		paint.setColor(color);
		paint.setStrokeWidth(Stroke);
		canvas.drawText(string, x, y, paint);
	}

	public static Bitmap scaleBitmap(Bitmap bitmap, float width, float height) {
		Matrix matrix = new Matrix();
		float sx = width / bitmap.getWidth();
		float sy = height / bitmap.getHeight();
		matrix.setScale(sx, sy);
		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bitmap2;
	}

	public static void addBitmapInBitmapFixSize(Bitmap bitmap, Bitmap bitmapAdd) {
		CANVAS.setBitmap(bitmap);
		PAINT.setAntiAlias(true);
		CANVAS.drawBitmap(bitmapAdd, null, new Rect(0, 0, bitmap.getWidth(),
				bitmap.getHeight()), PAINT);
	}

	public static void addBitmapInBitmapFixSize(Bitmap bitmap, Rect rect,
			Bitmap bitmapAdd, Rect rectAdd) {
		PAINT.setAntiAlias(true);
		CANVAS.setBitmap(bitmap);
		CANVAS.drawBitmap(bitmapAdd, rectAdd, rect, PAINT);
	}

	public static void vibrate(Context context, long[] pattern) {
		if (GameInfo.ENABLE_VIBRATE) {
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(pattern, -1);
		}
	}

	public static void getPositionClickHasCamera(MotionEvent ev, Vector2 vector2) {
		ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
		vector2.x = BaseObject.sSystemRegistry.cameraSystem.getX()
				+ ev.getRawX() * (1.0f / params.viewScaleXAfter);
		vector2.y = BaseObject.sSystemRegistry.cameraSystem.getY()
				+ params.gameHeightAfter - ev.getRawY()
				* (1.0f / params.viewScaleYAfter);
	}

	public static void getPositionClickNonCamera(MotionEvent ev, Vector2 vector2) {
		ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
		vector2.x = ev.getRawX() * (1.0f / params.viewScaleX);
		vector2.y = params.gameHeight - ev.getRawY()
				* (1.0f / params.viewScaleY);
	}

	public static double CosineInterpolateD(double y1, double y2, double mu) {
		double mu2;
		mu2 = (1 - Math.cos(mu * Math.PI)) / 2;
		return (y1 * (1 - mu2) + y2 * mu2);
	}

	public static float CosineInterpolateF(float y1, float y2, float mu) {
		float mu2;
		mu2 = (float) ((1 - Math.cos(mu * Math.PI)) / 2);
		return (y1 * (1 - mu2) + y2 * mu2);
	}

	public static boolean checkIn(float x, float y, float left, float right,
			float bot, float top) {
		if (x < left)
			return false;
		if (x > right)
			return false;
		if (y < bot)
			return false;
		if (y > top)
			return false;
		return true;
	}

	public static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, boolean scaleUp, boolean recycle) {
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}

		return b2;
	}

	/**
	 * Kiểm tra xem user có phải là dùng sim tại Việt Nam hay không
	 * 
	 * @param context
	 * @author Tran Vu Tat Binh (tvtbinh.bino@gmail.com)
	 */
	public static boolean isVietnameseNetwork(Context context) {
		TelephonyManager tel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String networkOperator = tel.getNetworkOperator();

		if (!TextUtils.isEmpty(networkOperator)) {
			try {
				int mcc = Integer.parseInt(networkOperator.substring(0, 3));
				if (mcc == 452) {
					return true;
				}
			} catch (Exception e) {
				// lambt Không parse được mcc từ networkOperator (đối với máy
				// không có sim)
			}
		}

		return false;
	}

	/**
	 * Ghi chuỗi text xuống file trên sdcard.
	 * 
	 * @param path
	 * @param value
	 * @throws IOException
	 */
	public static void writeStringToFile(String path, String value)
			throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
		bufferedWriter.write(value);
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	public static JSONObject getJSONObject(String data) {
		JSONObject producedObject = null;
		try {
			if (!TextUtils.isEmpty(data)) {
				producedObject = new JSONObject(data);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return producedObject;
	}

	/**
	 * Lấy inputstream của file text từ server.
	 * 
	 * @param urlStr
	 * @return
	 * @throws IOException
	 */
	public static InputStream getTextInputStream(String urlStr)
			throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "//text plain");
		connection.setRequestProperty("Connection", "close");
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream inputstream = connection.getInputStream();
			int length = connection.getContentLength();
			if (length != -1) {
				return inputstream;
			}
		}
		return null;
	}

	public static int[] mu2 = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024 };

	public static int get_2MuNGanNhat(final int number) {
		if (number >= 2048)
			return 2048;
		for (int i = mu2.length - 1; i >= 0; i--) {
			if (number >= mu2[i]) {
				return mu2[i];
			}
		}
		return 1;
	}

	/**
	 * Kiểm tra version của openGL trong máy có đủ đáp ứng để start app hay
	 * không.
	 * 
	 * @author lambt
	 */
	public static boolean isGLAvailable(Activity activity, String minVersion) {
		ActivityManager am = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		String eglVersion = info.getGlEsVersion();
		if (minVersion.compareToIgnoreCase(eglVersion) <= 0) {
			return true;
		}
		return false;
	}

	public static float smoothXToY(float x, float y, float smooth) {
		float offset = y - x;
		if (Math.abs(offset) > 0.1f * smooth)
			return (x += smooth * offset);
		else
			return y;
	}

	public static String format(float val, int n, int w) {
		String ZEROES = "000000000000";
		String BLANKS = "            ";
		// rounding
		double incr = 0.5;
		for (int j = n; j > 0; j--)
			incr /= 10;
		val += incr;

		String s = Float.toString(val);
		int n1 = s.indexOf('.');
		int n2 = s.length() - n1 - 1;

		if (n > n2)
			s = s + ZEROES.substring(0, n - n2);
		else if (n2 > n)
			s = s.substring(0, n1 + n + 1);

		if (w > 0 & w > s.length())
			s = BLANKS.substring(0, w - s.length()) + s;
		else if (w < 0 & (-w) > s.length()) {
			w = -w;
			s = s + BLANKS.substring(0, w - s.length());
		}
		return s;
	}

	public static String unzipTextFile(InputStream inputStream) {
		ZipInputStream zipInputStream = null;

		try {
			zipInputStream = new ZipInputStream(new BufferedInputStream(
					inputStream));
			zipInputStream.getNextEntry();
			return convertStreamToString(zipInputStream);

		} catch (Exception e) {
			//
		} finally {
			if (zipInputStream != null) {
				try {
					zipInputStream.close();
				} catch (IOException e) {
					//
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					//
				}
			}
		}
		return null;
	}

	public static void split(ArrayList<String> ArrayListResult, String content,
			String regularExpression, int maxLength) {
		ArrayList<String> arrayListWords = new ArrayList<String>();
		String[] listsWord = content.split(regularExpression);
		for (String string : listsWord) {
			if (string.length() > maxLength) {
				while (string.length() > maxLength) {
					arrayListWords.add("" + string.substring(0, maxLength));
					string = string.substring(maxLength, string.length());
				}
				if (string.length() > 0)
					arrayListWords.add(string);
			} else {
				if (string.length() > 0)
					arrayListWords.add(string);
			}
		}
		final int length = arrayListWords.size();
		int indext = 0;
		while (indext < length) {
			String item = "";
			int daudong = indext;
			while (indext < length
					&& item.length() + arrayListWords.get(indext).length() <= maxLength) {
				if (arrayListWords.get(indext).length() == 0) {
					indext++;
				} else {
					if (indext != daudong)
						item += " ";
					item += arrayListWords.get(indext);
					indext++;
				}
			}
			ArrayListResult.add(item);
		}
	}

	/**
	 * Call to other device. Only support VietNam network.
	 */
	public static boolean callSupport(Context context, String phoneNumber) {
		try {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + phoneNumber));
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			//
		}
		return false;
	}

	public static void setLocale(Context context, String language) {
		Locale locale2 = new Locale(language);
		Locale.setDefault(locale2);
		Configuration config2 = new Configuration();
		config2.locale = locale2;
		context.getResources().updateConfiguration(config2,
				context.getResources().getDisplayMetrics());

		// Editor editor = preferences.edit();
		// editor.putString(Constants.PREF_SETTINGS_LANGUAGE, language);
		// editor.commit();
	}

	public static void setLocaleVn(Context context) {
		setLocale(context, Constants.VI_LANGUAGE);
	}

	public static void setLocaleEng(Context context) {
		setLocale(context, Constants.EN_LANGUAGE);
	}
}