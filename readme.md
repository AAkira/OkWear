OkWear
----------
This is to easily use connection between android wear and device. 

## Usage

### Example

#### Send
* Message Api

```
byte[] bs = ("hello message").getBytes();
String path = "/path"
OkWear ok = new OkWear(context);

// unuse callback listener
ok.sendMessageAll(bs, path, null);

// use callback listener
ok.sendMessageAll(bs, path, new SendResultListener<MessageApi.SendMessageResult>() {
		@Override
		public void onResult(MessageApi.SendMessageResult result) {
			Log.v(TAG, "Status: " + result.getStatus());
		}
	});

```

* Data Api

You can easily setup to use Payload class.
```
OkWear ok = new OkWear(context);

Payload payload =
	new Payload.Builder("PATH")
							.addPayload("key1", 0) 
							.addPayload("key2", "hello")
							.build();

// unuse callback listener
ok.syncData(payload, null);

// use callback listener
ok.syncData(payload, new SendResultListener<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult result) {
				Log.v(TAG, "Status: " + result.getStatus());
			}
		});
```

#### Receive 

implements ReciverListener
```
OkWear ok = new OkWear(this);
ok.registReceiver(this);
```

callback listener(Receivelistener)
```
@Override
public void onReceiveMessage(MessageEvent messageEvent) {
	if (messageEvent.getPath().equals(OkWear.DEFAULT_MESSAGE_API_PATH)) {
		final String messagePayload = new String(messageEvent.getData());
		Log.v(TAG, messagePayload);
	}
}

@Override
public void onReceiveDataApi(DataEventBuffer dataEventBuffer) {
	for (DataEvent event : dataEventBuffer) {
		DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
		final int data = dataMap.getInt(OkWear.DEFAULT_DATA_API_KEY);
		Log.v(TAG, "data: " + data);
	}
}
```

### Attension
**ParentActivty or ParentService** - Use a parent class that extends Activity(Service) to remove the need to put connect() and disconnect() in every activity's onPause and onResume() methods.  
The following example is taken from the sample application:'

```
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

### for Gradle
```
comming soon...
```

## Status

Please wait...  
applying for Maven central


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
