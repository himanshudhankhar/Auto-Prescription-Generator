import sys
import speech_recognition as sr 
r = sr.Recognizer()  
import wave, numpy, struct
from posTagger import importantTags
import json
from symptoms import getSymptoms
from diseases import getDiseases
from diseasePredictor import predictDisease
from dosages import getDosages


fileName = sys.argv[1]
file_object = open( './Images/'+sys.argv[1],'rb')
with sr.AudioFile(file_object) as source:
    audio = r.record(source)
try:
    text = r.recognize_google(audio)
    # print("The audio file contains: " + r.recognize_google(audio)) 
    text=importantTags(text)
    symptoms= getSymptoms(text)
    diseases= getDiseases(text)
    predictedDisease=predictDisease(symptoms)
    onlyDiseaseNames=[]
    for predictions in predictedDisease:
        onlyDiseaseNames.append(predictions[0])
        diseases.append(predictions)
    # dosages = getDosages(onlyDiseaseNames)
    prescriptions =  {
        "disease":diseases,
        "symptoms":symptoms,
    } 
    outPut = json.dumps(prescriptions)
    print(outPut)    
except sr.UnknownValueError: 
    print("Google Speech Recognition could not understand audio") 
  
except sr.RequestError as e: 
    print("Could not request results from Google Speech Recognition service; {0}".format(e)) 
