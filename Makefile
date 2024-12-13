# load environement variables
ifneq (,$(wildcard ./.env))
# avoid calling .env rule by default
.PHONY: .env
    include .env
# export all variables defined in .env file
    export $(shell sed 's/=.*//' .env)
endif

# Main target and filename of the executable
ANDROID_MANIFEST= AndroidManifest.xml
PLATFORM_LEVEL = 26
JAVA_VERSION = 8
JAVA_HOME= /usr/lib/jvm/java-$(JAVA_VERSION)-openjdk

OUT_DIR :=out
BUILD_DIR := build
SRC_DIR := src
LIB_DIR := lib
RES_DIR := res

JAVAC = $(JAVA_HOME)/bin/javac
JAR= $(JAVA_HOME)/bin/jar
JAVA= $(JAVA_HOME)/bin/java
D8 = d8
AAPT = aapt
KEYTOOL = keytool
APKSIGNER = apksigner
ZIPALIGN= zipalign
ADB = adb

PACKAGE_NAME= org.nebuloss.qr1gen
PACKAGE_MAINACTIVITY= QrCodeActivity

# This makefile allows to compile automatically java files and produce a jar
rwildcard=$(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2)$(filter $(subst *,%,$2),$d))
# List of all the .java source files to compile
SRC := $(call rwildcard,$(SRC_DIR),*.java)
# List of all the .class object files to produce
OBJ := $(patsubst $(SRC_DIR)/%.java,$(BUILD_DIR)/%.class,$(SRC))

# List of jar files inside lib folder
JAR_LIB := $(call rwildcard,$(LIB_DIR),*.jar)

noop=
space = $(noop) $(noop)
ANDROID_JAR:=$(ANDROID_HOME)/platforms/android-$(PLATFORM_LEVEL)/android.jar

CLASSES_PATH:=$(subst $(space),:,$(strip $(BUILD_DIR) $(ANDROID_JAR) $(JAR_LIB)))

JARFILE:= $(OUT_DIR)/classes.jar
DEXFILE:= $(OUT_DIR)/classes.dex
APKFILE:= $(dir $(DEXFILE))app.apk

all: $(APKFILE)

$(BUILD_DIR) $(OUT_DIR)/:
	@echo Creating folder $@...
	@mkdir -p ${@:/=}

$(BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	@echo Compiling $<...
	@$(JAVAC) -cp "$(CLASSES_PATH)" $< -d $(BUILD_DIR) -sourcepath $(SRC_DIR)

$(JARFILE): $(dir $(JARFILE)) $(BUILD_DIR) $(OBJ)
	@echo Creating jar $@...
	@$(JAR) cMf $@ -C $(BUILD_DIR) .

$(DEXFILE): $(JARFILE)
	@echo Creating dex $@...
	@$(D8) --release --output $(dir $@) $(JARFILE) $(JAR_LIB) --classpath $(ANDROID_JAR)

$(APKFILE).unsigned: $(DEXFILE)
	@echo Creating unsigned apk $@...
	@$(AAPT) package -f -m -F $@ -M $(ANDROID_MANIFEST) -S $(RES_DIR) -I "$(ANDROID_JAR)"
	@cd $(dir $@) && $(AAPT) add -v $(notdir $@) $(notdir $<)

$(APKFILE).aligned: $(APKFILE).unsigned
	@echo Verifying $< alignment and creating $@...
	@if [ -f $@ ];then rm $@;fi
	$(ZIPALIGN) -p -v 4 $< $@

$(APKFILE): $(APKFILE).aligned $(KEYSTORE)
	@echo Signing $< and creating $@
	@$(APKSIGNER) sign --ks $(KEYSTORE) --ks-pass pass:$(KEYSTORE_PASSWORD) --out $@ $<

.PHONY: install
install: $(APKFILE)
	@echo "Installing $<..."
	@$(ADB) install $<

.PHONY: uninstall
uninstall: 
	@echo "Uninstalling $(PACKAGE_NAME)..."
	@$(ADB) uninstall $(PACKAGE_NAME)

.PHONY: run
run:
	@echo "Running application $(PACKAGE_NAME)..."
	@$(ADB) shell am start -n $(PACKAGE_NAME)/$(PACKAGE_NAME).$(PACKAGE_MAINACTIVITY)

$(KEYSTORE):
	@echo Generating keystore $@...
	@$(KEYTOOL) -genkeypair -v \
		-keystore $@ \
		-alias $(basename $@) \
		-keyalg RSA \
		-keysize 2048 \
		-validity 10000 \
		-dname "CN=$(basename $@)" \
		-storepass "$(KEYSTORE_PASSWORD)" -keypass "$(KEYSTORE_PASSWORD)"

clean:
	rm -rf $(OUT_DIR)
	rm -rf $(BUILD_DIR)
