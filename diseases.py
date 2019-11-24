import pandas as pd
import nltk
from nltk.stem import WordNetLemmatizer
from nltk.corpus import wordnet as wn
from itertools import product


def getDiseases(inputText):
    #in  list of words text check whether a given word is present or not
    #read disease name from 2 databases 
    Data  = pd.read_csv('Disease-symptoms-medication-dataset.csv')
    allDiseases = list(Data['Disease'])
    answer=[]
    wordnet_lemmatizer = WordNetLemmatizer()
    for word in inputText:
        word_new = wordnet_lemmatizer.lemmatize(word)
        for diseases in allDiseases:
            if word == diseases or word_new==diseases and diseases not in answer:
                answer.append(diseases)
            if (nltk.edit_distance(word,diseases) <= 2 or nltk.edit_distance(word_new,diseases) <=2) and diseases not in answer:
                answer.append(diseases)
    return answer

# print(getDiseases(['jaundice','kala azar']))



