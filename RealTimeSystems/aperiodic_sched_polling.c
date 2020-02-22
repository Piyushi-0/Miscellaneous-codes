/*
C program for Aperiodic Scheduling using Polling Server in RTS
*/
#include<stdio.h>
struct s
{
int lx;
int ex,rex,p,d,arv[500];
};
int n;
int es,ps;
int ar,ae;
int polling(struct s t[])
	{
	int i=n-1,j,sum=0;
	for(j=0;j<i;j++)
		{//printf("sum %d\n%d\n%d\n",t[i].d, t[j].p, t[j].e);
		sum+=ceil( t[i].d/(t[j].p*1.0)*t[j].e );
		}//printf("%d\n",sum);
	int ti=t[i].d;//printf("%d\n",4*ps*(ti-t[j].e-sum));
	int es=(t[i].d-t[i].e-sum)*ps/t[i].d;
	if(es>0 && es<ps)
	return es;
	else return -1;
	}

int edf(int time,struct s t[])
{
int i,minx=-1;
for(i=0;i<n;i++)
	{
	if(t[i].arv[t[i].lx+1]<=time)//next instance remaining has arrived
		{
		if(i==n)//server
			{
			if(ar>time || ae==0 || es==0)//aperiodic not arrived or completed or no budget
			continue;
			}
		if(minx==-1)
		minx=i;
		else if(t[minx].p+t[minx].arv[t[minx].lx+1]>t[i].p+t[i].arv[t[i].lx+1])
		minx=i;
		}
	}
	return minx;
}

void main()
{
printf("Enter how many periodic tasks\n");
scanf("%d",&n);
struct s t[n+1];
float u=0.0;
int i,flag,ti=0,tmp,tm;
for(i=0;i<n;i++)
	{
	printf("Enter period, execution time, deadline of task %d\n",i);
	scanf("%d%d%d",&t[i].p,&t[i].ex,&t[i].d);
	t[i].rex=t[i].ex;
	t[i].lx=-1;
	}
printf("Enter period of server\n");
scanf("%d%d",&ps,&es);
t[n].p=t[n].d=ps;
t[n].ex=t[n].rex=es=polling(t);
t[n].lx=-1;
es=0;
for(i=0;i<n;i++)
u+=t[i].ex/(float)t[i].p;
if(u>1)
	{
	printf("Can't schedule properly\n");
	return;
	}
int time;
time=0;

printf("Enter release time, execution time of aperiodic task\n");
scanf("%d%d",&ar,&ae);
while(ae>0)
	{
	if(time>=ar && time%ps==0)
	es=t[n].ex;//replenishment
	int x=edf(time,t);
	if(x<0)
		{
		printf("Idle at time %d\n",time);
		}
	else if (x==n)
		{
		printf("Executing aperiodic task on server\n");
		ae--;
		es--;
		if(es<0)
		es=0;
		}
	else
		{
		printf("Executing instance %d task %d at time %d\n",t[x].lx+1+1,x+1,time);
		t[x].rex--;
		}
		if(t[x].rex==0)
			{
			t[x].lx++;
			t[x].rex=t[x].ex;
			}
		time++;
	}
}
