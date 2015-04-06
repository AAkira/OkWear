OkWear
----------

![Screen](/images/okwear_256.png)
[![Platform](http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat)](http://developer.android.com/index.html)
[![Language](http://img.shields.io/badge/language-java-orange.svg?style=flat)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License](http://img.shields.io/badge/license-apache2.0-lightgrey.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-OkWear-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1698)
This is library to easily use connection between android wear and handheld device.

## Usage

### Example

#### Send
* [Message Api](https://developer.android.com/reference/com/google/android/gms/wearable/MessageApi.html)

```java
static final String PATH = "/path"
static final String MESSAGE = "hello"

OkWear ok = new OkWear(context);

// unuse callback listener
ok.sendMessageAll(MESSAGE, PATH);

// use callback listener
ok.sendMessageAll(MESSAGE, PATH, new SendResultListener<MessageApi.SendMessageResult>() {
		@Override
		public void onResult(MessageApi.SendMessageResult result) {
			Log.v(TAG, "Status: " + result.getStatus());
		}
	});

```

* [Data Api](https://developer.android.com/reference/com/google/android/gms/wearable/DataApi.html)

You can easily setup to use Payload class.
```java
OkWear ok = new OkWear(context);

Payload payload =
	new Payload.Builder(PATH)
				.addPayload("key1", 0)
				.addPayload("key2", "hello")
				.build();

// unuse callback listener
ok.syncData(payload);

// use callback listener
ok.syncData(payload, new SendResultListener<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult result) {
				Log.v(TAG, "Status: " + result.getStatus());
			}
		});
```

#### Receive

```java
OkWear ok = new OkWear(this);
ok.registReceiver(this);
```

implements callback listener(`WearReceiveListener`) on your class.
```java
@Override
public void onReceiveMessage(MessageEvent messageEvent) {
	if (messageEvent.getPath().equals(PATH)) {
		final String messagePayload = new String(messageEvent.getData());
		Log.v(TAG, messagePayload);
	}
}

@Override
public void onReceiveDataApi(DataEventBuffer dataEventBuffer) {
	for (DataEvent event : dataEventBuffer) {
		final DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
		final int data1 = dataMap.getInt("key1");
		final String data2 = dataMap.getString("key2");
		Log.v(TAG, "data(int): " + data1);
		Log.v(TAG, "data(string): " + data2);
	}
}
```
### Tips

You can easily create **payloads** using these util classes.

* [Payload](./library/okwear/src/main/java/jp/android/a/akira/library/okwear/util/Payload.java)
Payload is created by key value pairs.

| Support values ||||||
|:-:|:-:|:-:|:-:|:-:|:-:|
|ArrayList&lt;Integer>|Asset|Bitmap|boolean|byte|int|
|ArrayList&lt;String>|byte[]|DataMap|double|float|float[]|
|ArrayList&lt;Datamap>|long|long[]|String|String[]|||


```java
Payload payload =
	new Payload.Builder("path")
				.addPayload("key1", 0)
				.addPayload("key2", "hello")
				.build();
```
* [ParseByteArray](./library/okwear/src/main/java/jp/android/a/akira/library/okwear/util/ParseByteArray.java)

| Support values ||||
|:-:|:-:|:-:|:-:|
|boolean|long|int|short|
|double|float|String|char|

```java
byte[] payload = Parsebytearray.fromString("hello");
```

## Setup

### Gradle
Add the dependency in your ```build.gradle```

```groovy
buildscript {
	repositories {
		jcenter()
	}
}

dependencies {
	compile 'com.github.aakira:okwear:1.0.1'
}
```

### Lifecycle
**Activty, Fragment or Service**
You have to call `connect()` and `disconnect()` methods when `onResume()` and `onPause()` occur in your lifecycle

```java
Okwear mOkWear;

@Override
protected void onResume() {
	super.onResume();
	mOkWear.connect();
}

@Override
protected void onPause() {
	super.onPause();
	mOkWear.disconnect();
}
```

### SDK Version

This library requires over Android 4.3(Jelly Bean)
`minSdkVersion=18`


## License

```
Copyright (C) 2015 A.Akira

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

