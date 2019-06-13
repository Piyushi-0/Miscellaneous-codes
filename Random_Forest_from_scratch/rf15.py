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
#np.random.seed(0)
#random.seed(0)

prog_time1=datetime.datetime.now()
#LOADING DATASET
with open("spam.csv") as f:
    lis=[line.split() for line in f] 

l1=len(lis)
l2=len(lis[0])
for i in range(0,l1):
    for j in range(0,l2):
        lis[i][j]=float(lis[i][j])
        
df=pd.DataFrame(lis)

dtrain, dtest = train_test_split(df, test_size=0.3)     
#LOADING DATASET ENDS
M=len(dtrain.columns)-1#TOTAL NO. OF FEATURES

#ml=[i for i in range(1,M+1)]#DIFFERENT m VALUES

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

#CHECK IF PURE NODE
def qual(df):
    if len(df)==len(df[df.iloc[:,-1]==0]):
        return 0#YES, CLASS=0
    if len(df)==len(df[df.iloc[:,-1]==1]):
        return 1#YES, CLASS=1
    return -1
#CHECK IF PURE NODE ENDS

#CALCULATE ENTROPY
def entropy(df):#Should pass dataframe of newly split data
    n0=len(df[df.iloc[:,-1]==0])
    n1=len(df[df.iloc[:,-1]==1])
    p0=n0/((n0+n1)*1.0)
    p1=n1/((n0+n1)*1.0)
    e=0
    if p0>0:
        e+=p0*log(p0,2)
    if p1>0:
        e+=p1*log(p1,2)
    e=-e
    return e
#CALCULATE ENTROPY ENDS    

#CALCULATE GINI
def gini(df):
    n0=len(df[df.iloc[:,-1]==0])
    n1=len(df[df.iloc[:,-1]==1])
    p0=n0/((n0+n1)*1.0)
    p1=n1/((n0+n1)*1.0)
    g=1-p0*p0-p1*p1
    return g
#CALCULATE GINI ENDS    

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
#################################################

#ATTRIBUTE,VALUE,ENTROPY FOR BEST SPLIT
def sval(df,m):
    nc=len(df.columns)-1#Except last quality column
    #col=np.random.randint(0,nc,m)#Randomly pick indexes
    col=random.sample(range(0,nc),m)
    min_e=-1
    min_a=-1
    min_v=-1
    i=0
    for mx in range(0,m):
        i=col[mx]#index of column picked
        val=np.unique(df.iloc[:,i])
        for j in range(0,len(val)):
            df_l=df[df.iloc[:,i]<val[j]]
            df_r=df[df.iloc[:,i]>=val[j]]
            if(len(df_l)==0 or len(df_r)==0):
                continue
            e_l=entropy(df_l)
            e_r=entropy(df_r)
            e=(len(df_l)*e_l+len(df_r)*e_r)/((len(df_l)+len(df_r))*1.0)
            if min_e==-1 or e<min_e:
                min_e=e
                min_a=i
                min_v=val[j]
    return min_a, min_v, min_e
#ATTRIBUTE,VALUE,ENTROPY FOR BEST SPLIT ENDS

#TRAINING & BUILDING A TREE FOR GIVEN m
def train(df,tree,m):#Entry at cntr=0 will be of root
    global cntr
    q=qual(df)#CLASS LABEL IF LEAF, -1 IF NON-LEAF
    if q!=-1:#LEAF
        tree[cntr]=q#ONLY STORE CLASS LABEL AT THIS INDEX OF DICTIONARY
        cntr+=1#NEXT NODE TO BE ENTERED AT THIS INCREMENTED VALUE
        return cntr
    ent=entropy(df)#BEFORE SPLITTING ENTROPY
    a, v, e=sval(df,m)#ATTRIBUTE, VALUE, ENTROPY AFTER BEST SPLIT
    if ent-e==0:#INFO GAIN 0
        majority=0#MAJORITY CLASS
        if len(df[df.iloc[:,-1]==1])>len(df[df.iloc[:,-1]==0]):
            majority=1
        tree[cntr]=majority#LEAF SO ONLY STORE CLASS LABEL AT THIS INDEX OF DICTIONARY
        cntr+=1#NEXT NODE TO BE ENTERED AT THIS INCREMENTED VALUE
        return cntr

    #NOT LEAF SO RECURSE
    cntr1=train(df[df.iloc[:,a]<v],tree,m)
    cntr2=train(df[df.iloc[:,a]>=v],tree,m)
    tree[cntr]=[a,v,cntr1-1,cntr2-1]#STORE ATTRIBUTE, VALUE, INDEX OF LCHILD, INDEX OF RCHILD
    cntr+=1#NEXT NODE TO BE ENTERED AT THIS INCREMENTED cntr INDEX
    return cntr
