from pandas import Series, DataFrame
import pandas as pd
import numpy as np
import nltk
import re
from nltk.stem import WordNetLemmatizer
from sklearn.svm import LinearSVC
from sklearn.metrics import classification_report
import sklearn.metrics
from sklearn.feature_extraction.text import TfidfVectorizer,HashingVectorizer
from sklearn.ensemble import RandomForestClassifier
# from sklearn import grid_search
from sklearn.model_selection import GridSearchCV
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import FeatureUnion

import sys
reload(sys)  
sys.setdefaultencoding('utf8')

# A combination of Word lemmatization + LinearSVC model finally pushes the accuracy score past 80%

traindf = pd.read_json("/home/daizhaohui/Documents/all/document/deep_learn/whatiscooking/train.json")
traindf['ingredients_clean_string'] = [' , '.join(z).strip() for z in traindf['ingredients']]  
traindf['ingredients_string'] = [' '.join([WordNetLemmatizer().lemmatize(re.sub('[^A-Za-z]', ' ', line)) for line in lists]).strip() for lists in traindf['ingredients']]       

testdf = pd.read_json("/home/daizhaohui/Documents/all/document/deep_learn/whatiscooking/test.json") 
testdf['ingredients_clean_string'] = [' , '.join(z).strip() for z in testdf['ingredients']]
testdf['ingredients_string'] = [' '.join([WordNetLemmatizer().lemmatize(re.sub('[^A-Za-z]', ' ', line)) for line in lists]).strip() for lists in testdf['ingredients']]       



corpustr = traindf['ingredients_string']
estimators = [("tfidf", TfidfVectorizer(stop_words='english',
             ngram_range = ( 1 , 1 ),analyzer="word",
             max_df = .57 , binary=False ,max_features =6706, token_pattern=r'\w+' , sublinear_tf=False) ),
             ("hash", HashingVectorizer ( stop_words='english',
             ngram_range = ( 1 , 2 ),n_features  =6706,analyzer="word",token_pattern=r'\w+', binary =False))]

tfidftr=FeatureUnion(estimators).fit_transform(corpustr).todense()

predictors_tr = tfidftr

corpusts = testdf['ingredients_string']
tfidfts = FeatureUnion(estimators).transform(corpusts)

targets_tr = traindf['cuisine']

predictors_ts = tfidfts


classifier = LinearSVC(C=1, penalty="l2", dual=False)
# parameters = {'C':[1, 10]}
# parameters = {"max_depth": [3, 5,7]}
# clf = LinearSVC()
# clf = LogisticRegression()
# clf = RandomForestClassifier(n_estimators=100, max_features="auto",random_state=50)

# classifier = grid_search.GridSearchCV(clf, parameters)
# classifier = GridSearchCV(clf, parameters)
# classifier = RandomForestClassifier(n_estimators=200)

classifier=classifier.fit(predictors_tr,targets_tr)

predictions=classifier.predict(predictors_ts)
testdf['cuisine'] = predictions
testdf = testdf.sort_values('id' , ascending=True)

testdf[['id' , 'ingredients_clean_string' , 'cuisine' ]].to_csv("/home/daizhaohui/Documents/all/document/deep_learn/whatiscooking/submission77.csv")
