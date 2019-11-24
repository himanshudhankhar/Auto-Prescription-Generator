var multer= require('multer');
var express        =        require("express");
var bodyParser     =        require("body-parser");
var app            =        express();
const speech = require('@google-cloud/speech');
const client = new speech.SpeechClient();
var accessDataBase = require('G:/VoiceAssistantApp/accessdatabase');
var fs = require('fs'); 
var getNamesFromData= accessDataBase.getNamesFromData;
// console.log('Hi')
// //Here we are configuring express to use body-parser as middle-ware.
var audio1="notRecieved"
var audio2="notRecieved"
var finalResult={}
var audio1Result={}
var audio2Result={}
var finalPrescriptionReady=false

function listt(setInput){
    var answer=[]
    for (w in setInput){
        answer.push(setInput[w])
    }
    return answer
}


function generateFinalPrescription(){
symps1=  audio1Result['symptoms'];
symps2=audio2Result['symptoms'];
// console.log('symps',symps1,symps2);
let newSet=new Set();

for( w in symps1){
    newSet.add(symps1[w]);
}
for(w in symps2){
    newSet.add(symps2[w]);
}

resultSymps = Array.from(newSet);
// console.log(resultSymps);
finalResult['symptoms']=resultSymps

dis1 = audio1Result['disease'];
dis2 = audio2Result['disease'];
// console.log('diss',dis1,dis2);
let finalDiseases=[];
newSet=new Set();
for( w in dis1){
    if(newSet.has(dis1[w][0])){
        continue;
    }
    newSet.add(dis1[w][0]);
    finalDiseases.push(dis1[w])
}
for(w in dis2){
    if(newSet.has(dis2[w][0])){
        continue;
    }
    newSet.add(dis2[w][0]);
    finalDiseases.push(dis2[w])
}


// console.log('final diseases',finalDiseases);
// console.log(finalDiseases);
if (finalDiseases.length==1){
    finalResult['disease']=finalDiseases[0];
}else{
    let maxDiseaseIndex=0;
    let secondMaxDiseaseIndex=0;
    let maxValue=0;
    let secondMaxValue=0;
    for (let i=0;i<finalDiseases.length;i++){
        if(finalDiseases[i][1] > maxValue ){
            maxValue=finalDiseases[i][1]
            maxDiseaseIndex=i
        }
        if(finalDiseases[i][1] > secondMaxValue && finalDiseases[i][1] < maxValue){
            secondMaxDiseaseIndex=i;
            secondMaxValue=finalDiseases[i][1]
        }
    }
finalDiseasesSelected = [finalDiseases[maxDiseaseIndex],finalDiseases[secondMaxDiseaseIndex]]
finalResult['disease']=finalDiseasesSelected
}

//get dosages
let argumentss=["getFinalDosages.py"]
for(w in finalDiseasesSelected){
    argumentss.push(finalDiseasesSelected[w][0])
}
console.log(argumentss);
let resultDosages=''
var spawn = require("child_process").spawn;
let childProc = spawn('python',argumentss);
childProc.stderr.on('error',(err)=>{
    console.log("error in getting final dosages",err);
})
childProc.stdout.on('data',(data)=>{
    resultDosages += data
    // console.log(data);
});
childProc.on('close',(code)=>{
    //  console.log(resultDosages);
    resultDosages=JSON.parse(resultDosages)
    resultDosages=resultDosages['dosages']
    finalResult['dosage']=resultDosages;
    finalPrescriptionReady=true
    audio1="notRecieved"
    audio2="notRecieved"
console.log(finalResult);
});


}






app.use(bodyParser.urlencoded({ extended: false }));
 app.use(bodyParser.json());

 var Storage = multer.diskStorage({
    destination: function(req, file, callback) {
        
        callback(null, "./Images");
    },
    filename: function(req, file, callback) {
     
        callback(null, file.originalname);
    }
});
app.post('/processCommand', function(request,response){
    // console.log(request.body);
    command =request.body.command;
    requestMaker = request.body.requestMaker;
    posTaggedCommand = request.body.posTaggedCommand;
     
    available_nouns=[];
    for(let i =0;i<posTaggedCommand.length;i+=2){
        if(i+1 < posTaggedCommand.length){
            var cateogory = posTaggedCommand[i+1];
        if(cateogory=="NN"||cateogory == "NNS"||cateogory =="NNP"||cateogory =="NNPS"){
                 available_nouns.push(posTaggedCommand[i]);
        }
        }else{
            continue;
        }
    }
//check from db whether this request maker is valid or not
if(requestMaker==null||requestMaker.length===0){
    response.send({
        success:false,
        error:"Invalid ID of requestMaker"
    });
  console.log("Invalid Id of request maker");
    return;
}

console.log(available_nouns);
//check from database for the avavilable names if part found store there email ids in a set 
 accessDataBase.getNamesFromData(available_nouns,function(getNames){
    response.send({
        availablenames:getNames,
        status:"OK"
    });
    
 });

    

});


app.get('/',function(request,response){
    console.log(request.ips);
    response.send("ok");
});