#TRAINING & BUILDING A TREE FOR GIVEN m ENDS

acc=np.zeros(lml,dtype=float)#ACCURACY FOR EVERY m
sen=np.zeros(lml,dtype=float)#SENSITIVITY FOR EVERY m
time_train=np.zeros(lml,dtype=float)#TIME-TAKEN FOR EVERY m

DD=[]
TT=[]
for mx in range(0,lml):
    m=ml[mx]#FOR A FIXED m TO BUILD ALL TREES
    t_train=datetime.datetime.now(); #t_test=datetime.datetime.now()#TIME TAKEN FOR TRAIN AND TEST
    D=[]#Stores all dfs for given m
    T=[]#Stores all trees for given m
    n1=np.zeros(ntest,dtype=int)#ENTRY AT INDEX i REPRESENTS HOW MANY TREES SAY INSTANCE AT i BELONGS TO 1, for given m
    n0=np.zeros(ntest,dtype=int)#ENTRY AT INDEX i REPRESENTS HOW MANY TREES SAY INSTANCE AT i BELONGS TO 0, for given m
    
    for nt in range(0, K):#BUILDING AND TESTING ON A TREE
        print("Building Tree "+str(nt+1)+"for m="+str(m))
        dtrn=dtrain.sample(frac=0.66)#SAMPLED
        D.append(dtrn)
        tree={}
        cntr=0

    	st=datetime.datetime.now()
        r=train(dtrn,tree,m)#TREE BUILT
        et=datetime.datetime.now()
        
        T.append([tree,r])
        if nt==0:#Our analysis is for each m. Given an m, we need sum of training time for all trees
        	t_train=et-st#for first tree
        else:
        	t_train+=et-st#TIME TAKEN TO BUILD a TREE ADDED    
        #For next m, nt will again start from 0 so we're storing every m's value 
        #test

        for tx in range(0, ntest):#TESTING ALL INSTANCES ON THAT TREE
            x=dtest.iloc[tx:tx+1,:-1]#1 ROW=1 TEST INSTANCE
            
            #st=datetime.datetime.now()
            
            y_p=test(x, r-1,tree)#PREDICTED
            
            #et=datetime.datetime.now()         
            #if tx==0:
            	#t_test=et-st
            #else:
            	#t_test+=et-st#TIME TAKEN TO TEST 1 INSTANCE
            
            if y_p==0:
                n0[tx]+=1
            else:
                n1[tx]+=1
    #All trees built for given m
    DD.append(D)
    TT.append(T)
    cor=0#NO. OF CORRECT
    tp=0#TRUE POSITIVE
    fn=0#FALSE NEGATIVES
    #DECIDING LABEL. NOT ADDING IN TESTING
    #st=datetime.datetime.now()
    for tx in range(0, ntest):
        if n0[tx]>=n1[tx] and dtest.iloc[tx,-1]==0:
            cor+=1
        if n1[tx]>n0[tx] and dtest.iloc[tx,-1]==1:
            cor+=1
            tp+=1     	
        if n0[tx]>n1[tx] and dtest.iloc[tx,-1]==1:                    
            fn+=1   
    #et=datetime.datetime.now()                
    #t_test+=et-st
    acc[mx]=cor/(ntest*1.0)*100
    sen[mx]=tp/((tp+fn)*1.0)
    time_train[mx]=t_train.total_seconds()    
    #time_test[mx]=t_test.total_seconds()   #Time taken for testing doesn't depend on m

with open("DD"+str(K),"wb") as f:
    pickle.dump(DD,f)
with open("TT"+str(K),"wb") as f:
    pickle.dump(TT,f)    
with open("acc"+str(K),"wb") as f:
    pickle.dump(acc,f)
with open("sen"+str(K),"wb") as f:
    pickle.dump(sen,f)
with open("traintime"+str(K),"wb") as f:
    pickle.dump(time_train,f)       
#with open("testtime"+str(K),"wb") as f:
    #pickle.dump(time_test,f)           

prog_time2=datetime.datetime.now()
print(prog_time2-prog_time1)
