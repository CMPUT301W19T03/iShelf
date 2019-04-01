package ca.ualberta.ishelf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;


/**
 * ScanActivity
 *
 * Allows you to scan a book to get its iSBN
 * Using Google Books API, all information about the book is retrieved
 *
 * @author : Faisal
 */

public class ScanActivity extends AppCompatActivity {

    final String TAG = "scanActivity";

    // camera variables
    private CameraManager cameraManager;
    private int cameraFacing;
    private String cameraId;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession scanSession;
    private CaptureRequest captureRequest;
    private CameraDevice.StateCallback stateCallback;

    // handler variables
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;

    // textureView variables
    private Size Size;
    private TextureView.SurfaceTextureListener surfaceTextureListener;

    // MLKit variables
    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;

    private Bitmap lastScan;
    private String outputISBN = "";

    private int CAMERA_REQUEST_CODE = 29;

    // elements on screen
    private TextureView textureView;
    private Button lastISBN;
    private FrameLayout shutter;
    private final AlphaAnimation fade = new AlphaAnimation(1, 0);
    private ProgressBar progressBar;
    private String visit;


    String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    private final OkHttpClient client = new OkHttpClient();
    private String description = "";
    private String title = "";
    private String year = "";
    private String genre = "";
    private String author = "";
    private String URLimage = "";
    private Button finishScanButton;

    public boolean testing = false;
    public boolean finished_computation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // request to use cameras
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

        /* get what role scanning plays:
           - is it getting information of a book   or
           - is it getting the ISBN for verification purposes
         */
        Intent intent = getIntent();
        visit = intent.getStringExtra("task");

        // set up back button -- just go back to previous activity (that is, EditBookProfile)
        FloatingActionButton backScan = findViewById(R.id.back_button_camera);
        backScan.setOnClickListener(view -> finish());

        // basic camera setup
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraFacing = CameraCharacteristics.LENS_FACING_BACK;

