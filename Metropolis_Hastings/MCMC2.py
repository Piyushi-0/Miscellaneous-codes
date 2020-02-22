'''
Implementation of Bayesian MCMC Metropolis Hastings Algorithm from scratch(MCMC.py, MCMC2.py) & with 
pymc3(Inbuilt MCMC.ipynb) for Pumps:conjugate gamma-Poisson hierarchical model.
http://www.openbugs.net/Examples/Pumps.html
'''

import joblib
import numpy as np

from scipy.stats import poisson
from scipy.stats import norm
from scipy.stats import gamma
from scipy.stats import expon

a_posterior=joblib.load('aposterior')
b_posterior=joblib.load('bposterior')

t=np.array([94.3, 15.7, 62.9, 126, 5.24, 31.4, 1.05, 1.05, 2.1, 10.5])

import sys

x=np.zeros(10)

for j in range(0, 10):
	a_pos=a_posterior[j][1000:]
	b_pos=b_posterior[j][1000:]
	
	a=np.mean(a_pos)
	b=np.mean(b_pos)
	
	#qm=gamma.stats(a=a, scale=b)[0]
	
	q=gamma.rvs(a=a, scale=b, size=1)
	
	x[j]=poisson.rvs(mu=q*t[j], size=1)
	#x[j]=poisson.stats(mu=qm*t[j])[0]
	
data=np.array([5, 1, 5, 14, 3, 19, 1, 1, 4, 22])
from sklearn.metrics import mean_squared_error
print(mean_squared_error(data, x))#133.4
