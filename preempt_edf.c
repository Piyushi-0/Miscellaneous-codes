/*
C program for pre-emptive EDF scheduling in RTS
*/
#include<stdio.h>
struct s
{
int lx,m;
int ex,rex,p,d,arv[500];
};
int n;
int edf(int time,struct s t[])
{
int i,minx=-1;
for(i=0;i<n;i++)
	{
	if(t[i].lx+1<t[i].m && t[i].arv[t[i].lx+1]<=time)//next instance remaining has arrived
		{
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
printf("Enter how many tasks\n");
scanf("%d",&n);
struct s t[n];
float u=0.0;
int i,flag,ti=0,tmp,tm;
for(i=0;i<n;i++)
	{
	printf("Enter period, execution time, deadline of task %d\n",i);
	scanf("%d%d%d",&t[i].p,&t[i].ex,&t[i].d);
	t[i].rex=t[i].ex;
	t[i].lx=-1;
	}
for(i=0;i<n;i++)
	u+=t[i].ex/(float)t[i].p;
if(u>1)
	{
	printf("Can't schedule properly\n");
	return;
	}
int time,H;
time=0;H=t[0].p;
do
	{
	flag=1;ti++;
	for(i=0;i<n;i++)
		{
		if(H*ti%t[i].p!=0)
		flag=0;
		}
	}while(flag==0);
H=H*ti;
for(i=0;i<n;i++)
	{
	tmp=0;
	for(tm=0;tm<H;tm++)
		{
		if(tm%t[i].p==0)
		t[i].arv[tmp++]=tm;
		}
	t[i].m=tmp;
	}
while(time<H)
	{
	int x=edf(time,t);
	if(x<0)
		{
		printf("Idle at time %d\n",time);
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
