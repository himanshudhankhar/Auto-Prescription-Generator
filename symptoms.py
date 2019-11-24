import pandas as pd
import nltk

from nltk.stem import WordNetLemmatizer
from nltk.corpus import wordnet as wn
from itertools import product

def getSymptoms(symps):
    MainSymps = pd.read_csv('symps.csv')
    MainSymps.columns={'numbering','name'}
    names=list(MainSymps['name'])
    answer=[]
    
    #do lemmatisation of input parts 
    wordnet_lemmatizer = WordNetLemmatizer()
    for word in symps:
        word_new = wordnet_lemmatizer.lemmatize(word)
        for name in names:
            if (nltk.edit_distance(word_new,str(name)) <=1 or nltk.edit_distance(word,str(name))<=1) and name not in answer:
                answer.append(name) 
    return answer

# print(getSymptoms(['pain','headache','yellow','stool','cough','blood']))            


