/**
 *
 */
package com.fgomiero.cordova.plugin;

import java.io.IOException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.net.Uri;
import java.io.File;

/**
 * @author Fabio Gomiero
 *
 */
public class ExternalFileUtil extends CordovaPlugin {

	/* (non-Javadoc)
	 * @see org.apache.cordova.CordovaPlugin#execute(java.lang.String, org.json.JSONArray, org.apache.cordova.CallbackContext)
	 */
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
            if (action.equals("openWith")) {
                openFile(args.getString(0));
                callbackContext.success();
            }
        } catch (JSONException e) {
        	callbackContext.error(e.getLocalizedMessage());
        } catch (IOException e) {
            callbackContext.error(e.getLocalizedMessage());
        }
		return false;
	}

	/**
	 *
	 * @param url
	 * @throws IOException
	 */
	private void openFile(String url) throws IOException {
		// Create URI
		final CordovaResourceApi resourceApi = webView.getResourceApi();

		Uri tmpTarget = Uri.parse(url);
		final Uri uri = resourceApi.remapUri(tmpTarget.getScheme() != null ? tmpTarget : Uri.fromFile(new File(url)));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		//Intent intent = new Intent(Intent.ACTION_SEND);
		String type = "*/*";
		//intent = new Intent(Intent.ACTION_VIEW);
		// Check what kind of file you are trying to open, by comparing the url with extensions.
		// When the if condition is matched, plugin sets the correct intent (mime) type,
		// so Android knew what application to use to open the file

		if (url.contains(".doc") || url.contains(".docx")) { // Word document
			type = "application/msword";
		} else if (url.contains(".pdf")) { // PDF file
			type = "application/pdf";
		} else if (url.contains(".ppt") || url.contains(".pptx")) { // Powerpoint file
			type = "application/vnd.ms-powerpoint";
		} else if (url.contains(".xls") || url.contains(".xlsx")) { // Excel file
			type = "application/vnd.ms-excel";
		} else if (url.contains(".rtf")) { // RTF file
			type = "application/rtf";
		} else if (url.contains(".wav")) { // WAV audio file
			type = "audio/x-wav";
		} else if (url.contains(".gif")) { // GIF file
			type = "image/gif";
		} else if (url.contains(".jpg") || url.contains(".jpeg")) { // JPG file
			type = "image/jpeg";
		} else if (url.contains(".txt")) { // Text file
			type = "text/plain";
		} else if (url.contains(".mpg") || url.contains(".mpeg")
				|| url.contains(".mpe") || url.contains(".mp4")
				|| url.contains(".avi")) { // Video files
			type = "video/*";
		}

		// if you want you can also define the intent type for any other file
		// additionally use else clause below, to manage other unknown extensions
		// in this case, Android will show all applications installed on the device
		// so you can choose which application to use

		else {
		}

		//intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setDataAndType(uri, type);

		this.cordova.getActivity().startActivity(intent);
	}
}