app.post("/schedule_meeting",function(request,response){

console.log(request.body);
response.send("ok");

})
var upload = multer({
    storage: Storage
}).single("file");
app.post("/patient_audio", function(req, res) {
 
    upload(req, res,async function(err) {
        if (err) {
            console.log(err);
            return res.end("Something went wrong!");
        }
        res.send("Great");
        console.log(req.file.filename);

        //  res.end("File uploaded sucessfully!.");
         let readMe = fs.readFileSync('./Images/'+req.file.filename);  
     
        const audioBytes = readMe.toString('base64');
       
        var spawn = require("child_process").spawn; 
        var child = spawn('python',["getPrescriptions.py",req.file.filename]); 
        var resultData=''
            child.stderr.on('error',(err)=>{
                console.log(err);
            });
            child.stdout.on('data',(data)=>{
                    resultData+=data
            });
            child.on('close',(code)=>{
                    resultData=JSON.parse(resultData)
                    if(audio1=="notRecieved"){
                        audio1="recieved";
                        finalPrescriptionReady=false;
                        audio1Result=resultData
                    }else if(audio1=="recieved" && audio2=="notRecieved"){
                        audio2="recieved"
                        finalPrescriptionReady=false;
                        audio2Result=resultData
                        generateFinalPrescription()
                    }else if(audio1=="recieved" && audio2=="recieved"){
console.log('not possible situation!!, computation failed');
audio1="notRecieved";
finalPrescriptionReady=false;
audio2="notRecieved";
finalPrescriptionReady=false   
}
            })


    });
});

 function runn(){
    let readMe = fs.readFileSync('./Images/'+'conversation.wav');  
     
      const audioBytes = readMe.toString('base64');
       var result='';
        var spawn = require("child_process").spawn; 
        var command= spawn('python',["getPrescriptions.py",'conversation.wav'])
        command.stdout.on('data',function(data){
                result+=data;
                 
        });
        command.on('close',function(code){
            result=JSON.parse(result)
            if(audio1=="notRecieved"){
                audio1="recieved";
                finalPrescriptionReady=false;
                audio1Result=result
                console.log('case 1 ',audio1Result);
               
            }else if(audio1=="recieved" && audio2=="notRecieved"){
                audio2="recieved"
                finalPrescriptionReady=false;
                audio2Result=result
                console.log('case 2',audio2Result)
                generateFinalPrescription()
            }else if(audio1=="recieved" && audio2=="recieved"){
console.log('not possible situation!!, computation failed');
audio1="notRecieved";
finalPrescriptionReady=false;
audio2="notRecieved";
finalPrescriptionReady=false   
}
})
}

runn();
runn();


app.get('/getPrescription',(req,res)=>{

if(finalPrescriptionReady==false){
    res.send("Prescription not ready");
    return;
}else{
    let ffinalResult={}
    ffinalResult['disease']=finalResult['disease'][0][0]+'_'+finalResult['disease'][0][1]+','+finalResult['disease'][1][0]+'_'+finalResult['disease'][1][1]
   let arr=finalResult['symptoms']
   let sympp='';
   for (let i =0 ;i<arr.length;i++){
    sympp+=arr[i]
    sympp+=','
   }
   ffinalResult['symptoms']=sympp; 
   let arrr=finalResult['dosage']
   let symppr='';
   for (let i =0 ;i<arrr.length;i++){
    symppr+=arrr[i]
    symppr+=','
   }
   ffinalResult['dosage']=symppr;
   res.send(ffinalResult)
}
})


// for await (const error of child.stderr) {
//     console.log(`number of err files: ${error}`);
//   };
     
// for await (const data of child.stdout) {
  
//     console.log(`number of files: ${data}`);
//     console.log(data['symptoms']);
    
//   };
    
   










app.get('/processCommand',function(request,response){
        console.log( request);
        response.send('OK');
        });

app.get('/verify_meeting_key/:meeting_key',(req,res)=>{
    res.send("key verified");
})

app.get('/verify_otp/:otp',(req,res)=>{
    res.send({
        SESSION_ID:'2_MX40NjQ1OTUzMn5-MTU3NDA5NDI3MjMwOX5CTTlXT2dqa002cFpvWXpLOU9kLzVtZFh-fg',
        TOKEN:'T1==cGFydG5lcl9pZD00NjQ1OTUzMiZzaWc9NTFmZDc4ZWRiYzRiM2Q5YzIzZDZlZDUxOWM4MWQxOGNkMTIzOTUwYzpzZXNzaW9uX2lkPTJfTVg0ME5qUTFPVFV6TW41LU1UVTNOREE1TkRJM01qTXdPWDVDVFRsWFQyZHFhMDAyY0ZwdldYcExPVTlrTHpWdFpGaC1mZyZjcmVhdGVfdGltZT0xNTc0MDk0MzIxJm5vbmNlPTAuMzI2MjQyNDc4OTUwOTI2NCZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTc0Njk5MTIwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9'
        ,API_KEY:'46459532'    });
})

    
app.listen(5000,function(){
    console.log("Started Voice Assistant server on PORT 5000....");
  })
