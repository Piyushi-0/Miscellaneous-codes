/*C program for sporadic task acceptance test*/
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

int slack(int idle[],int i,int d[],int r[])
{
int ic=0,j=0;
lt=0;
while(idle[j]+lt<d[i])
	{
	if(idle[j]+lt>=r[i])
		{//printf("%d\n",idle[j]+lt);
		ic++;
		}
	j++;
	if(idle[j]==-1)
		{
		j=0;
		lt+=H;
		}
	}
return ic;
}

void main()//Tasks that are already scheduled
{
t[0].p=4;t[1].p=12;t[2].p=6;t[0].e=1;t[1].e=2;t[2].e=1;
printf("Enter how many sporadic tasks\n");
scanf("%d",&n);
int i,j,time=0,accepted[n],r[n],ex[n],d[n],flag;
printf("Enter r,e,d for these\n");
for(i=0;i<n;i++)
scanf("%d%d%d",&r[i],&ex[i],&d[i]);
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
	t[i].m=tmp;
	}
for(i=0;i<n;i++)
	{
	printf("Enter release, execution, deadline of sporadic %d\n",i);
	scanf("%d%d%d",&r[i],&ex[i],&d[i]);
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

int ld[n],hd[n],l,h;//higher & lower deadlne already accepted
for(i=0;i<n;i++)//sporadic jobs in order of arrival
	{
	for(j=0;j<n;j++)
	ld[j]=hd[j]=-1;
	l=h=0;
	if(i==0)
		{
		if(slack(idle,i,d,r)<ex[i])
			{
			printf("slack %dSporadic job %d is rejected\n",slack(idle,i,d,r),i);
			}
		else
			{
			printf("slack %dSporadic job %d is accepted\n",slack(idle,i,d,r),i);
			accepted[i]=1;
			}
		}
	else
		{
	if(slack(idle,i,d,r)<ex[i])
	printf("slack %ds%d rejected\n",slack(idle,i,d,r),i);
	else
		{
		for(j=0;j<i;j++)//in order of arrival
			{
			if(accepted[j]==1)
				{
				if(d[i]>d[j])
				ld[l++]=j;
				else
				hd[h++]=j;
				}
			}
		//for those with lower deadline than this, this should accom in it then oppposite for higher
			int sum=0;
			for(j=0;j<l;j++)
				{
				sum+=ex[ld[j]];
				}
			flag=1;
			for(j=0;j<h;j++)
				{
				if(slack(idle,hd[j],d,r)<ex[i]+ex[hd[j]])
				flag=0;
				}
			if(slack(idle,i,d,r)>=sum+ex[i] && flag==1)
			printf("slack %ds%d is accepted\n",slack(idle,i,d,r),i);
			else
			printf("slack %ds%d is rejected\n",slack(idle,i,d,r),i);
		}
		}
	}
}
