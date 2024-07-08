# aosp_aidl_vhal_custom
## Overview
This is Android AIDL VHAL custom example

## Requirements
* [Set up for AOSP development](https://source.android.com/docs/setup/start/requirements)

## Do

1. Adding custom vehicle properties
* Please read and following below topic
* Reference: https://medium.com/@subin.bsuresh27/adding-custom-vehicle-properties-in-android-aosp-13-aidl-unlocking-customizations-d2a89c29fc64

2. Add an allowlist
* Reference: https://source.android.com/docs/core/permissions/perms-allowlist
* Add privapp permission of package into file ```frameworks/base/data/etc/privapp-permissions-platform.xml```
```
    <privapp-permissions package="vendor.chibihate.hello_vhal">
        <permission name="android.car.permission.CAR_VENDOR_EXTENSION"/>
    </privapp-permissions>
```

3. Clone repo to run app


## Reference
1. https://medium.com/@number1shiksha/how-to-read-car-speed-using-car-property-in-aosp-edaec29f218e
2. https://github.com/nkh-lab/car-api-hello-world