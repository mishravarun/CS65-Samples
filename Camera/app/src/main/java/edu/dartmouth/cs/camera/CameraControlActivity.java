package edu.dartmouth.cs.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
// extra credit -- take from camera or gallery

public class CameraControlActivity extends Activity {

	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	//public static final int REQUEST_CODE_CROP_PHOTO = 2;

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final String URI_INSTANCE_STATE_KEY = "saved_uri";

	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private boolean isTakenFromCamera;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
        checkPermissions();
		mImageView = (ImageView) findViewById(R.id.imageProfile);

		if (savedInstanceState != null) {
			mImageCaptureUri = savedInstanceState
					.getParcelable(URI_INSTANCE_STATE_KEY);
		}

		loadSnap();
	}

	/**
	 * Code to check for runtime permissions.
	 */
	private void checkPermissions() {
		if(Build.VERSION.SDK_INT < 23)
			return;

		if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				|| checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
		}else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)||shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					//Show an explanation to the user *asynchronously*
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("This permission is important for the app.")
							.setTitle("Important permission required");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
							}

						}
					});
					requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
				}else{
					//Never ask again and handle your app without permission.
				}
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the image capture uri before the activity goes into background
		outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
	}

	// ****************** button click callbacks ***************************//

	public void onSaveClicked(View v) {
		// Save picture
		saveSnap();
		// Making a "toast" informing the user the picture is saved.
		Toast.makeText(getApplicationContext(),
				getString(R.string.ui_profile_toast_save_text),
				Toast.LENGTH_SHORT).show();
		// Close the activity
		finish();
	}

	public void onChangePhotoClicked(View v) {
		// changing the profile image, show the dialog asking the user
		// to choose between taking a picture
		// Go to MyRunsDialogFragment for details.
		displayDialog(MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER);
	}

	// Handle data after activity returns.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case REQUEST_CODE_TAKE_FROM_CAMERA:
			// Send image taken from camera for cropping
			beginCrop(mImageCaptureUri);
			break;

		case Crop.REQUEST_CROP: //We changed the RequestCode to the one being used by the library.
			// Update image view after image crop
			handleCrop(resultCode, data);

			// Delete temporary image taken by camera after crop.
			if (isTakenFromCamera) {
				File f = new File(mImageCaptureUri.getPath());
				if (f.exists())
					f.delete();
			}

			break;
		}
	}

	// ******* Photo picker dialog related functions ************//

	public void displayDialog(int id) {
		DialogFragment fragment = MyRunsDialogFragment.newInstance(id);
		fragment.show(getFragmentManager(),
				getString(R.string.dialog_fragment_tag_photo_picker));
	}

	public void onPhotoPickerItemSelected(int item) {
		Intent intent;

		switch (item) {

		case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
			// Take photo from camera，
			// Construct an intent with action
			// MediaStore.ACTION_IMAGE_CAPTURE
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// Construct temporary image path and name to save the taken
			// photo
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            mImageCaptureUri = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            /**
                This was the previous code to generate a URI. This was throwing an exception -
                "android.os.StrictMode.onFileUriExposed" in Android N.
                This was because StrictMode prevents passing URIs with a file:// scheme. Once you
                set the target SDK to 24, then the file:// URI scheme is no longer supported because the
                security is exposed. You can change the  targetSDK version to be <24, to use the following code.
                The new code as written above works nevertheless.


                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_"
                        + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            **/

			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			try {
				// Start a camera capturing activity
				// REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
				// defined to identify the activity in onActivityResult()
				// when it returns
				startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
			isTakenFromCamera = true;
			break;
			
		default:
			return;
		}

	}

	// ****************** private helper functions ***************************//

	private void loadSnap() {


		// Load profile photo from internal storage
		try {
			FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
			Bitmap bmap = BitmapFactory.decodeStream(fis);
			mImageView.setImageBitmap(bmap);
			fis.close();
		} catch (IOException e) {
			// Default profile photo if no photo saved before.
			mImageView.setImageResource(R.drawable.default_profile);
		}
	}

	private void saveSnap() {

	// Commit all the changes into preference file
		// Save profile image into internal storage.
		mImageView.buildDrawingCache();
		Bitmap bmap = mImageView.getDrawingCache();
		try {
			FileOutputStream fos = openFileOutput(
					getString(R.string.profile_photo_file_name), MODE_PRIVATE);
			bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/** Method to start Crop activity using the library
	 *	Earlier the code used to start a new intent to crop the image,
	 *	but here the library is handling the creation of an Intent, so you don't
	 * have to.
	 *  **/
	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
		Crop.of(source, destination).asSquare().start(this);
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			mImageView.setImageURI(Crop.getOutput(result));
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

}