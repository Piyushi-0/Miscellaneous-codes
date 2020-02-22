/*
C program for Rate Monotonic scheduling in RTS
*/
#include<stdio.h>
struct s
{int p,e,lx,m,re,arv_at[100],pr,d;
};
int n;
int rm(int time, struct s t[])
	{
	int i,ex=-1;
	for(i=0;i<n;i++)
		{
		if(t[i].lx+1<t[i].m && t[i].arv_at[t[i].lx+1]<=time)
			{
			if(ex==-1)
			ex=i;
			else if(t[ex].pr<t[i].pr)
			ex=i;
			}
		}
	return ex;
	}
void main()
{
int i,j,max,temp,flag;
float u=0.0;
printf("Enter total no. of tasks\n");
scanf("%d",&n);
struct s t[n];
for(i=0;i<n;i++)
	{
	printf("Enter period, execution, deadline of task %d\n",i);
	scanf("%d%d%d",&t[i].p,&t[i].e,&t[i].d);
	t[i].pr=0;
	}
temp=n;
for(i=0;i<n;i++)
	{
	for(j=0;j<n;j++)//1/d smaller=>d larger
		{
		if(t[j].pr==0)
			{
			max=j;
			break;
			}
		}
	for(j=0;j<n;j++)
		{
		if(t[j].pr==0 && t[j].d>t[max].d)
		max=j;
		}
	t[max].pr=temp--;
	}

for(i=0;i<n;i++)
u+=t[i].e/(float)t[i].p;

if(u>1)
	{
	printf("Can't schedule\n");
	return;
	}
int H=t[0].p;
do
	{
	flag=1;
	for(i=0;i<n;i++)
		{
		if(H%t[i].p!=0)
			{
			H++;
			flag=0;
			}
		}
	}while(flag==0);
for(i=0;i<n;i++)
	{
	int tmp=0;t[i].lx=-1;t[i].re=t[i].e;
	for(j=0;j<H;j++)
		{
		if(j%t[i].p==0)
			{
			t[i].arv_at[tmp++]=j;
			}
		}
	t[i].m=tmp;
	}
int time=0;
printf("Scheduling\n");
while(time<H)
	{
	int x=rm(time,t);
	if(x==-1)
		{
		printf("Idle at time %d\n",time);
		time++;
		continue;
		}
	printf("J%d%d at time %d\n",x+1,t[x].lx+2,time);
	t[x].re--;
	if(t[x].re==0)
		{
		t[x].lx++;t[x].re=t[x].e;
		}
	time++;
	}
}
