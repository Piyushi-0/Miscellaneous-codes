'''
Implementation of Bayesian MCMC Metropolis Hastings Algorithm from scratch(MCMC.py, MCMC2.py) & with 
pymc3(Inbuilt MCMC.ipynb) for Pumps:conjugate gamma-Poisson hierarchical model.
http://www.openbugs.net/Examples/Pumps.html
'''

import pickle,joblib
import numpy as np
import scipy as sp
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from scipy.stats import poisson
from scipy.stats import norm
from scipy.stats import gamma
from scipy.stats import expon
sns.set_style('white')
sns.set_context('talk')

np.random.seed(123)

from math import factorial
from math import log
import math

data=np.array([5, 1, 5, 14, 3, 19, 1, 1, 4, 22])
t=np.array([94.3, 15.7, 62.9, 126, 5.24, 31.4, 1.05, 1.05, 2.1, 10.5])

def sampler(a_init=1, b_init=1, proposal_width=.15, a_prior_ld=0.1, b_prior_a=0.1, b_prior_b=1.0):
    apos=[]
    bpos=[]
    for j in range(0, 10):
        a_current = a_init
        b_current= b_init
        a_pos = [a_current]
        b_pos=[b_current]
        for sample in range(15000):
		# suggest new position
		a_proposal = norm(a_current, proposal_width).rvs()
		while a_proposal<0:
		    a_proposal = norm(a_current, proposal_width).rvs()
		b_proposal = norm(b_current, proposal_width).rvs()
		while b_proposal<0:
		    b_proposal = norm(b_current, proposal_width).rvs()

		#print(a_proposal, b_proposal)


		# Compute likelihood by multiplying probabilities of each data point

		sL1=0#sum of likelihood with proposal
		sL0=0#sum of likelihood with current


		for i in range(0, 400):
	    		q=gamma.rvs(a=a_proposal, scale=b_proposal, size=1)
			sL1+= poisson.pmf(k=data[j], mu=q*t[j])
		      
	    		q0=gamma.rvs(a=a_current, scale=b_current, size=1)
			sL0+= poisson.pmf(k=data[j], mu=q0*t[j])
			            		
		prior_a_current=a_prior_ld*np.exp(-a_prior_ld*a_current)#expon.pdf(x=a_current, scale=a_prior_ld)
		prior_a_proposal=a_prior_ld*np.exp(-a_prior_ld*a_proposal)#expon.pdf(x=a_proposal, scale=a_prior_ld)

		prior_b_current=gamma.pdf(x=b_current, a=b_prior_a, scale=b_prior_b)
		prior_b_proposal=gamma.pdf(x=b_proposal, a=b_prior_a, scale=b_prior_b)

		#print(prior_a_current, prior_b_current, sL0)

		p1=sL1*prior_a_proposal*prior_b_proposal
		p0=sL0*prior_a_current*prior_b_current

		accept = log(np.random.rand()) < log(p1)-log(p0)

		if accept:
		    # Update position
		    print(str(j)+" "+str(sample)+"accepted")
		    a_current = a_proposal
		    b_current=b_proposal

		    a_pos.append(a_current)
		    b_pos.append(b_current)
        
        apos.append(a_pos)
        bpos.append(b_pos)
        
    return apos, bpos
    
a_posterior,b_posterior=sampler()
print(a_posterior,b_posterior)

with open("aposterior", 'wb') as fp:
    pickle.dump(a_posterior, fp)
with open("bposterior", 'wb') as fp:    
    pickle.dump(b_posterior, fp)    
