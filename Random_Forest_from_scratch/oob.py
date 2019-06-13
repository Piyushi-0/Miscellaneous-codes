import sys
import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
import pickle
import datetime

import os
import random
from math import log,sqrt
np.random.seed(0)
random.seed(0)

startt=datetime.datetime.now()
#LOADING DATASET

with open("spam.csv") as f:
    lis=[line.split() for line in f] 

l1=len(lis)
l2=len(lis[0])
for i in range(l1):
    for j in range(l2):
        lis[i][j]=float(lis[i][j])
        
df=pd.DataFrame(lis)

dtrain, dtest = train_test_split(df, test_size=0.3)     
#LOADING DATASET ENDS
M=len(dtrain.columns)-1#TOTAL NO. OF FEATURES

ml=[1,6,12,18,24,30,36,42,48,54,M]
ml.append(int(sqrt(M)))
ml.append(int(sqrt(M)/2))
ml.append(int(2*sqrt(M)))
ml.append(int(np.log(M)))
ml=set(ml)
ml=list(ml)
ml.sort()
lml=len(ml)#LENGTH
K=15#NO. OF TREES

ntest=len(dtest)#NO. OF TEST INSTANCES
ntrain=len(dtrain)#NO. OF TRAINING INSTANCES

#TESTING FOR CLASS LABEL
def test(x, ix, tree):
    #print("Testing")
    root=tree[ix]
    if isinstance(root, list):#NOT A LEAF
        a=root[0]#CHOSEN ATTRIBUTE TO SPLIT
        v=root[1]#CHOSEN VALUE TO SPLIT

        if x.iloc[0,a]<v:
            return test(x, root[2], tree)#RECURSE FOR LEFT CHILD
        else:
            return test(x, root[3], tree)#RECURSE FOR RIGHT CHILD
    else:#LEAF SO RETURN CLASS LABEL
        return root
#TESTING FOR CLASS LABEL ENDS     

import joblib

DD=joblib.load("DD"+str(K))
TT=joblib.load("TT"+str(K))

oob=np.zeros(lml, dtype=float)
for mx in range(0, lml):
    print("For m "+str(ml[mx]))
    dfm=DD[mx]
    treerm=TT[mx]

    er=0
    n0=np.zeros(ntrain,dtype=int)#How many times 0 voted for this training instance
    n1=np.zeros(ntrain,dtype=int)
    for tx in range(0, ntrain):
        for dx in range(0,K):
            x=dtrain.iloc[tx:tx+1,:]        
            
            treedf=dfm[dx]      
            #CHECKING IF TRAIN INSTANCE IS PRESENT IN THIS DATASET      	
            val=(x.values==treedf.values)
            row,_=np.nonzero(val)#The Matching row indexes in ndarray, row
            _,count=np.unique(row, return_counts=True)#Frequency of the corresponding matching row indexes in ndarray, count
            isin=len(np.nonzero(count==len(dfm[dx].columns) )[0])#If there is any row that has true in all columns
            if isin==1:
                print("Present in this tree")
                continue
            #CHECKING IF TRAIN INSTANCE IS PRESENT IN THIS DATASET
            
            print("Not present in this tree")#Including votes of only these
            tree=treerm[dx][0]
            r=treerm[dx][1]
            y=dtrain.iloc[tx:tx+1,-1] 
            x=dtrain.iloc[tx:tx+1,:-1] 
            y_p=test(x, r-1, tree)

            if y_p==0:
            	n0[tx]+=1
            else:
            	n1[tx]+=1
        
        if (n0[tx]>n1[tx] and y.values[0]==1) or (n0[tx]<n1[tx] and y.values[0]==0):#Error. Can't be equal unless both 0(Then that appeared everywhere)
            	er+=1#Error for this training instance
    oob[mx]=er/(ntrain*1.0)#er.mean()

with open("oob"+str(K),"wb") as fil:
    pickle.dump(oob,fil)

print(oob)
endt=datetime.datetime.now()
print(endt-startt)
