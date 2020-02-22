/*
C program for Aperiodic Task Scheduling in RTS
*/

#include<stdio.h>
int n;
int lt=0,H;
struct
{
int p,e,arv_at[100],lx,m,re;
}t[3];
int edf(int time)
	{
	int i,minx=-1;
	for(i=0;i<3;i++)
		{
		if(t[i].lx+1<t[i].m && t[i].arv_at[t[i].lx+1]<=time)//next instance remaining has arrived
			{
			if(minx==-1)
			minx=i;
			else if(t[minx].p+t[minx].arv_at[t[minx].lx+1]>t[i].p+t[i].arv_at[t[i].lx+1])
			minx=i;
			}
		}
	return minx;
	}
void slack(int idle[],int i,int r[],int rex[])
{
int ic=0,j=0;
lt=0;
while(rex[i]>0)
	{
	if(idle[j]+lt>=r[i])
		{//printf("%d\n",idle[j]+lt);
		ic++;
		rex[i]--;
		}
	if(rex[i]==0)
	break;
	j++;
	if(idle[j]==-1)
		{
		j=0;
		lt+=H;
		}
	}
printf("aperiodic %d completed at %d\n",i,idle[j]+lt);
}
void main()//Tasks that are already scheduled
{
t[0].p=4;t[1].p=12;t[2].p=6;t[0].e=1;t[1].e=2;t[2].e=1;
printf("Enter how many aperiodic tasks\n");
scanf("%d",&n);

int i,j,time=0,accepted[n],r[n],ex[n],rex[n],flag;
printf("Enter r,e for these\n");
for(i=0;i<n;i++)
scanf("%d%d",&r[i],&ex[i]);
H=t[0].p;
do
	{
	flag=1;
	for(i=0;i<3;i++)
		{
		if(H%t[i].p!=0)
			{
			H++;
			flag=0;
			}
		}
	}while(flag==0);
int idle[H],tmp;
for(i=0;i<3;i++)
{
t[i].lx=-1;
t[i].re=t[i].e;
tmp=0;
for(j=0;j<H;j++)
	{
	if(j%t[i].p==0)
		{
		t[i].arv_at[tmp++]=j;
		}
	}
t[i].m=tmp;}
for(i=0;i<n;i++)
	{
	rex[i]=ex[i];
	accepted[i]=0;
	}
for(i=0;i<H;i++)
idle[i]=-1;
tmp=0;
while(time<H)
	{
	int x=edf(time);
	if(x==-1)
		{
		//
		printf("Idle at time %d\n",time);
		idle[tmp++]=time;
		time++;
		continue;
		}
	//printf("Executing instance %d of task %d at time %d\n",t[x].lx+2,x+1,time);
	t[x].re--;
	if(t[x].re==0)
		{
		t[x].lx++;
		t[x].re=t[x].e;
		}
	time++;
	}
for(i=0;i<n;i++)//jobs in order of arrival
	{
	slack(idle,i,r,rex);
	}
}
