package vendor.chibihate.hello_vhal;

import androidx.appcompat.app.AppCompatActivity;

import android.car.Car;
import android.car.VehiclePropertyIds;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HELLO_VHAL";
    private TextView mTextView;
    private Car mCar;
    String[] perms = { "android.car.permission.CAR_SPEED" };
    int permsRequestCode = 200;
    CarPropertyManager mCarPropertyManager;
    private boolean isSuccessed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
        if (!isSuccessed) {
            mCarPropertyManager = (CarPropertyManager) Car.createCar(this).getCarManager(Car.PROPERTY_SERVICE);
            registerCarPropertyManagerCBs();
        } else {
            requestPermissions(perms, permsRequestCode);
        }

    }

    CarPropertyManager.CarPropertyEventCallback mCallBack = new CarPropertyManager.CarPropertyEventCallback() {
        @Override
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            Log.d(TAG, "onChangeEvent: " + carPropertyValue.toString());
            if (carPropertyValue.getPropertyId() == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                float value = (Float) carPropertyValue.getValue();
                mTextView.setText("Speed: " + Math.round(value));
            }
        }

        @Override
        public void onErrorEvent(int i, int i1) {
            Log.e(TAG, "onErrorEvent: " + i);
        }
    };

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");

        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
        switch (permsRequestCode) {
            case 200:
                Log.d(TAG, "onRequestPermissionsResult: " + permsRequestCode);
                boolean carPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (carPermission) {
                    mCar = Car.createCar(this);
                    mCarPropertyManager = (CarPropertyManager) mCar.getCarManager(Car.PROPERTY_SERVICE);
                    mCarPropertyManager.registerCallback(mCallBack, VehiclePropertyIds.PERF_VEHICLE_SPEED,
                            CarPropertyManager.SENSOR_RATE_NORMAL);

                    Log.d(TAG, "onRequestPermissionsResult: isConnected " + mCar.isConnected());
                    isSuccessed = true;
                } else {
                    requestPermissions(perms, permsRequestCode);
                }
                break;
            default:
                Log.d(TAG, "onRequestPermissionsResult: default " + permsRequestCode);
                break;
        }
    }

    private void registerCarPropertyManagerCBs() {
        Log.d(TAG, "Test CarPropertyManager callbacks:");
        mCarPropertyManager.registerCallback(new CarPropertyManager.CarPropertyEventCallback() {
            @Override
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                Log.d(TAG, "CUSTOM_PROPERTY: onChangeEvent(" + carPropertyValue.getValue() + ")");
                mTextView.setText(String.valueOf(carPropertyValue.getValue()));
            }

            @Override
            public void onErrorEvent(int propId, int zone) {
                Log.d(TAG, "CUSTOM_PROPERTY: onErrorEvent(" + propId + ", " + zone + ")");
            }
        }, VehiclePropertyIds.CUSTOM_PROPERTY, CarPropertyManager.SENSOR_RATE_ONCHANGE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCar.disconnect();
    }
}