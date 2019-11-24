import nltk 
from nltk.corpus import stopwords 
from nltk.tokenize import word_tokenize, sent_tokenize 
stop_words = set(stopwords.words('english')) 


def posTagger(textInput):
    tokenized = sent_tokenize(textInput) 
    allTags=[]
    for i in tokenized: 
      
    # Word tokenizers is used to find the words  
    # and punctuation in a string 
        wordsList = nltk.word_tokenize(i) 
  
    # removing stop words from wordList 
        wordsList = [w for w in wordsList if not w in stop_words] 
         
  
    #  Using a Tagger. Which is part-of-speech  
    # tagger or POS-tagger.  
        tagged = nltk.pos_tag(wordsList) 
        for tags in tagged:
            allTags.append(tags)
    return allTags

def importantTags(inputText): 
    wordList = posTagger(inputText) 
    finalAnswer=[]
    for words in wordList:
        finalAnswer.append(words[0])
    return finalAnswer


