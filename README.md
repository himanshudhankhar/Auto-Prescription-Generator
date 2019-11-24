# Auto-Prescription-Generator
### Requirements
```
python version >= 2.8
node version >=8
nltk , gensim , ngrok or public url , numpy , pandas, beautifulsoup
```

### Steps for steping up server and building app
After cloning and meeting all requirements just do step by step in the directory "./Auto-Prescription-Generator"

To add node packages that are required in the project
```
1. npm install
```
If you don't have a public url use ngrok to provide tunneling to localhost server

After installing ngrok run the command in the terminal where you have installed it

```
2. ngrok http 5000
```
It will provide wierd public url , on which all the requests will be directed to localhost:5000

After getting a public url either by yourself or by ngrok , update the url androidapp where ever there is a request being made to server , update the url you can find them in Auto-Prescription-Generator\AndroidApp\Telemedicine\app\src\main\java\com\docs\telemedicine

Files that exactly required to changed are Meetings_Frag.java , Profile_Frag.java , Records_FRag.java , VideoCallingActivity.java , ShowPrescriptions.java 

After updating url in the android app now its time to start the server
```
3. node index
```
Now in android studio build the app , built apk can be find in Auto-Prescription-Generator\AndroidApp\Telemedicine\app\build\outputs\apk\debug with name app-debug.java
```
4. Make a build of app in android studio
```
```
5. Run the app, now app and server both are running
```


