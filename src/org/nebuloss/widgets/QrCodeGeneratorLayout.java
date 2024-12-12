package org.nebuloss.widgets;

import org.nebuloss.models.IStringGenerator;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class QrCodeGeneratorLayout extends LinearLayout {

    private QrCodeView qrCode;
    private ProgressBar refreshBar;
    private TextView qrCodeDataTextView;  // The TextView to display the QR code data
    private int refreshInterval; // 5 seconds
    private CountDownTimer countDownTimer;
    private IStringGenerator qrCodeGenerator;

    @SuppressWarnings("unused")
    public QrCodeGeneratorLayout(Context context,IStringGenerator generator,int refreshInterval){
        super(context);
        qrCodeGenerator=generator;
        this.refreshInterval=refreshInterval;

        setOrientation(LinearLayout.VERTICAL);
        setPadding(20, 20, 20, 20);
        setGravity(Gravity.CENTER);  // Center the content in the layout

        // QR Code ImageView
        qrCode = new QrCodeView(context);
        addView(qrCode);

        //TextView to display QR code data (above the progress bar)
        qrCodeDataTextView = new TextView(context);
        qrCodeDataTextView.setPadding(0, 20, 0, 0); // Add padding for spacing
        qrCodeDataTextView.setGravity(Gravity.CENTER);  // Center the text inside the TextView
        setDebugMode(false);
        
        addView(qrCodeDataTextView);

        // Refresh ProgressBar (below QR Code and TextView)
        refreshBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        refreshBar.setMax(refreshInterval);
        addView(refreshBar);

        IconTextButton refreshButton=new IconTextButton(context, android.R.drawable.ic_popup_sync, "Refresh");
        
        refreshButton.setOnClickListener(v -> {
            countDownTimer.onFinish();
        });

        addView(refreshButton);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startGeneration();
            }
        });
    }

    public QrCodeGeneratorLayout(Context context,IStringGenerator generator){
        this(context,generator,5000);
    }

    public void setDebugMode(boolean status){
        qrCodeDataTextView.setVisibility(status? View.VISIBLE:View.GONE);
    }

    public void startGeneration() {
        stopGeneration();
        generateQRCode(); // Generate the initial QR code

        countDownTimer = new CountDownTimer(refreshInterval, 50) { // Update progress bar every 50ms
            @Override
            public void onTick(long millisUntilFinished) {
                refreshBar.setProgress((int) (refreshInterval - millisUntilFinished));
            }

            @Override
            public void onFinish() {
                generateQRCode();
                refreshBar.setProgress(0);
                start(); // Restart the timer for continuous refresh
            }
        }.start();
    }


    public void stopGeneration() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void generateQRCode() {
        try {
            String qrCodeData = qrCodeGenerator.generate();  // Get the data to display
            qrCode.generateQRCode(qrCodeData);  // Generate QR code with the data
            qrCodeDataTextView.setText(qrCodeData);  // Update the TextView with the QR code data
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