        // set up ISBN scanner
        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_EAN_13)
                .build();
        detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);

        // get elements on screen
        textureView = findViewById(R.id.texture_view);
        lastISBN = findViewById(R.id.last_ISBN);
        shutter = findViewById(R.id.snapshot_effect);
        progressBar = findViewById(R.id.progress_bar_scan);
        finishScanButton = findViewById(R.id.accept_scan_button);

        setUpShutter();

        // set up the surfaceTexture for the camera
        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                initializeCamera();
                openCamera();
            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            }
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }
        };

        // connects to the camera to create the preview
        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice cameraDevice) {
                ScanActivity.this.cameraDevice = cameraDevice;
                createPreviewSession();
            }
            @Override
            public void onDisconnected(CameraDevice cameraDevice) {
                cameraDevice.close();
                ScanActivity.this.cameraDevice = null;
            }
            @Override
            public void onError(CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                ScanActivity.this.cameraDevice = null;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        openBackgroundThread();
        if (textureView.isAvailable()) {
            initializeCamera();
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // make sure to not leave any un-used resources lying around
        closeCamera();
        closeBackgroundThread();
        Log.d("cam","camStop");
    }

    private void closeCamera() {
        if (scanSession != null) {
            scanSession.close();
            scanSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    /**
     * Allows us to see a live preview of the camera
     * @author: Faisal
     */
    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(Size.getWidth(), Size.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                captureRequest = captureRequestBuilder.build();
                                ScanActivity.this.scanSession = cameraCaptureSession;
                                ScanActivity.this.scanSession.setRepeatingRequest(captureRequest,
                                        null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.d("cam","cam3");
            e.printStackTrace();
        }
    }

    // sets up the camera
    private void initializeCamera() {
            try {
                for (String cameraId : cameraManager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics =
                            cameraManager.getCameraCharacteristics(cameraId);
                    if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
                        StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        Size = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                        this.cameraId = cameraId;
                    }
                }
            } catch (CameraAccessException e) {
                Log.d("cam","cam1");
                e.printStackTrace();
            }
    }

    // opens the camera
    private void openCamera() {
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
                }
            } catch (CameraAccessException e) {
                Log.d("cam","cam2");
                e.printStackTrace();
            }
    }

    // opens a background thread for the camera
    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }


    /**
     * Gets the information of a book using its ISBN (Google Books API)
     * @author: Faisal
     */
    public void getBookInfo(){
        Request request = new Request.Builder()
                .url(url + outputISBN)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async OKHttp call");
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String originalURL = URLimage;

                /*
                Lots of error handling, since Google Books API may give incomplete and messy
                information
                 */
                String string = null;
                if (responseBody != null) {
                    string = responseBody.string();
                }
                JSONObject urlJSON = null;
                try {
                    urlJSON = new JSONObject(string);
                } catch (JSONException ignored) { }
                JSONArray books = null;
                try {
                    books = urlJSON.getJSONArray("items");
                } catch (JSONException ignored){}

                if (books != null && books.length() > 0) {
                    for (int i = 0; i < books.length(); i++) {
                        JSONObject book = null;
                        try {
                            book = books.getJSONObject(i);
                        } catch (JSONException e) {
                            continue; }
                        JSONObject volumeInfo = null;
                        try {
                            volumeInfo = book.getJSONObject("volumeInfo");
                        } catch (JSONException e) {
                            continue; }
                        try {
                            if (author.equals("")) {
                                author = volumeInfo.getJSONArray("authors").getString(0);
                            }
                        } catch (JSONException ignored) {}
                        try {
                            if (genre.equals("")) {
                                genre = volumeInfo.getJSONArray("categories").getString(0);
                            }
                        } catch (JSONException ignored) {}
                        try {
                            if (description.equals("")) {
                                description = volumeInfo.getString("description");
                            }
                        } catch (JSONException ignored) {}
                        try {
                            if (title.equals("")) {
                                title = volumeInfo.getString("title");
                            }
                        } catch (JSONException ignored) {}
                        try {
                            if (year.equals("")) {
                                year = volumeInfo.getString("publishedDate");
                            }
                        } catch (JSONException ignored) {}
                        try {
                            if (i == 0) {
                                URLimage = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                                URLimage = URLimage.replace("http", "https");
                                URLimage = URLimage.replace("zoom=1", "zoom=0");
                            }
                            else if (URLimage.equals("")) {
                                URLimage = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                                URLimage = URLimage.replace("http", "https");
                                URLimage = URLimage.replace("zoom=1", "zoom=0");
                            }
                        } catch (JSONException ignored) {}
                    }
                }

                // Remember to set the bitmap in the main thread.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (!lastISBN.equals("")) {
                            finishScanButton.setBackground(getDrawable(R.drawable.gradientbutton));
                        }
                        finished_computation = true;
                    }
                });

                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }

            }
        });
    }

    /**
     * Return to EditBookActivity with any information retrieved that is useful
     * @author: Faisal
     */
    public void finishScanButton(View v) {
        if (finished_computation) {
            Bundle extras = new Bundle();
            extras.putString("ISBN", outputISBN);
            extras.putString("description", description);
            extras.putString("title", title);
            extras.putString("year", year);
            extras.putString("author", author);
            extras.putString("genre", genre);
            extras.putString("URL", URLimage);

            Intent intent = null;
            if (visit.equals("get_book_info")) {
                intent = new Intent(ScanActivity.this, EditBookActivity.class);
            }
            else if (visit.equals("verification")) {
                intent = new Intent(ScanActivity.this, BookProfileActivity.class);
            }
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Retrieves the ISBN from back of book using FireBaseVision
     * @author: Faisal
     */
    public void scanFAB(View v){
        finished_computation = false;
        shutter.startAnimation(fade);

        lastScan = textureView.getBitmap();
        if (!testing) {
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(lastScan);
            detector.detectInImage(image)
                    .addOnSuccessListener(barCodes -> {
                        if (barCodes.size() > 0) {
                            // get the ISBN
                            FirebaseVisionBarcode ISBN = barCodes.get(0);
                            outputISBN = ISBN.getRawValue();
                            lastISBN.setText(outputISBN);
                            progressBar.setVisibility(View.VISIBLE);
                            // get the Book information from the ISBN
                            getBookInfo();
                        }
                        else {
                            String output = "Detected No ISBN";
                            @SuppressLint("ShowToast") Toast toast = Toast.makeText(ScanActivity.this, output, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 64);
                            toast.show();
                        }
                    });
        }

        if (testing) {
            outputISBN = "9780307401199";
            lastISBN.setText(outputISBN);
            progressBar.setVisibility(View.VISIBLE);
            getBookInfo();
        }
    }

    /**
     * Sets up a flash of white that appears when the user takes a photo/scan
     * @author: Faisal
     */
    public void setUpShutter(){
        shutter.setVisibility(View.INVISIBLE);
        fade.setDuration(1000);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation anim) {
                shutter.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation anim) {
            }
            @Override
            public void onAnimationStart(Animation anim) {
            }
        });
    }
}
