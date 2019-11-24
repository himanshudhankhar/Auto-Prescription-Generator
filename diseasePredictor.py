from gensim.test.utils import common_texts, get_tmpfile
from gensim.models import Word2Vec
import pandas as pd
import pickle as _pickle
from gensim.models import KeyedVectors

def predictDisease(symptoms):
    # path = get_tmpfile("word2vecSecond.model")
    model = Word2Vec.load("word2vecSecondVersion.model")
    # model = KeyedVectors.load("wordVectos.kv", mmap='r')

    #load diseases from the database
    #and check which of the most similar diseases are there
    Data  = pd.read_csv('Disease-symptoms-medication-dataset.csv')
    allDiseases = list(Data['Disease'])
    answer=[]
    for symps in symptoms:
        symps_similar = []
        try:
            symps_similar=list(model.most_similar(symps))
        except KeyError:
            continue
        for ss in symps_similar:
            if ss[0] in allDiseases:
                answer.append(ss)
    return answer


# print(predictDisease(['headache','pain','yellow','stool']))
