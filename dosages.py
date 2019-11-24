import pandas as pd
import nltk
from nltk.stem import WordNetLemmatizer
from nltk.corpus import wordnet as wn
from itertools import product


def getDosages(inputDiseases):
    Data  = pd.read_csv('Disease-symptoms-medication-dataset.csv')
    medicine = list(Data['Medicine'])
    diseases=list(Data['Disease'])
    answer=[]
    # print(len(medicine),len(diseases))
    for i in range(0,len(diseases)):
        for inputs in inputDiseases: 
            if nltk.edit_distance(inputs,diseases[i])<=2:
                # print(medicine[i],inputs)
                if str(medicine[i])=="nan":
                    continue
                else:
                    for w in medicine[i].split(','):
                        answer.append(w)
    return answer

# print(getDosages(['hepatitis']))


