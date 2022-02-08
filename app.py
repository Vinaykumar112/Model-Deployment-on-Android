# -*- coding: utf-8 -*-
"""
Created on Sun Jan 30 12:27:00 2022

@author: vinis
"""
import numpy as np
import pickle
import nltk
import re
nltk.download('stopwords')
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from flask import Flask,request,jsonify

vec_file=pickle.load(open('vectorizer.sav','rb'))
model_2=pickle.load(open('Example2.sav','rb'))

app=Flask(__name__)

@app.route('/')
def index():
    return "Hello world"

@app.route("/sentiment",methods=["POST"])
def sentiment(): 
    review=request.form.get('review')
    result = model(review)
    # return result
    # result={'review':review}
    return jsonify({'Result':result})

def model(review):
    x=predict_sentiment(review)
    x1=np.array([1])
    if np.array_equal(x,x1):
        return 1
    else:
        return 0

def predict_sentiment(sample_review):
    sample_review = re.sub(pattern='[^a-zA-Z]',repl=' ', string = sample_review)
    sample_review = sample_review.lower()
    sample_review_words = sample_review.split()
    sample_review_words = [word for word in sample_review_words if not word in set(stopwords.words('english'))]
    ps = PorterStemmer()
    final_review = [ps.stem(word) for word in sample_review_words]
    final_review = ' '.join(final_review)
    temp = vec_file.transform([final_review]).toarray()
    return model_2.predict(temp)


if __name__=="__main__":
    app.run(debug=False)