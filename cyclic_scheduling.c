/*
C program for cyclic scheduling in RTS
*/
#include<stdio.h>
int n;
int gcd(int a,int b)
{
int i=a;
while(1)
	{
	if(i%a!=0 || i%b!=0)
		return i;
	else i++;
	}
}
struct s
{
int lx,m,present;
int ex,rex,p,d,arv[500];
};
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
	t[i].present=0;
	}
for(i=0;i<n;i++)
	u+=t[i].ex/(float)t[i].p;
if(u>1)
	{
	printf("Can't schedule properly\n");
	return;
	}
int time,H,f;
time=0;H=t[0].p;
do
	{
	flag=1;
	ti++;
	for(i=0;i<n;i++)
		{
		if(H*ti%t[i].p!=0)
		flag=0;
		}
	}while(flag==0);
H=H*ti;
for(i=0;i<n;i++)
	{
	if(i==0 || f<t[i].ex)
	f=t[i].ex;
	}
while(1)
	{
	if(H%f!=0)
		{
		f++;
		continue;
		}
	for(i=0;i<n;i++)
		{
		if(2*f-gcd(t[i].p,f)>t[i].d)
			{
			f++;
			continue;
			}
		}
	if(f>0)
	break;
	}
printf("frame=%d\n",f);
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
int j;
while(time<H)
{
if(time%f==0)
	{
	for(i=0;i<n;i++)
		{
		if(t[i].lx+1<t[i].m && t[i].arv[t[i].lx+1]<=time)//next instance remaining has arrived
			{
			t[i].present=1;
			}
		}
	}
int x;
for(j=0;j<f;j++)
	{
	x=-1;
	for(i=0;i<n;i++)
		{
		if(t[i].present==1)
			{
			if(x==-1 || t[x].arv[t[x].lx]+t[x].d<t[i].arv[t[i].lx]+t[i].d)
			x=i;
			}
		}
	if(x<0)
	printf("Idle at %d\n",time+j);
	else
		{
		t[x].re--;
		printf("Executing %d at time %d",x,time+j);
		t[x].present=0;
		}
	if(t[x].re==0)
		{
		t[x].lx++;
		t[x].re=t[x].ex;
		}
	}
time=time+f;
}
}

