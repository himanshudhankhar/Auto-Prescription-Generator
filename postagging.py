import nltk 
# nltk.download('stopwords')
# nltk.download('punkt')
# nltk.download('averaged_perceptron_tagger')
from nltk.corpus import stopwords 
from nltk.tokenize import word_tokenize, sent_tokenize 
# stop_words = set(stopwords.words('english')) 




def posTagger(command):
    # txt = "Schedule my meeting with Gaurav harit at his room on wednesday 3 P.M. ."  
    tokenized = sent_tokenize(command) 
    for i in tokenized: 
	    wordsList = nltk.word_tokenize(i) 
    tagged=nltk.pos_tag(wordsList) 
    return tagged
	 
