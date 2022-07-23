# Last Fm Album Store
## Android application to browse albums through LastFm Api and save them localy

To launch the app you need API Key from LastFm. Get it here: https://www.last.fm/api/authentication

Put your API Key inside `local.properties` file.

Contents of this file should now be similar:
```
sdk.dir={Your SDK path}
LAST_FM_API_KEY={PASTE_YOUR_API_KEY_HERE}
```

### Some libraries used in this app:
- Hilt - DI
- Room - storing data in database
- Retrofit - network calls
- Moshi - serialisation and deserialization of JSON
- Glide - image loading and caching
- LiveData and ViewModel
- Navigation
- ViewBinding delegate - easier view binding

### App screenshots:
<div>
<img src="https://user-images.githubusercontent.com/31513193/180619505-b3c3a496-159c-4d23-aa7f-a5aedcaecb14.png" width="300">
<img src="https://user-images.githubusercontent.com/31513193/180619761-e71a6746-409c-4994-82fe-02135c7b6750.png" width="300">
<img src="https://user-images.githubusercontent.com/31513193/180619548-d65bc43d-f38e-49ab-ac2d-7dfb0d1b6d6a.png" width="300">
<img src="https://user-images.githubusercontent.com/31513193/180619588-7b71f73c-57fe-4ba6-94f5-ba3ec609f9ff.png" width="300">
<img src="https://user-images.githubusercontent.com/31513193/180619507-917766b3-c83a-4aa9-86fe-9f23353d8424.png" width="300">
</div>
