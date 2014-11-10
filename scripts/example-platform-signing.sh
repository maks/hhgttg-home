#!/bin/sh

SIGNING_JAR=~/AOSP/signing/aospsign.jar
CERTS_DIR=~/AOSP/signing/aosp_platform_keys

java -jar $SIGNING_JAR $CERTS_DIR/platform.x509.pem $CERTS_DIR/platform.pk8 bin/hhgtg_home-debug.apk hhgth_home_signed.apk